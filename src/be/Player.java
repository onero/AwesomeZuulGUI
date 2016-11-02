package be;

import gui.model.Game;
import gui.view.OutputManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Player {

    private final String mNAME;
    private final int mMAX_LOAD;
    private boolean mHaveKey;
    private final Stack mPREVIOUS_ROOMS;
    private final List<Item> takenItems;
    private final int LOW_HIT_DAMGE = 1;
    private final int MEDIUM_HIT_DAMAGE = 3;
    private final int CRITICAL_HIT_DAMAGE = 5;

    private Room mCurrentRoom;
    private int mLoadLeft;
    private int mDamage;
    private int mHealth;
    private boolean mHasFinalWeapon;

    /**
     * Creates the Player
     *
     * @param name
     * @param health
     * @param maxLoad
     */
    public Player(String name, int health, int maxLoad) {
        mNAME = name;
        mMAX_LOAD = maxLoad;
        mLoadLeft = mMAX_LOAD;
        mPREVIOUS_ROOMS = new Stack();
        takenItems = new ArrayList<>();
        mHaveKey = false;
        mDamage = LOW_HIT_DAMGE;
        mHealth = health;
        mHasFinalWeapon = false;

    }

    /**
     * Gets the players name
     *
     * @return
     */
    public String getPlayerName() {
        return mNAME;
    }

    /**
     * Gets the players max load
     *
     * @return
     */
    public int getPlayerMaxLoad() {
        return mMAX_LOAD;
    }

    /**
     * Gets the current Room of the Player
     *
     * @return
     */
    public Room getCurrentRoom() {
        return mCurrentRoom;
    }

    public Stack getPreviousRooms() {
        return mPREVIOUS_ROOMS;
    }

    /**
     * Sets the current room of the player
     *
     * @param currentRoom
     */
    public void setCurrentRoom(Room currentRoom) {
        mCurrentRoom = currentRoom;
    }

    /**
     * Try to go to one direction. If there is an exit, enter the new room,
     * otherwise print an error message.
     *
     * @param newDirection
     * @return
     */
    public String goRoom(String newDirection) {
        String moveString = "";
        String direction = newDirection;

        // Try to leave current room.
        Room nextRoom = getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            moveString += ("There is no door!\n");
        } else {
            moveString += "Opening next door\n";
            if (nextRoom.isLocked() && !mHaveKey) {
                moveString += "Darn we don't have the key!\n";
            } else if (nextRoom.isLocked() && mHaveKey) {
                moveString += "We use the secret key to open the door!\n";
                enterNextRoom(nextRoom);
            } else {
                enterNextRoom(nextRoom);
            }
        }
        return moveString;
    }

    /**
     * If room isn't locked enter the next room If the room is locked, but we
     * have the key we can enter!
     *
     * @param nextRoom
     */
    private void enterNextRoom(Room nextRoom) {
        getPreviousRooms().add(getCurrentRoom());
        setCurrentRoom(nextRoom);
    }

    /**
     * Player checks the Room for a Monster
     */
    private void checkForMonster() {
        if (mCurrentRoom.hasMonster()) {
            encounterMonster();
        }
    }

    /**
     * Teleport the soor looser back to the beginning!
     */
    public void teleportToBeginning() {
        mCurrentRoom = (Room) mPREVIOUS_ROOMS.firstElement();
    }

    /**
     * Player picks up the item if it is not too heavy
     *
     * @param itemName
     */
    public void takeItem(String itemName) {
        for (Item mItem : getCurrentRoom().getItems()) {
            if (mItem.getItemName().equals(itemName)) {
                addItemToInventory(itemName, mItem);
                checkForWeapon();
                break;
            }
        }
    }

    /**
     * Checks if room has th parsed item
     *
     * @param item
     * @return
     */
    public boolean checkForItemInRoom(String item) {
        boolean roomHasitem = false;
        for (Item roomItem : getCurrentRoom().getItems()) {
            if (roomItem.getItemName().equals(item)) {
                roomHasitem = true;
            }
        }
        return roomHasitem;
    }

    /**
     * Adds current item to the players inventory and reduces the total weigh
     * the player can hold
     *
     * @param itemName
     * @param mItem
     */
    private void addItemToInventory(String itemName, Item mItem) {
        OutputManager.outputString("Took item " + itemName);
        takenItems.add(mItem);
        mLoadLeft -= mItem.getItemWeight();
        getCurrentRoom().getItems().remove(mItem);
    }

    /**
     * Drops the item to the mCurrentRoom
     *
     * @param itemName
     */
    public void dropItem(String itemName) {
        for (Item mItem : takenItems) {
            if (mItem.getItemName().equals(itemName)) {
                takenItems.remove(mItem);
                mLoadLeft += mItem.getItemWeight();
                getCurrentRoom().getItems().add(mItem);
                break;
            }
        }
    }

    /**
     * Prints information about the players inventory status
     *
     * @return
     */
    public String inventoryStatus() {
        String status = "";
        if (takenItems.isEmpty()) {
            status += ("You don't currently hold any items");
        } else {
            status += ("You're currently holding " + getTakenItemsAsString());
        }
        status += ("\nOf your maximum capacity of " + mMAX_LOAD + " Kg" + " you have " + mLoadLeft + " Kg left!");
        return status;
    }

    /**
     * Sends the player back to the previous room
     */
    public void goBack() {
        setCurrentRoom((Room) getPreviousRooms().lastElement());
        getPreviousRooms().remove(getPreviousRooms().lastElement());
    }

    /**
     * Gets a String of the items the player currently holds
     */
    private String getTakenItemsAsString() {
        String allItems = "";
        for (Item item : takenItems) {
            allItems += item.getItemName() + " ";
        }
        return allItems;
    }

    /**
     * Updates the player to have the secret key!
     */
    public void setSecretKey() {
        mHaveKey = true;
    }

    /**
     * Fight the monster
     *
     * @return
     */
    public String encounterMonster() {
        String monsterFight = "";
        monsterFight += ("Aaaaaarh there is a monster in here!!!\n\n");
        monsterFight += checkForLastBossEncounter();
        if (mCurrentRoom.hasMonster()) {
            boolean monsterIsAlive = true;
            monsterFight += ("Combat vs " + mCurrentRoom.getMonster().get(0).getName() + " begins!\n");
            monsterFight += fightMonster(monsterIsAlive, monsterFight);
        }
        return monsterFight;
    }

    private String fightMonster(boolean monsterIsAlive, String monsterFight) {
        while (monsterIsAlive) {
            monsterFight += hitMonster() + "\n";
            monsterIsAlive = isMonsterStillAlive();
            if (monsterIsAlive) {
                monsterFight += (mCurrentRoom.getMonster().get(0).damagePlayer() + "\n");
                mHealth -= mCurrentRoom.getMonster().get(0).getDamage();
                if (mHealth <= 0) {
                    monsterFight += (mCurrentRoom.getMonster().get(0).getName() + " killed you...\n");
                    monsterFight += Game.gameOver();
                    break;
                } else {
                    monsterFight += ("You now only have " + mHealth + " health left!\n");
                }
            } else {
                monsterFight += ("You have slayed " + mCurrentRoom.getMonster().get(0).getName() + " congratulations!\n");
                monsterFight += checkBossKill() + "\n";
                mCurrentRoom.getMonster().remove(0);
            }
        }
        return monsterFight;
    }

    /**
     * Check if killed boss was last boss or mini boss
     */
    private String checkBossKill() {
        String bossKill = "";
        if (mCurrentRoom.getMonster().get(0).getName().equals(Game.getLAST_BOSS())) {
            bossKill += ("\nYou have now found the princess and she is so happy that you saved her, that she promises to marry you!");
            bossKill += Game.win();
        } else {
            mCurrentRoom.getItems().add(mCurrentRoom.getMonster().get(0).getLoot());
            bossKill += ("\nWhile hitting the floor " + mCurrentRoom.getMonster().get(0).getName() + " dropped "
                    + mCurrentRoom.getMonster().get(0).getLoot().getItemName() + "!" + "\nThe inscription on this weapon reads:\n"
                    + mCurrentRoom.getMonster().get(0).getLoot().getItemDescription());
        }
        return bossKill;
    }

    /**
     * Checks if we're facing the last boss
     */
    private String checkForLastBossEncounter() {
        String lastBossFight = "";
        if (mCurrentRoom.getMonster().get(0).getName().equals(Game.getLAST_BOSS())) {
            lastBossFight += ("You found the final boss " + Game.getLAST_BOSS() + "\n");
            boolean haveFinalItem = false;
            for (Item takenItem : takenItems) {
                if (takenItem.getItemName().equals(Game.getFINAL_WEAPON())) {
                    haveFinalItem = true;
                }
            }
            if (haveFinalItem) {
                lastBossFight += ("Good thing we picked up " + Game.getFINAL_WEAPON() + "!\n");
                lastBossFight += ("Now we can use it to kill " + Game.getLAST_BOSS() + "\n");
            } else {
                lastBossFight += ("Sorry you must have The Ancient Sword of Dracula to enter this fight!\n");
                lastBossFight += ("You will now be teleported away to safety... TELEPORTING\n");
                mCurrentRoom = (Room) mPREVIOUS_ROOMS.firstElement();
            }
        }
        return lastBossFight;
    }

    /**
     * Player attacks the monster
     *
     * @param monsterIsAlive
     */
    private String hitMonster() {
        String monsterHit = "";
        Random rand = new Random();
        int hitChance = rand.nextInt(3) + 1;
        switch (hitChance) {
            case 1:
                mDamage += LOW_HIT_DAMGE;
                break;
            case 2:
                mDamage += MEDIUM_HIT_DAMAGE;
                break;
            case 3:
                mDamage += CRITICAL_HIT_DAMAGE;
                break;
            default:
                break;
        }
        monsterHit += ("You strike " + mCurrentRoom.getMonster().get(0).getName() + " with a devastating hit for " + mDamage + " points!\n");
        mCurrentRoom.getMonster().get(0).takeDamage(mDamage);
        if (mCurrentRoom.getMonster().get(0).getHealth() > 0) {
            monsterHit += ("Monster now only has " + mCurrentRoom.getMonster().get(0).getHealth() + " health left!\n");
        } else {
            monsterHit += ("That did it!\n");
        }
        return monsterHit;
    }

    /**
     * Checks if the monster is alive
     */
    private boolean isMonsterStillAlive() {
        boolean isMonsterAlive = true;
        if (mCurrentRoom.getMonster().get(0).isMonsterDead()) {
            isMonsterAlive = false;
        }
        return isMonsterAlive;
    }

    /**
     * Checkif the player has a weapon
     */
    private boolean checkForWeapon() {
        boolean isWeapon = false;
        for (Item takenItem : takenItems) {
            if (takenItem instanceof Weapon) {
                isWeapon = true;
                if (takenItem.getItemName().equals(Game.getFINAL_WEAPON())) {
                    mHasFinalWeapon = true;
                }
                increasePlayerDamage(((Weapon) takenItem).getWeaponDamage());
            }
        }
        return isWeapon;
    }

    private void increasePlayerDamage(int weaponDamage) {
        mDamage += weaponDamage;
        OutputManager.outputString("Your total damage is now " + mDamage + "!");
    }

    /**
     * Updates information about the players possesion of the key
     *
     */
    public void playerHasKey() {
        mHaveKey = true;
    }

    /**
     * Returns true if the player has the key
     *
     * @return
     */
    public boolean checkIfPlayerHasKey() {
        return mHaveKey;
    }

    /**
     * Gets the players item list
     *
     * @return
     */
    public List<Item> getTakenItems() {
        return takenItems;
    }
}

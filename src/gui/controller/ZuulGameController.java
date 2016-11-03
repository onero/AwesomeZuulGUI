/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controller;

import gui.model.Game;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author Adamino
 */
public class ZuulGameController implements Initializable {

    private Game game;

    @FXML
    private TextArea txtRoom;
    @FXML
    private TextArea txtItems;
    @FXML
    private TextArea txtInfo;
    @FXML
    private TextField txtInput;
    @FXML
    private MenuButton txtCommand;
    @FXML
    private TextField txtAnswer;
    @FXML
    private TextField chosenCommand;
    @FXML
    private TextField txtExits;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Starts the game and updates the TextAreas
     *
     * @param event
     */
    @FXML
    private void startGame(ActionEvent event) {
        game = new Game();
        updateRoom();
        txtInfo.setText(game.getWelcomeString());
    }

    /**
     * Sets the label command GO
     */
    @FXML
    private void setCommandGo() {
        chosenCommand.setText("GO");
    }

    /**
     * Sets the label command BACK
     */
    @FXML
    private void setCommandBack() {
        chosenCommand.setText("BACK");
    }

    /**
     * Sets the label command TAKE
     */
    @FXML
    private void setCommandTake() {
        chosenCommand.setText("TAKE");
    }

    /**
     * Sets the label command DROP
     */
    @FXML
    private void setCommandDrop() {
        chosenCommand.setText("DROP");
    }

    /**
     * Sets the label command QUIT
     */
    @FXML
    private void setCommandQuit() {
        chosenCommand.setText("QUIT");
    }

    /**
     * Set the label command QUIT
     */
    @FXML
    private void setCommandHelp() {
        chosenCommand.setText("HELP");
    }

    /**
     * Retrieves the Go command from the GUI and then executes the command
     */
    @FXML
    private void getGoCommand() {
        carryOutCommand();
        txtInput.setText("");

    }

    /**
     * Check if enter is clikced on txtInput
     */
    @FXML
    private void getEnterCommand(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            carryOutCommand();
            txtInput.setText("");
        }
    }

    /**
     * Carry out command from chosenCommand and txtInput
     */
    private void carryOutCommand() {
        String command = chosenCommand.getText();
        String intention = txtInput.getText();
        switch (command) {
            case "GO":
                goRoom(intention);
                break;
            case "BACK":
                goBack();
                updateRoom();
                break;
            case "TAKE":
                takeItem(intention);
                updateRoom();
                updateInventory();
                break;
            case "DROP":
                dropItem(intention);
                updateRoom();
                updateInventory();
                break;
            case "QUIT":
                quit();
                break;
            case "HELP":
                help();
                break;
            default:
                break;
        }
    }

    /**
     * Give answer to the challenge
     */
    @FXML
    private void answerChallenge() {
        String answer = txtAnswer.getText();
        appendText(game.checkAnswer(answer));
        updateRoom();
    }

    /**
     * Updates the information about the room and the items in it in the View
     */
    private void updateRoom() {
        txtRoom.setText(game.getPlayerRooomAsString());
        txtExits.setText(game.getExitsAsString());
        txtItems.setText(game.getItemsInRoomAsString());
    }

    /**
     * Moves the player to the parsed Room
     */
    private void goRoom(String direction) {
        appendText(game.moveToNextRoomString(direction));
        if (game.roomHasChallenge()) {
            appendText(game.getChallengeAsString());
        } else {
            if (game.roomHasMonster()) {
                appendText(game.fightMonsterString());
            } else {
                updateRoom();
            }
        }

    }

    /**
     * Appends the parsed text to txtInfo
     *
     * @param direction
     */
    private void appendText(String text) {
        txtInfo.appendText("\n" + text);
    }

    /**
     * Moves the player back to the previous Room
     */
    private void goBack() {
        if (!game.isAtBeginning()) {
            game.movePlayerBack();
        } else {
            appendText("You're at the beginning!");
        }
    }

    /**
     * The Player picks up the parsed item
     *
     * @param intention
     */
    private void takeItem(String intention) {
        appendText(game.takeItemString(intention));
    }

    /**
     * Updates the players inventory in the view
     */
    private void updateInventory() {
        appendText(game.getInventoryAsString());
    }

    /**
     * Player drops the parsed item
     *
     * @param intention
     */
    private void dropItem(String intention) {
        if (game.checkForPlayerItem(intention)) {
            game.playerDropItem(intention);
        } else {
            appendText("Sorry that item doesn't exist...");
        }
    }

    /**
     * Player quits the game
     */
    private void quit() {
        appendText(game.quit());
    }

    /**
     * Adds help text
     */
    private void help() {
        appendText(game.printHelp());
    }

}

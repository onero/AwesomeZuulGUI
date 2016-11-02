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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Adamino
 */
public class ZuulGameController implements Initializable {

    private Game game;

    @FXML
    private TextArea txtOutput;
    @FXML
    private TextArea txtRoom;
    @FXML
    private TextArea txtItems;
    @FXML
    private TextArea txtInfo;
    @FXML
    private TextArea txtInput;
    @FXML
    private TextArea txtCommand;
    @FXML
    private TextField txtAnswer;

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
        txtInfo.setText(game.getWelcome());
    }

    /**
     * Retrieves the Go command from the GUI and then executes the command
     */
    @FXML
    private void getGoCommand() {

        //TODO ALH: Go through all commands, fix challenge, add boss, add weapon!
        String command = txtCommand.getText();
        String intention = txtInput.getText();
        switch (command) {
            case "go":
                goRoom(intention);
                break;
            case "back":
                goBack();
                updateRoom();
                break;
            case "take":
                takeItem(intention);
                updateRoom();
                updateInventory();
                break;
            case "drop":
                dropItem(intention);
                updateRoom();
                updateInventory();
                break;
            case "quit":
                quit();
                break;
            default:
                break;
        }
        txtInput.setText("");

    }

    /**
     * Give answer to the challenge
     */
    @FXML
    private void answerChallenge() {
        String answer = txtAnswer.getText();
        appendText(game.checkAnswer(answer));
        updateRoom();
        checkForMonster();
    }

    /**
     * Check if there is a monster
     */
    private void checkForMonster() {
        if (game.roomHasMonster()) {
            appendText(game.fightMonster());
            updateRoom();
        }
    }

    /**
     * Updates the information about the room and the items in it in the View
     */
    private void updateRoom() {
        txtRoom.setText(game.getPlayerRooom());
        txtItems.setText(game.getItemsInRoom());
    }

    /**
     * Moves the player to the parsed Room
     */
    private void goRoom(String direction) {
        appendText(game.moveToNextRoom(direction));
        if (game.roomHasChallenge()) {
            appendText(game.getChallenge());
        } else {
            updateRoom();
            checkForMonster();
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
            game.goBack();
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
        appendText(game.getInventory());
    }

    /**
     * Player drops the parsed item
     *
     * @param intention
     */
    private void dropItem(String intention) {
        if (game.checkForPlayerItem(intention)) {
            game.dropItem(intention);
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

}

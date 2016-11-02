package bll;

/*
 * This class is the main class of the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a two word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 *
 */
/**
 *
 * @author Adamino
 */
public class Parser {

    private final CommandWords commands;  // holds all valid command words

    /**
     * Create a parser to read from the terminal window.
     *
     */
    public Parser() {
        commands = new CommandWords();
    }

    /**
     * @param commandWord
     * @return The next command from the user.
     */
    public Command getCommand(String commandWord) {
        return new Command(commands.getCommandWord(commandWord));
    }

    /**
     * Print out a list of valid command words.
     *
     * @return
     */
    public String showCommands() {
        return commands.showAll();
    }
}

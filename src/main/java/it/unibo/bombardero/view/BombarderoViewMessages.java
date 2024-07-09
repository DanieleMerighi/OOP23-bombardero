package it.unibo.bombardero.view;

/**
 * This enumerator represents the various types of messages that can be displayed in the view.
 */
public enum BombarderoViewMessages {

    /**
     * The message to explain how to move to a new player.
     */
    EXPLAIN_MOVEMENT("To move, press the W, A, S and D"),

    /**
     * The message to explain how to place a bomb to a new user.
     */
    PLACE_BOMB("<html>Everyone starts with one bomb, <br>press the space bar to place it near the crate<br>and brake it</html>"),

    /** 
     * The message to explain the basis of powerups to a new user. 
     */
    EXPLAIN_POWERUP("<html>Under the crates you can find powerups<br>to enhance your abilities</html>"),

    /**
     * The message to explain how bombs work to a new user.
     */
    KILL_ENEMY("<html>Bombs have a certain range,<br>place a bomb near an enemy to kill him<html>"),

    /**
     * The message to end the guide and prompt the user into choosing what to do next.
     */
    END_GUIDE("<html>Good!<br>The guide has endend,<br>you can go back to the menu or start a new game!</html>");

    private String message;

    BombarderoViewMessages(final String message) {
        this.message = message;
    }

    /**
     * Returns the string message to be displayed
     * related to this type of message.
     * @return the String that has to be displayed in the View
     */
    public String getMessage() {
        return this.message;
    }
}

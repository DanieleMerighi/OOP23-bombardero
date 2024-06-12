package it.unibo.bombardero.view;

public enum BombarderoViewMessages {

    EXPLAIN_MOVEMENT("To move, press the W, A, S and D"),
    PLACE_BOMB("Everyone starts with one bomb, press the space bar to place it"),
    EXPLAIN_POWERUP("Under the crates you can find powerups to enhance your abilities"),
    KILL_ENEMY("Bombs have a certain range, place a bomb near an enemy to kill him"),
    END_GUIDE("Good! The guide has endend, you can go back to the menu or start a new game!");

    private String message;

    private BombarderoViewMessages(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}

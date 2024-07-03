package it.unibo.bombardero.view;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFrame;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.GenPair;

public interface GraphicsEngine {

    enum viewCards {
        GAME("game"),
        MENU("menu"),
        GUIDE("guide"),
        END("end");

        private String id;

        private viewCards(final String cardName) {
            id = cardName;
        }

        public String getStringId() {
            return this.id;
        }
    }

    enum endGameState {
        WIN,
        LOSE;
    }

    void showCard(viewCards cardName);

    void update(Map<GenPair<Integer, Integer>, Cell> map, List<Character> playerList, List<Character> enemiesList, final Optional<Long> timeLeft);

    void setPausedView();

    void setUnpausedView();

    void setMessage(BombarderoViewMessages message);

    ResizingEngine getResizingEngine();

    ResourceGetter getResourceGetter();

    JFrame getParentFrame();

}
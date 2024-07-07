package it.unibo.bombardero.view.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.JFrame;

import it.unibo.bombardero.cell.Cell;
import it.unibo.bombardero.character.Character;
import it.unibo.bombardero.map.api.GenPair;
import it.unibo.bombardero.view.BombarderoViewMessages;
import it.unibo.bombardero.view.ResizingEngine;
import it.unibo.bombardero.view.ResourceGetter;

public interface GraphicsEngine {

    enum ViewCards {
        GAME("game"),
        MENU("menu"),
        GUIDE("guide"),
        END("end");

        private String id;

        ViewCards(final String cardName) {
            id = cardName;
        }

        public String getStringId() {
            return this.id;
        }
    }

    enum EndGameState {
        WIN,
        LOSE;
    }

    void showGameScreen(ViewCards cardName);

    void update(Map<GenPair<Integer, Integer>, Cell> map, List<Character> playerList, List<Character> enemiesList, Optional<Long> timeLeft);

    void setPausedView();

    void setUnpausedView();

    void setMessage(BombarderoViewMessages message);

    void showEndScreen(EndGameState gameState);

    ResizingEngine getResizingEngine();

    ResourceGetter getResourceGetter();

    JFrame getParentFrame();

}
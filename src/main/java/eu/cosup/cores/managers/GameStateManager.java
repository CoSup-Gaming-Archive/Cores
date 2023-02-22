package eu.cosup.cores.managers;

import eu.cosup.cores.events.ChangeGamePhaseEvent;
import eu.cosup.cores.events.ChangeGameStateEvent;

public class GameStateManager {

    private GameState gameState;
    private GamePhase gamePhase;

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState newGameState) {
        new ChangeGameStateEvent(newGameState, this.gameState);
        this.gameState = newGameState;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase newGamePhase) {
        new ChangeGamePhaseEvent(newGamePhase, this.gamePhase);
        this.gamePhase = newGamePhase;
    }

    public enum GameState {
        LOADING,
        JOINING,
        STARTING,
        ACTIVE,
        ENDING
    }

    public enum GamePhase {

        ARMOR_UPGRADE,
        SWORD_UPGRADE,
        BEACON_DESTRUCTION,
        ARENA
    }
}
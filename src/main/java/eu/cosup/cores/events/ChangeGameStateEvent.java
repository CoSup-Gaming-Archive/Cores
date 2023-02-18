package eu.cosup.cores.events;

import eu.cosup.cores.interfaces.GameListener;
import eu.cosup.cores.managers.GameStateManager;

import java.util.ArrayList;
import java.util.List;

public record ChangeGameStateEvent(GameStateManager.GameState newGameState, GameStateManager.GameState oldGameState) {

    public ChangeGameStateEvent(GameStateManager.GameState newGameState, GameStateManager.GameState oldGameState) {
        this.newGameState = newGameState;
        this.oldGameState = oldGameState;
        changeGameState();
    }

    private static List<GameListener> listeners = new ArrayList<>();

    public static void addListener(GameListener listener) {
        listeners.add(listener);
    }

    private void changeGameState() {
        for (GameListener listener : listeners)
            listener.firedChangeGameStateEvent(this);
    }
}

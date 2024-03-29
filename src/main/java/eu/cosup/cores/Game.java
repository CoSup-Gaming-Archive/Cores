package eu.cosup.cores;

import eu.cosup.cores.core.data.LoadedMap;
import eu.cosup.cores.core.data.TeamColor;
import eu.cosup.cores.managers.BlockManager;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.TeamManager;
import eu.cosup.cores.tasks.ActivateGameTask;
import eu.cosup.cores.tasks.GameEndTask;
import eu.cosup.cores.tasks.GameTimerTask;
import org.jetbrains.annotations.Nullable;

public class Game {

    private static Game gameInstance;
    private final GameStateManager gameStateManager;
    private final TeamManager teamManager;
    private final LoadedMap selectedMap;
    private final BlockManager blockManager;

    public Game(LoadedMap selectedMap) {
        gameInstance = this;
        this.selectedMap = selectedMap;

        gameStateManager = new GameStateManager();
        teamManager = new TeamManager();
        blockManager = new BlockManager();

        initGame();
    }

    public static Game getGameInstance() {
        return gameInstance;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }


    public LoadedMap getSelectedMap() {
        return selectedMap;
    }

    // loading and joining phase
    private void initGame() {

        // this really just a useless state
        gameStateManager.setGameState(GameStateManager.GameState.LOADING);

        gameStateManager.setGameState(GameStateManager.GameState.JOINING);
    }

    // active phase
    public void activateGame() {

        new ActivateGameTask().runTask(Cores.getInstance());
        GameTimerTask.resetTimer();
        new GameTimerTask().runTask(Cores.getInstance());

    }

    public void finishGame(@Nullable TeamColor winner) {
        new GameEndTask(winner).runTask(Cores.getInstance());
    }
}

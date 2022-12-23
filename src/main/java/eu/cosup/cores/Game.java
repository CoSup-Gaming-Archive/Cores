package eu.cosup.cores;

import eu.cosup.cores.data.LoadedMap;
import eu.cosup.cores.managers.*;
import eu.cosup.cores.tasks.ActivateGameTask;
import eu.cosup.cores.tasks.GameEndTask;
import eu.cosup.cores.tasks.StartCountdownTask;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class Game {

    private static Game gameInstance;
    private ArrayList<Player> joinedPlayers = new ArrayList<>();
    private GameStateManager gameStateManager;
    private TeamManager teamManager;
    private LoadedMap selectedMap;
    private BukkitTask startTask;

    public Game(LoadedMap selectedMap) {
        gameInstance = this;

        gameStateManager = new GameStateManager();
        teamManager = new TeamManager();

        this.selectedMap = selectedMap;

        refreshPlayerCount();

        // TODO choose and load map

        initGame();
    }

    public static Game getGameInstance() {
        return gameInstance;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public int getPlayerCount() {
        return joinedPlayers.size();
    }

    public LoadedMap getSelectedMap() {
        return selectedMap;
    }

    // loading and joining phase
    private void initGame() {
        gameStateManager.setGameState(GameStateManager.GameState.LOADING);

        // TODO load maps here
        // then switch to joining

        gameStateManager.setGameState(GameStateManager.GameState.JOINING);
    }

    // active phase
    public void activateGame() {

        new ActivateGameTask(joinedPlayers).run();

    }

    public void finishGame(TeamColor winner) {

        new GameEndTask(winner).run();

    }

    public ArrayList<Player> getJoinedPlayers() {
        return joinedPlayers;
    }

    // to check how many players are on the cores game
    public void refreshPlayerCount() {

        joinedPlayers = new ArrayList<>(Cores.getInstance().getServer().getOnlinePlayers());

        // TODO fix this
        if (joinedPlayers.size() < Cores.getInstance().getConfig().getInt("required-player-count")) {
            Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.JOINING);
            if (startTask != null) {
                startTask.cancel();
            }
            Cores.getInstance().getServer().broadcastMessage("Not enough players");
            return;
        }

        // if the game already started
        if (gameStateManager.getGameState() != GameStateManager.GameState.JOINING) {
            return;
        }

        startTask = new StartCountdownTask().runTask(Cores.getInstance());
    }
}

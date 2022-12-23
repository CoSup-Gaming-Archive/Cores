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

    public Game(LoadedMap selectedMap) {
        gameInstance = this;

        gameStateManager = new GameStateManager();
        teamManager = new TeamManager();

        this.selectedMap = selectedMap;

        refreshPlayerCount();

        // TODO add load map chunks with probably async task

        joinedPlayers = new ArrayList<>(Cores.getInstance().getServer().getOnlinePlayers());

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

        new ActivateGameTask(joinedPlayers).runTask(Cores.getInstance());

    }

    public void finishGame(TeamColor winner) {

        new GameEndTask(winner).runTask(Cores.getInstance());

    }

    public ArrayList<Player> getJoinedPlayers() {
        return joinedPlayers;
    }

    // to check how many players are on the cores game
    public void refreshPlayerCount() {

        Bukkit.getLogger().info(""+joinedPlayers);

        // if the game already started
        if (gameStateManager.getGameState() != GameStateManager.GameState.JOINING && gameStateManager.getGameState() != GameStateManager.GameState.STARTING) {
            return;
        }

        // TODO fix this
        if (joinedPlayers.size() < Cores.getInstance().getConfig().getInt("required-player-count")) {
            // this means there is already a countdown going
            if (Game.gameInstance.gameStateManager.getGameState() == GameStateManager.GameState.STARTING) {

                Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.JOINING);

                Cores.getInstance().getServer().broadcastMessage(ChatColor.YELLOW+"Stopping!");
            }
            // omg teach me proper formatting cuz god dayum this one is ugly
            Cores.getInstance().getServer().broadcastMessage(ChatColor.RED+"Not enough players: ("+joinedPlayers.size()+"/"+Cores.getInstance().getConfig().getInt("required-player-count")+")");
            return;
        }

        // just saving so we can cancel it later
        new StartCountdownTask().runTask(Cores.getInstance());
    }
}

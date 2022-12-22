package eu.cosup.cores;

import eu.cosup.cores.data.LoadedMap;
import eu.cosup.cores.managers.*;
import eu.cosup.cores.tasks.StartCountdownTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class Game {

    private static Game gameInstance;
    private ArrayList<Player> joinedPlayers = new ArrayList<>();
    private GameStateManager gameStateManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;

    private LoadedMap selectedMap;
    private BukkitTask startTask;

    public Game(LoadedMap selectedMap) {
        gameInstance = this;

        gameStateManager = new GameStateManager();
        teamManager = new TeamManager();
        playerManager = new PlayerManager();

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

        // do all the loading for maps and stuff

        gameStateManager.setGameState(GameStateManager.GameState.JOINING);
    }

    // active phase
    public void activateGame() {

        // get the spawn locations and teleport players to them
        Cores.getInstance().getServer().broadcastMessage(ChatColor.YELLOW+"STARTING");

        teleportPlayersToSpawns();

    }

    private void teleportPlayersToSpawns() {
        for (Player player : joinedPlayers) {

            TeamColor teamColor = Game.getGameInstance().teamManager.whichTeam(player);

            if (teamColor == TeamColor.RED) {
                player.teleport(Game.getGameInstance().getSelectedMap().getTeamRedSpawns());
            }

            if (teamColor == TeamColor.BLUE) {
                player.teleport(Game.getGameInstance().getSelectedMap().getTeamBlueSpawns());
            }

            player.setGameMode(GameMode.SURVIVAL);
        }
    }

    public ArrayList<Player> getJoinedPlayers() {
        return joinedPlayers;
    }

    // to check how many players are on the cores game
    public void refreshPlayerCount() {

        // remove all the spectators from joined
        joinedPlayers = new ArrayList<>(Cores.getInstance().getServer().getOnlinePlayers());

        if (joinedPlayers.size() < Cores.getInstance().getConfig().getInt("required-player-count")) {
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

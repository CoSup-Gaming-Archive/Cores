package eu.cosup.cores;

import eu.cosup.cores.managers.*;
import eu.cosup.cores.tasks.StartCountdownTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Game {

    private int playerCount = 0;
    private GameStateManager gameStateManager;
    private TeamManager teamManager;
    private PlayerManager playerManager;

    public Game() {
        gameStateManager = new GameStateManager();
        teamManager = new TeamManager();
        playerManager = new PlayerManager();
        refreshPlayerCount();

        // TODO choose and load map

        initGame();
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    private void initGame() {
        gameStateManager.setGameState(GameStateManager.GameState.LOADING);

        // do all the loading for maps and stuff

        gameStateManager.setGameState(GameStateManager.GameState.JOINING);
    }

    private void startGame() {
        // asign teams for players

        ArrayList<Player> joinedPlayers = new ArrayList<>(Cores.getInstance().getServer().getOnlinePlayers());
        teamManager.makeTeams(joinedPlayers);

        // TODO remove debug line bellow
        for (Team team : teamManager.getTeams()) {
            for (Player player : team.getPlayers()) {
                player.sendMessage(TeamColor.getChatColor(team.getColor())+"You are on ");
            }
        }

        new StartCountdownTask(this).runTask(Cores.getInstance());

    }

    private void teleportPlayers() {

    }


    public void refreshPlayerCount() {
        playerCount = Cores.getInstance().getServer().getOnlinePlayers().size();

        if (playerCount < Cores.getInstance().getConfig().getInt("required-player-count")) {
            return;
        }
        // if the game already started
        if (gameStateManager.getGameState() != GameStateManager.GameState.JOINING) {
            return;
        }

        startGame();
    }
}

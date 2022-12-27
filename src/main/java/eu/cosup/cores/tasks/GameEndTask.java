package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.Team;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class GameEndTask extends BukkitRunnable {

    private TeamColor winner;

    public GameEndTask(TeamColor winner) {

        this.winner = winner;

    }

    @Override
    public void run() {

        Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.ENDING);
        Team winnerTeam = Game.getGameInstance().getTeamManager().getTeamByColor(winner);

        for (Player player : winnerTeam.getPlayers()) {

            Location playerLocation = player.getLocation();

            for (int i = 0; i < 10; i++) {
                Cores.getInstance().getWorld().spawnEntity(playerLocation, EntityType.FIREWORK);
            }
        }

        for (Player player : Game.getGameInstance().getJoinedPlayers()) {

            if (!winnerTeam.isPlayerInTeam(player)) {
                player.setGameMode(GameMode.SPECTATOR);
            }

        }

        Cores.getInstance().getServer().broadcastMessage(TeamColor.getChatColor(winner) + "" + winner + " is the winner team congratulations!");

        Bukkit.getLogger().warning("New game in: " + Cores.getInstance().getConfig().getInt("return-to-lobby-delay"));
        new BukkitRunnable() {
            @Override
            public void run() {

                // you should return players to lobby before restarting
                Bukkit.getLogger().severe("Restarting game");

                // the only solution at the moment later wil fix
                Cores.getInstance().getServer().shutdown();

                // TODO test this
                // create the new game
                Cores.getInstance().createGame();

                // basicaly kill all players

                Cores.getInstance().getGame().refreshPlayerCount();

            }
        }.runTaskLater(Cores.getInstance(), Cores.getInstance().getConfig().getInt("return-to-lobby-delay") * 20L);

    }

}

package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.Team;
import eu.cosup.cores.managers.TeamColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameEndTask extends BukkitRunnable {

    private final TeamColor winner;

    public GameEndTask(TeamColor winner) {

        this.winner = winner;

    }

    @Override
    public void run() {

        Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.ENDING);
        Team winnerTeam = Game.getGameInstance().getTeamManager().getTeamByColor(winner);

        GameTimerTask.getInstance().cancelTimer();

        for (Player player : winnerTeam.getPlayers()) {

            Location playerLocation = player.getLocation();

            for (int i = 0; i < 10; i++) {
                Cores.getInstance().getGameWorld().spawnEntity(playerLocation, EntityType.FIREWORK);
            }
        }

        for (Player player : Game.getGameInstance().getJoinedPlayers()) {

            if (!winnerTeam.isPlayerInTeam(player)) {
                player.setGameMode(GameMode.SPECTATOR);
            }

        }

        Component msg = Component.text().content(winner.toString()).color(TeamColor.getNamedTextColor(winner))
                .append(Component.text().content(" is the winner!").color(NamedTextColor.YELLOW)).build();
        Cores.getInstance().getServer().broadcast(msg);

        Bukkit.getLogger().warning("New game in: " + Cores.getInstance().getConfig().getInt("return-to-lobby-delay"));
        new BukkitRunnable() {
            @Override
            public void run() {

                // you should return players to lobby before restarting
                Bukkit.getLogger().severe("Restarting game");

                for (Player player : Cores.getInstance().getServer().getOnlinePlayers()) {
                    player.teleport(Cores.getInstance().getLobbyWorld().getSpawnLocation());
                }

                // create new game instance
                Cores.getInstance().createGame();

                Cores.getInstance().getGame().refreshPlayerCount();

            }
        }.runTaskLater(Cores.getInstance(), Cores.getInstance().getConfig().getInt("return-to-lobby-delay") * 20L);

    }

}

package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.TeamColor;
import io.papermc.paper.entity.RelativeTeleportFlag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SpectatorTask extends BukkitRunnable {

    private Player player;

    public SpectatorTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {

        player.sendMessage(ChatColor.RED+"You died");

        TeamColor team = Game.getGameInstance().getTeamManager().whichTeam(player);

        player.setGameMode(GameMode.SPECTATOR);

        player.setVelocity(new Vector().zero());

        // yay
        player.teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());

        new BukkitRunnable() {
            @Override
            public void run() {

                // TODO make sure this works later because can be a bug players experience
                if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ENDING) {
                    cancel();
                    return;
                }

                if (team == TeamColor.RED) {
                    player.teleport(Game.getGameInstance().getSelectedMap().getTeamRedSpawns());
                }

                if (team == TeamColor.BLUE) {
                    player.teleport(Game.getGameInstance().getSelectedMap().getTeamBlueSpawns());
                }

                player.setGameMode(GameMode.SURVIVAL);

                player.sendMessage(TeamColor.getChatColor(team)+"You are alive");
            }

        }.runTaskLater(Cores.getInstance(), Cores.getInstance().getConfig().getInt("respawn-delay")*20L);

    }

}

package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SpectatorTask extends BukkitRunnable {

    private Player player;

    public SpectatorTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {

        player.sendMessage(ChatColor.RED+"You died");

        TeamColor team = Game.getGameInstance().getTeamManager().whichTeam(player);

        // TODO fix this bs
        // WHY TF THIS NOT WORK UHHHHHHHHHHH
        Bukkit.getLogger().info(""+player.teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn().toBlockLocation()));

        player.setGameMode(GameMode.SPECTATOR);

        new BukkitRunnable() {
            @Override
            public void run() {

                // TODO make sure this works later because can be a bug players experience
                if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ENDING) {
                    cancel();
                    return;
                }


                // TODO remove debug lines (player.sendmessage)
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

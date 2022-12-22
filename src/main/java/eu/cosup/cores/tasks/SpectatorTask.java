package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import org.bukkit.Bukkit;
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

        player.setGameMode(GameMode.SPECTATOR);
        Bukkit.getLogger().info(""+player.getGameMode());

        // TODO teleport to spectator position
        //player.teleport();

        new BukkitRunnable() {
            @Override
            public void run() {
                // TODO teleport player to its spawn
                //player.teleport();

                player.setGliding(true);

                player.sendMessage("You are alive");
            }

        }.runTaskLater(Cores.getInstance(), 50L);

    }

}

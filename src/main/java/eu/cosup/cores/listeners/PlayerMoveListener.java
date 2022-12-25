package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {

        double playerY = event.getTo().getY();

        // if player is bellow the threshold
        if (Game.getGameInstance().getSelectedMap().getDeathHeight() > playerY) {
            // he die

            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.getPlayer().setHealth(0);
            }
        }
    }
}

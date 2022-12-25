package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    @EventHandler
    private void onPlayerInteractWithBeacon(PlayerInteractEvent event) {

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block.getType() == Material.BEACON) {

            if (event.getAction().isLeftClick()) {
                return;
            }

            event.setCancelled(true);
        }
    }
}

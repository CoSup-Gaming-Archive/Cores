package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import eu.cosup.cores.utility.BlockUtility;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerInteractListener implements Listener {

    @EventHandler
    private void onPlayerInteractWithBeacon(PlayerInteractEvent event) {

        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            return;
        }

        event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);


        if (event.getClickedBlock() == null) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block.getType() == Material.BEACON) {
            if (event.getAction().isLeftClick()) {

                // its not one of the beacons from game
                if (!BlockUtility.isLocationProtected(event.getClickedBlock().getLocation())) {
                    return;
                }

                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 1, false, false, false));
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 2, false, false, false));

                return;
            }

            event.setCancelled(true);
        }
    }
}

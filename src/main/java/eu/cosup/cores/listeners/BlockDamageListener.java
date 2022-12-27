package eu.cosup.cores.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;

public class BlockDamageListener implements Listener {

    @EventHandler
    private void onBlockDamage(BlockDamageEvent event) {

        // TODO implement this properly

        // if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
        //     event.setCancelled(true);
        // }

    }

}

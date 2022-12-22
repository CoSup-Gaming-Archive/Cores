package eu.cosup.cores.listeners;

import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) throws NoSuchFieldException {


        // for slowing down of breaking beacon
        // TODO implement this properly
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.setCancelled(true);
        }

    }

}

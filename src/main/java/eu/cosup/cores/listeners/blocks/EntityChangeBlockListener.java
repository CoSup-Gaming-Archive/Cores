package eu.cosup.cores.listeners.blocks;

import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

@SuppressWarnings("unused")
public class EntityChangeBlockListener implements Listener {

    @EventHandler
    public void onBlockFall(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            FallingBlock block = (FallingBlock) event.getEntity();
            block.setGravity(false);
            event.setCancelled(true);
        }
    }

}

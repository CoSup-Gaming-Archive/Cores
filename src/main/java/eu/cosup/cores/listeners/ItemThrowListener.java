package eu.cosup.cores.listeners;

import eu.cosup.cores.tasks.ActivateGameTask;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemThrowListener implements Listener {

    // when player tries to get rid of default items we should stop him

    @EventHandler
    private void onPlayerThrow(PlayerDropItemEvent event) {

        Material thrownMaterialName = event.getItemDrop().getItemStack().getType();

        if (ActivateGameTask.isItemDefault(thrownMaterialName)) {
            event.setCancelled(true);
            return;
        }

        // do we want despawning in the tournament?
        //event.getItemDrop().setUnlimitedLifetime(true);

    }
}

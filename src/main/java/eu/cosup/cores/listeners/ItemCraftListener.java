package eu.cosup.cores.listeners;

import eu.cosup.cores.utility.BlockUtility;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;


public class ItemCraftListener implements Listener {

    @EventHandler
    private void onItemCraft(CraftItemEvent event) {

        if (!BlockUtility.blockWhitelisted(event.getRecipe().getResult().getType())) {
            event.setCancelled(true);
        }
    }
}

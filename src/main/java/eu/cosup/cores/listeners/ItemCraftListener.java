package eu.cosup.cores.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;


public class ItemCraftListener implements Listener {

    @EventHandler
    private void onItemCraft(CraftItemEvent event) {

        if (!BlockBreakListener.blockWhitelisted(event.getRecipe().getResult().getType())) {
            event.setCancelled(true);
        }
    }
}

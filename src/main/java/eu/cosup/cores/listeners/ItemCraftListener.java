package eu.cosup.cores.listeners;

import eu.cosup.cores.utility.BlockUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

public class ItemCraftListener implements Listener {

    @EventHandler
    private void onPlayerCraft(CraftItemEvent event) {
        if (BlockUtility.isItemBlackListCraft(event.getRecipe().getResult().getType())) {
            event.setCancelled(true);
            event.getView().getPlayer().sendMessage(Component.text("You can't craft this item!").color(NamedTextColor.RED));
        }
    }
}

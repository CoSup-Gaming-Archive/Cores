package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import java.util.ArrayList;

public class ItemCraftListener implements Listener {

    private static ArrayList<String> whitelistCraft = new ArrayList<>(Cores.getInstance().getConfig().getStringList("whitelist-craft"));

    @EventHandler
    private void onItemCraft(CraftItemEvent event) {

        if (!isItemWhitelist(event.getRecipe().getResult().getType())) {
            event.setCancelled(true);
        }
    }

    public static boolean isItemWhitelist(Material material) {

        for (String materialString : whitelistCraft) {

            if (Material.getMaterial(materialString.toUpperCase()) == material) {
                return true;
            }
        }
        return false;
    }
}

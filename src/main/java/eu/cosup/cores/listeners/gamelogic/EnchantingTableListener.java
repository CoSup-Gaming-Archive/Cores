package eu.cosup.cores.listeners.gamelogic;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class EnchantingTableListener implements Listener {

    @EventHandler
    private void onPlayerOpenEnchantingTable(InventoryOpenEvent event) {

        if (event.getInventory().getType() != InventoryType.ENCHANTING) {
            return;
        }

        EnchantingInventory enchantingInventory = (EnchantingInventory) event.getInventory();
        ItemStack lapis = new ItemStack(Material.LAPIS_LAZULI, 64);
        enchantingInventory.setSecondary(lapis);
    }

    @EventHandler
    private void removeSweepingEdge(EnchantItemEvent event) {
        event.getEnchantsToAdd().remove(Enchantment.SWEEPING_EDGE);
    }

    @EventHandler
    private void onPlayerCloseEnchantingTale(InventoryClickEvent event) {

        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().getType() == Material.LAPIS_LAZULI) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    private void onPlayerCloseInventory(InventoryCloseEvent event) {
        event.getPlayer().getInventory().remove(Material.LAPIS_LAZULI);
        event.getInventory().remove(Material.LAPIS_LAZULI);
    }
}
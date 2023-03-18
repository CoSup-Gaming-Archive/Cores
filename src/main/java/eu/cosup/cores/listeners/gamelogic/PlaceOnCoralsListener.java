package eu.cosup.cores.listeners.gamelogic;

import eu.cosup.cores.Game;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@SuppressWarnings("unused")
public class PlaceOnCoralsListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    private void onInteractWithCoral(PlayerInteractEvent event) {

        if (event.getAction().isLeftClick()) {
            return;
        }

        if (event.getClickedBlock() == null) {
            return;
        }

        if (event.getClickedBlock().getType() == Material.BEACON) {
            event.setCancelled(true);
        }

        if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR || !event.getPlayer().getInventory().getItemInMainHand().getType().isBlock()) {
            return;
        }

        if (event.getClickedBlock().getType().toString().contains("CORAL") && !event.getClickedBlock().getType().toString().contains("BLOCK")) {
            event.setCancelled(true);
            Game.getGameInstance().getBlockManager().addBlock(event.getClickedBlock());
            event.getClickedBlock().setType(event.getPlayer().getInventory().getItemInMainHand().getType());
            event.getPlayer().getInventory().remove(new ItemStack(event.getPlayer().getInventory().getItemInMainHand().getType(), 1));
        }

        List<Material> flowers = List.of(
                Material.PINK_TULIP,
                Material.ALLIUM,
                Material.RED_TULIP,
                Material.WHITE_TULIP
        );

        if (flowers.contains(event.getClickedBlock().getType())) {
            event.setCancelled(true);
            Game.getGameInstance().getBlockManager().addBlock(event.getClickedBlock());
            event.getClickedBlock().setType(event.getPlayer().getInventory().getItemInMainHand().getType());
            event.getPlayer().getInventory().remove(new ItemStack(event.getPlayer().getInventory().getItemInMainHand().getType(), 1));
        }
    }
}

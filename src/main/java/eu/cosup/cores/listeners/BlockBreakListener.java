package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.tasks.TeamLoseBeaconTask;
import eu.cosup.cores.utility.BlockUtility;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BlockBreakListener implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    private void onBlockBreak(@NotNull BlockBreakEvent event) {

        Block block = event.getBlock();

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
                return;
            }
        }

        if (Game.getGameInstance().getBlockManager().isBlockPlaced(event.getBlock())) {
            return;
        }

        if (block.getType().equals(Material.BEACON)) {
            event.setCancelled(true);
            new TeamLoseBeaconTask(event.getBlock().getLocation(), event.getPlayer());
            return;
        }

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (BlockUtility.isLocationProtected(block.getLocation())) {
                event.setCancelled(true);
                return;
            }
        }

        if (!BlockUtility.shouldDropItem(event.getBlock())) {
            event.setDropItems(false);
        }
    }


    @EventHandler
    private void onBlockDestroy(BlockBreakBlockEvent event) {
        if (!event.getBlock().isSolid() && !event.getBlock().getType().equals(Material.LADDER)) {
            event.getDrops().clear();
        }
    }

    @EventHandler
    private void onBlockInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            if (event.getClickedBlock().getType().equals(Material.LAVA_CAULDRON)
                    || event.getClickedBlock().getType().equals(Material.WATER_CAULDRON)
                    || event.getClickedBlock().getType().equals(Material.CAULDRON)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    private void onBlockFall(EntityChangeBlockEvent event) {
        if (event.getBlock().getType().toString().contains("POWDER"))
            event.setCancelled(true);
    }
}

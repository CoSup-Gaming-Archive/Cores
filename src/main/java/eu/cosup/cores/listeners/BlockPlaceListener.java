package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.utility.BlockUtility;
import eu.cosup.cores.utility.ColorUtility;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class BlockPlaceListener implements Listener {

    @EventHandler
    private void onPlayerPlaceBlock(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        Block block = event.getBlock();

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            event.setCancelled(true);
            return;
        }

        Component msg = Component.text().content("You cannot place blocks here").color(ColorUtility.getStdTextColor("red")).build();

        if (Game.getGameInstance().getSelectedMap().getMaxHeight() < block.getY()) {
            player.sendMessage(msg);
            event.setCancelled(true);
            return;
        }

        if (Game.getGameInstance().getSelectedMap().getMinHeight() > block.getY()) {
            player.sendMessage(msg);
            event.setCancelled(true);
            return;
        }

        if (!BlockUtility.blockWhitelisted(block.getType())) {
            event.setCancelled(true);
            return;
        }


        if (BlockUtility.isLocationProtected(block.getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(msg);
            return;
        }

        if (event.getBlock().getLocation().getBlockX() > Game.getGameInstance().getSelectedMap().getxMax() ||
            event.getBlock().getLocation().getBlockX() < Game.getGameInstance().getSelectedMap().getxMin()) {

            event.setCancelled(true);
            return;
        }

        if (event.getBlock().getLocation().getBlockZ() > Game.getGameInstance().getSelectedMap().getzMax() ||
            event.getBlock().getLocation().getBlockZ() < Game.getGameInstance().getSelectedMap().getzMin()) {

            event.setCancelled(true);
            return;
        }

    }
}

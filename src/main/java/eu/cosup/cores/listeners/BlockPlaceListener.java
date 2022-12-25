package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
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

        if (Game.getGameInstance().getSelectedMap().getMaxHeight() < block.getY()) {
            player.sendMessage(ChatColor.RED+"Cannot place blocks here");
            event.setCancelled(true);
            return;
        }

        if (Game.getGameInstance().getSelectedMap().getMinHeight() > block.getY()) {
            player.sendMessage(ChatColor.RED+"Cannot place blocks here");
            event.setCancelled(true);
            return;
        }

        if (!BlockBreakListener.blockWhitelisted(block.getType())) {
            event.setCancelled(true);
            return;
        }


        for (Location teamSpawn : Game.getGameInstance().getSelectedMap().getTeamSpawns()) {

            if (teamSpawn.distance(block.getLocation()) < Cores.getInstance().getConfig().getDouble("spawn-protection-distance")) {
                event.getPlayer().sendMessage(ChatColor.RED+"Cannot place blocks so close to the spawn");
                event.setCancelled(true);
                return;
            }
        }
    }
}

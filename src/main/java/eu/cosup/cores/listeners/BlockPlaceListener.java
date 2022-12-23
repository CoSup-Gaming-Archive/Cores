package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public class BlockPlaceListener implements Listener {

    private static List<String> placableList = Cores.getInstance().getConfig().getStringList("whitelist-break");

    @EventHandler
    private void onPlayerPlaceBlock(BlockPlaceEvent event) {


        Player player = event.getPlayer();

        Block block = event.getBlock();

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            event.setCancelled(true);
        }

        if (Game.getGameInstance().getSelectedMap().getMaxHeight() < block.getY()) {
            player.sendMessage(ChatColor.RED+"Cannot place blocks here");
            event.setCancelled(true);
            return;
        }

        if (Game.getGameInstance().getSelectedMap().getMinHeight() > block.getY()) {
            player.sendMessage(ChatColor.RED+"Cannot place blocks here");
            event.setCancelled(true);
        }

    }

}

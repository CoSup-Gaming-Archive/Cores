package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.BeaconInformation;
import eu.cosup.cores.managers.NameTagEditor;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        Game game = Game.getGameInstance();
        NameTagEditor nameTagEditor = new NameTagEditor(event.getPlayer());
        nameTagEditor.setNameColor(ChatColor.RESET).setPrefix("").setTabName(event.getPlayer().getName());


        game.getPlayerList().remove(event.getPlayer());
        BeaconInformation.update();
        game.getJoinedPlayers().remove(event.getPlayer());
        game.refreshPlayerCount();
    }
}

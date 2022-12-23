package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {

        Game.getGameInstance().refreshPlayerCount();

    }
}

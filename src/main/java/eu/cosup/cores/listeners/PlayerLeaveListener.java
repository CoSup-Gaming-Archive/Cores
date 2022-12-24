package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {

    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {

        Game.getGameInstance().getJoinedPlayers().remove(event.getPlayer());

        Game.getGameInstance().refreshPlayerCount();

    }
}

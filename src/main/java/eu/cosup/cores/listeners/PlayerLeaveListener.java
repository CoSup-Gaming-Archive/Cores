package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.BeaconInformation;
import eu.cosup.cores.managers.GameStateManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        Game.getGameInstance().getPlayerList().remove(event.getPlayer());
        BeaconInformation.update();
        Game.getGameInstance().getJoinedPlayers().remove(event.getPlayer());
        Game.getGameInstance().refreshPlayerCount();
    }
}

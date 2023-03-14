package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.tournament.common.utility.PlayerUtility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class PlayerPickupArrowListener implements Listener {

    @EventHandler
    public void onPickupArrow(PlayerPickupArrowEvent event) {
        // if t
        if (PlayerUtility.isPlayerStaff(event.getPlayer().getUniqueId(), event.getPlayer().getName())) {
            // Just in case we want to allow staff to pickup arrows outside of the game for whatever reason
            // its interesting that a simple query needs to be this long ~Niko
            if (!Cores.getInstance().getGame().getGameStateManager().getGameState().equals(GameStateManager.GameState.ACTIVE)) {
                event.setCancelled(true);
            }
        }
    }
}

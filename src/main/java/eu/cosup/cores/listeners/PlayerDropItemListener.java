package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

@SuppressWarnings("unused")
public class PlayerDropItemListener implements Listener {

    // when player tries to get rid of default items we should stop him
    // we only prevent dropping if the game isnt running

    @EventHandler
    private void onPlayerThrow(PlayerDropItemEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
                event.setCancelled(true);
            }
        }
    }
}

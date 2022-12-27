package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.tasks.ActivateGameTask;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.util.Vector;

public class ItemThrowListener implements Listener {

    // when player tries to get rid of default items we should stop him

    @EventHandler
    private void onPlayerThrow(PlayerDropItemEvent event) {

        Material thrownMaterialName = event.getItemDrop().getItemStack().getType();

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (ActivateGameTask.isItemDefault(thrownMaterialName)) {
                event.setCancelled(true);
                return;
            }

            if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
                event.setCancelled(true);
            }
        }


        // TODO decide question bellow
        // do we want despawning in the tournament?
        //event.getItemDrop().setUnlimitedLifetime(true);

    }
}

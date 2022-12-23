package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.tasks.SpectatorTask;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {


        event.setCancelled(true);

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            // not really necessary
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
            return;
        }

        new SpectatorTask(event.getPlayer()).runTask(Cores.getInstance());

    }

}

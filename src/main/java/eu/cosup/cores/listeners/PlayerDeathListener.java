package eu.cosup.cores.listeners;

import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.tasks.SpectatorTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {

        event.setCancelled(true);
        new SpectatorTask(event.getPlayer()).run();

    }

}

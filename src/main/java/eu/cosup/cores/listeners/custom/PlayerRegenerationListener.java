package eu.cosup.cores.listeners.custom;

import eu.cosup.cores.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class PlayerRegenerationListener implements Listener {

    @EventHandler
    private void onPlayerHeal(EntityRegainHealthEvent event) {
    }
}

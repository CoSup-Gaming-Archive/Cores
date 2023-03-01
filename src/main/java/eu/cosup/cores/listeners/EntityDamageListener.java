package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {
    @EventHandler
    public void onEvent(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()) == null) {
            return;
        }
    }
}

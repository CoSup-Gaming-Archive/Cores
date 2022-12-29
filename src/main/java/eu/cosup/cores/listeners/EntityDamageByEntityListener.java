package eu.cosup.cores.listeners;

import eu.cosup.cores.managers.PlayerDamageManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    private void onEntityDamageEntity(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Player)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        PlayerDamageManager.setPlayerLastDamage((Player) event.getEntity(), (Player) event.getDamager());

    }

}

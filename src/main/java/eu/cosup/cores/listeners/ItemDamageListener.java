package eu.cosup.cores.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

@SuppressWarnings("unused")
public class ItemDamageListener implements Listener {

    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        // Fishing rods should not be infinitely usable therefor we allow their breaking
        if (event.getItem().getType() == Material.FISHING_ROD) {
            return;
        }

        // everything else is unbreakable
        event.setCancelled(true);
    }

}

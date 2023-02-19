package eu.cosup.cores.listeners.custom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.jetbrains.annotations.NotNull;

public class HungerReceiveListener implements Listener {

    @EventHandler
    private void onPlayerGetHungry(@NotNull FoodLevelChangeEvent event) {
        event.setCancelled(true);
        event.getEntity().setFoodLevel(20);
    }
}

package eu.cosup.cores.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerReceiveListener implements Listener {

    @EventHandler
    private void onPlayerGetHungry(FoodLevelChangeEvent event) {
        // event.getEntity().setFoodLevel(Integer.MAX_VALUE);
        event.getEntity().setFoodLevel(20); // how about this instead
    }

}

package eu.cosup.cores.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class HungerReceiveListener implements Listener {

    // TODO im not so sure this works (im pretty sure it does)
    @EventHandler
    private void onPlayerGetHungry(FoodLevelChangeEvent event) {
        event.getEntity().setFoodLevel(Integer.MAX_VALUE);
    }

}

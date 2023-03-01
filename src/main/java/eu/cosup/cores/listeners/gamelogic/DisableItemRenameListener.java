package eu.cosup.cores.listeners.gamelogic;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;

import java.util.Objects;

public class DisableItemRenameListener implements Listener {

    @EventHandler
    private void onRenameItem(PrepareAnvilEvent event) {

        if (event.getResult() == null) {
            return;
        }

        if (event.getResult().getItemMeta() == null) {
            return;
        }

        if (event.getResult().getItemMeta().displayName() == null) {
            return;
        }

        if (Objects.equals(event.getResult().getItemMeta().displayName(), Objects.requireNonNull(event.getInventory().getFirstItem()).getItemMeta().displayName())) {
            event.setResult(null);
        }
        if (Objects.equals(event.getResult().getItemMeta().displayName(), Objects.requireNonNull(event.getInventory().getSecondItem()).getItemMeta().displayName())) {
            event.setResult(null);
        }

    }
}

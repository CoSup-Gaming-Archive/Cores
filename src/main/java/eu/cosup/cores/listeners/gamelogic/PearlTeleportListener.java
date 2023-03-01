package eu.cosup.cores.listeners.gamelogic;

import eu.cosup.cores.Cores;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;

public class PearlTeleportListener implements Listener {

    @EventHandler
    private void onPlayerPearl(@NotNull PlayerTeleportEvent event) {
        // TODO is this needed?
        // 3/1/2023 yes its player qol -plusleft
        if (event.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
            for (Player player : Cores.getInstance().getServer().getOnlinePlayers()) {
                player.playSound(event.getTo(), Sound.ENTITY_ENDERMAN_TELEPORT, 10, 1);
            }
        }
    }
}

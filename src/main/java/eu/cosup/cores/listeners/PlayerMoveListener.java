package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.objects.Team;
import eu.cosup.cores.tasks.ActivateGameTask;
import eu.cosup.tournament.common.utility.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

public class PlayerMoveListener implements Listener {

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {


        // prevents ghosting
        if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            if (!PlayerUtility.isPlayerStaff(event.getPlayer().getUniqueId(), event.getPlayer().getName())) {
                if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

        if (
                event.getPlayer().getGameMode() == GameMode.CREATIVE
                || event.getPlayer().getGameMode() == GameMode.SPECTATOR
        ) {
            return;
        }

        // if player is bellow the threshold
        if (Game.getGameInstance().getSelectedMap().getDeathHeight() > event.getTo().getY()) {
            // he die
            event.getPlayer().setHealth(0);
        }

        if (event.getPlayer().getLocation().getBlockX() > Game.getGameInstance().getSelectedMap().getxMax() ||
            event.getPlayer().getLocation().getBlockX() < Game.getGameInstance().getSelectedMap().getxMin()) {

            event.getPlayer().setHealth(0);
            return;
        }

        if (event.getPlayer().getLocation().getBlockZ() > Game.getGameInstance().getSelectedMap().getzMax() ||
            event.getPlayer().getLocation().getBlockZ() < Game.getGameInstance().getSelectedMap().getzMin()) {

            event.getPlayer().setHealth(0);
            return;
        }
    }
}

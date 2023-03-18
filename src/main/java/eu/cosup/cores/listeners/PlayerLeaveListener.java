package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("unused")
public class PlayerLeaveListener implements Listener {
    @EventHandler
    private void onPlayerLeave(PlayerQuitEvent event) {
        Game game = Game.getGameInstance();

        if (game.getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {

            event.getPlayer().setHealth(0);

            // the player is not in a team
            if (Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId()) == null) {
                return;
            }

            if (!Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId()).isAlive()) {
                if (Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId()).getOnlinePlayers().size() < 2) {
                    Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId()).setAlive(false);
                }
            }
        }
    }
}

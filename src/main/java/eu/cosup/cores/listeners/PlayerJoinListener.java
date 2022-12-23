package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {

        Game.getGameInstance().getJoinedPlayers().add(event.getPlayer());

        Game.getGameInstance().refreshPlayerCount();


        if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            // if player is not in a team

            if (Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer()) == null) {

                Game.getGameInstance().getTeamManager().addPlayerToTeam(event.getPlayer(), TeamColor.SPECTATOR);

                event.getPlayer().setGameMode(GameMode.SPECTATOR);
                event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
                return;
            }

            event.getPlayer().setHealth(0);
            return;
        }

        event.getPlayer().setGameMode(GameMode.SPECTATOR);
        event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());

    }
}

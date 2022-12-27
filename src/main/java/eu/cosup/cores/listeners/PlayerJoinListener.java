package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.BeaconInformation;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.awt.*;

public class PlayerJoinListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Game.getGameInstance().getPlayerList().add(event.getPlayer());
        BeaconInformation.update();


        // if game has already started
        if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            // if player is not in a team

            TeamColor playerTeam = Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer());

            if (playerTeam == null) {

                event.getPlayer().sendMessage(ChatColor.RED + "You joined as spectator since there are enough players already!");
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
                event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
                return;
            }

            event.getPlayer().sendMessage(TeamColor.getChatColor(playerTeam) + "You joined as " + playerTeam);
            event.getPlayer().setHealth(0);
            return;
        }

        if (Game.getGameInstance().getJoinedPlayers().size() < Cores.getInstance().getConfig().getInt("required-player-count")) {
            Game.getGameInstance().getJoinedPlayers().add(event.getPlayer());
            Game.getGameInstance().refreshPlayerCount();
            event.getPlayer().sendMessage(ChatColor.YELLOW + "You joined!");
            // idk if this is good code but whatever
        } else {
            // the player will join as spectator
            event.getPlayer().sendMessage(ChatColor.RED + "You joined as spectator since there are enough players already!");
        }

        event.getPlayer().setGameMode(GameMode.SPECTATOR);
        event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());

    }
}

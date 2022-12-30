package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.BeaconInformation;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.NameTagEditor;
import eu.cosup.cores.managers.TeamColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Game game = Game.getGameInstance();
        
        game.getPlayerList().add(event.getPlayer());
        BeaconInformation.update();
        NameTagEditor nameTagEditor = new NameTagEditor(event.getPlayer());
        nameTagEditor.setNameColor(ChatColor.GRAY).setPrefix("Spectator ").setTabName(ChatColor.translateAlternateColorCodes('&', "&7"+event.getPlayer().getName()));

        // if game has already started
        if (game.getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            // if player is not in a team

            TeamColor playerTeam = game.getTeamManager().whichTeam(event.getPlayer());

            if (playerTeam == null) {
                Component msg = Component.text().content("You joined as spectator since the game already started").color(NamedTextColor.RED).build();
                event.getPlayer().sendMessage(msg);
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
                event.getPlayer().teleport(game.getSelectedMap().getSpectatorSpawn());
                return;
            }

            Component msg = Component.text().content("You joined as ").color(NamedTextColor.YELLOW)
                            .append(Component.text().content(playerTeam.toString()).color(TeamColor.getNamedTextColor(Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer())))).build();

            event.getPlayer().sendMessage(msg);
            event.getPlayer().setHealth(0);
            return;
        }

        if (game.getJoinedPlayers().size() < Cores.getInstance().getConfig().getInt("required-player-count")) {
            game.getJoinedPlayers().add(event.getPlayer());
            game.refreshPlayerCount();
        } else {
            // the player will join as spectator
            Component msg = Component.text().content("You joined as spectator since there are already enough players in the game").color(NamedTextColor.GRAY).build();
            event.getPlayer().sendMessage(msg);
        }

        event.getPlayer().setGameMode(GameMode.SPECTATOR);
        event.getPlayer().teleport(game.getSelectedMap().getSpectatorSpawn());

    }
}

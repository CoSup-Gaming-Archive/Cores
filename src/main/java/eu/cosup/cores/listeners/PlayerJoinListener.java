package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.core.data.Team;
import eu.cosup.cores.core.data.TeamColor;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.scoreboards.CoresScoreboard;
import eu.cosup.cores.tasks.SpectatorTask;
import eu.cosup.tournament.common.utility.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class PlayerJoinListener implements Listener {

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event) {
        Game game = Game.getGameInstance();
        CoresScoreboard.getScoreboards().put(event.getPlayer().getName(), new CoresScoreboard(event.getPlayer()));

        // Set scoreboard (tablist) for all players
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(allPlayer -> {
                    Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                        Cores.getInstance().updateScoreboard(allPlayer, onlinePlayer);
                    });
                });
            }
        }.runTaskAsynchronously(Cores.getInstance());


        if (PlayerUtility.isPlayerStaff(event.getPlayer().getUniqueId(), event.getPlayer().getName())) {
            event.getPlayer().setGameMode(GameMode.CREATIVE);
            event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
            return;
        }

        if (PlayerUtility.isPlayerStreamer(event.getPlayer().getUniqueId(), event.getPlayer().getName())) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false, false));
            event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
            return;
        }

        // The player needs to be readded to the teams player list on rejoin because the old player
        // object in the teams player list becomes stale
        Team team = Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId());
        if (team != null) {
            team.getPlayers().removeIf(player -> player.getUniqueId().equals(event.getPlayer().getUniqueId()));
            if (team.getPlayers().stream().noneMatch(player -> player.getUniqueId().equals(event.getPlayer().getUniqueId()))) {
                team.getPlayers().add(event.getPlayer());
            }
        }


        // if game has already started
        if (game.getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            event.getPlayer().setHealth(0);
            return;
        }
        event.getPlayer().getInventory().clear();

        event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
        if (!PlayerUtility.isPlayerStaff(event.getPlayer().getUniqueId(), event.getPlayer().getName())) {
            new SpectatorTask(event.getPlayer(), false).runTask(Cores.getInstance());
            return;
        }
    }
}

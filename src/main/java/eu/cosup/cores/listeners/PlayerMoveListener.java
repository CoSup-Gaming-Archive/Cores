package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.core.data.BeaconState;
import eu.cosup.cores.core.data.TeamColor;
import eu.cosup.cores.managers.GameStateManager;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlayerMoveListener implements Listener {

    public PlayerMoveListener() {

        // TODO document this and explain why it needs to run every tick, those might be some very expensive operations.
        new BukkitRunnable() {
            @Override
            public void run() {

                // we dont want to do this in arena
                List<TeamColor> leftBeaconsChange = new ArrayList<>();
                List<TeamColor> rightBeaconsChange = new ArrayList<>();

                Game.getGameInstance().getTeamManager().getTeams().forEach(team -> {
                    if (team.getLeftBeaconState() != BeaconState.OFF) {
                        team.setLeftBeaconState(BeaconState.ON);
                    }
                    if (team.getRightBeaconState() != BeaconState.OFF) {
                        team.setRightBeaconState(BeaconState.ON);
                    }

                });

                if (Game.getGameInstance().getGameStateManager().getGamePhase() == GameStateManager.GamePhase.ARENA) {
                    return;
                }

                for (Player player : Cores.getInstance().getServer().getOnlinePlayers()) {
                    for (TeamColor teamColor : Game.getGameInstance().getSelectedMap().getTeamBeacons().keySet()) {
                        Pair<Location, Location> beaconLocations = Game.getGameInstance().getSelectedMap().getTeamBeacons().get(teamColor);

                        if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()) == null) {
                            continue;
                        }

                        if (teamColor == Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor()) {
                            continue;
                        }

                        if (beaconLocations.left().distance(player.getLocation()) < 5) {
                            if (Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getLeftBeaconState() != BeaconState.OFF) {
                                leftBeaconsChange.add(teamColor);
                            }
                        }

                        if (beaconLocations.right().distance(player.getLocation()) < 5) {
                            if (Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getRightBeaconState() != BeaconState.OFF) {
                                rightBeaconsChange.add(teamColor);
                            }
                        }
                    }
                }

                leftBeaconsChange.forEach(teamColor -> {
                    Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).setLeftBeaconState(BeaconState.ATTACK);
                    Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getOnlinePlayers().forEach(player ->
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1));
                });
                rightBeaconsChange.forEach(teamColor -> {
                    Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).setRightBeaconState(BeaconState.ATTACK);
                    Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getOnlinePlayers().forEach(player ->
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1, 1));
                });

            }
        }.runTaskTimer(Cores.getInstance(), 1, 1);

    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {

        if (
                event.getPlayer().getGameMode() == GameMode.CREATIVE
                        || event.getPlayer().getGameMode() == GameMode.SPECTATOR
        ) {
            return;
        }

        if (Game.getGameInstance().getGameStateManager().getGamePhase() == GameStateManager.GamePhase.ARENA) {
            return;
        }

        if (Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId()) != null) {
            if (Objects.requireNonNull(Game.getGameInstance().getTeamManager().whichTeam(event.getPlayer().getUniqueId())).isPlayerDead(event.getPlayer())) {
                return;
            }
        }

        // if player is bellow the threshold
        if (Game.getGameInstance().getSelectedMap().getDeathHeight() > event.getTo().getY()) {
            // he die
            event.getPlayer().setHealth(0);
        }
    }
}

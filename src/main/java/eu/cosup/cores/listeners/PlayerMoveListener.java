package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.objects.BeaconState;
import eu.cosup.cores.objects.Team;
import eu.cosup.cores.objects.TeamColor;
import eu.cosup.cores.tasks.ActivateGameTask;
import eu.cosup.tournament.common.utility.PlayerUtility;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PlayerMoveListener implements Listener {

    public PlayerMoveListener() {

        new BukkitRunnable() {
            @Override
            public void run() {

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

                for (Player player : Cores.getInstance().getServer().getOnlinePlayers()) {
                    for (TeamColor teamColor : Game.getGameInstance().getSelectedMap().getTeamBeacons().keySet()) {
                        Pair<Location, Location> beaconLocations = Game.getGameInstance().getSelectedMap().getTeamBeacons().get(teamColor);

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
                });
                rightBeaconsChange.forEach(teamColor -> {
                    Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).setRightBeaconState(BeaconState.ATTACK);
                });

            }
        }.runTaskTimer(Cores.getInstance(), 1, 1);

    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {

        // prevents ghosting
        if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            if (!PlayerUtility.isPlayerStaff(event.getPlayer().getUniqueId(), event.getPlayer().getName())) {
                if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
                    event.setCancelled(true);
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 100));
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

        if (PlayerUtility.isPlayerStaff(event.getPlayer().getUniqueId(), event.getPlayer().getName())) {
            return;
        }


    }
}

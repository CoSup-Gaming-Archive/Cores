package eu.cosup.cores.listeners.custom;

import eu.cosup.cores.Game;
import eu.cosup.cores.objects.BeaconState;
import eu.cosup.cores.objects.Team;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BeaconBreakListener implements Listener {

    @EventHandler
    private void onPlayerInteractWithBeacon(PlayerInteractEvent event) {

        event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        event.getPlayer().removePotionEffect(PotionEffectType.FAST_DIGGING);

        for (Team team : Game.getGameInstance().getTeamManager().getTeams()) {
            team.setRightBeaconState(BeaconState.ON);
            team.setLeftBeaconState(BeaconState.ON);
        }

        if (event.getClickedBlock() == null) {
            return;
        }

        if (event.getClickedBlock().getType() != Material.BEACON) {
            return;
        }

        Team beaconTeam = Game.getGameInstance().getSelectedMap().whichTeamBeacon(event.getClickedBlock().getLocation());

        if (beaconTeam == null) {
            return;
        }

        if (Game.getGameInstance().getSelectedMap().isLeftBeacon(beaconTeam.getColor(), event.getClickedBlock().getLocation())) {
            beaconTeam.setLeftBeaconState(BeaconState.ATTACK);
        } else {
            beaconTeam.setRightBeaconState(BeaconState.ATTACK);
        }

        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 1, false, false, false));
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 1, false, false, false));
    }
}

package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.core.data.BeaconState;
import eu.cosup.cores.core.data.Team;
import eu.cosup.cores.core.data.TeamColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeamLoseBeaconTask extends BukkitRunnable {

    private Location beaconLocation;
    private Player killer;

    public TeamLoseBeaconTask(@NotNull Location beaconLocation, @Nullable Player killer) {
        this.killer = killer;
        this.beaconLocation = beaconLocation;

        this.runTask(Cores.getInstance());
    }

    @Override
    public void run() {

        Team killerTeam = null;

        if (killer != null) {
            killerTeam = Game.getGameInstance().getTeamManager().whichTeam(killer.getUniqueId());
        }

        TeamColor killerTeamColor = null;

        if (killerTeam != null) {
            killerTeamColor = killerTeam.getColor();
        }

        Team beaconTeam = Game.getGameInstance().getSelectedMap().whichTeamBeacon(beaconLocation);

        if (beaconTeam == null) {
            return;
        }

        // so no own kill
        if (killerTeamColor == beaconTeam.getColor()) {
            if (killer.getGameMode() != GameMode.CREATIVE) {
                return;
            }
        }

        Cores.getInstance().getGameWorld().setType(beaconLocation, Material.AIR);
        beaconTeam.setBeaconCount(beaconTeam.getBeaconCount() - 1);

        Component beaconSide = Component.text("Left").color(NamedTextColor.YELLOW);

        boolean isLeftBeacon = Game.getGameInstance().getSelectedMap().isLeftBeacon(beaconTeam.getColor(), beaconLocation);
        if (!isLeftBeacon) {
            beaconSide = Component.text("Right").color(NamedTextColor.YELLOW);
        }

        if (Game.getGameInstance().getSelectedMap().isLeftBeacon(beaconTeam.getColor(), beaconLocation)) {
            beaconTeam.setLeftBeaconState(BeaconState.OFF);
        } else if (!Game.getGameInstance().getSelectedMap().isLeftBeacon(beaconTeam.getColor(), beaconLocation)) {
            beaconTeam.setRightBeaconState(BeaconState.OFF);
        }

        for (Player alivePlayer : Cores.getInstance().getServer().getOnlinePlayers()) {
            if (killerTeamColor != null) {
                Title title = Title.title(beaconSide
                        .append(Component.text(" Beacon ").color(TeamColor.getNamedTextColor(beaconTeam.getColor())))
                        .append(Component.text(" destroyed")),
                        Component.text("By ")
                        .append(Component.text(killer.getName()).color(TeamColor.getNamedTextColor(killerTeam.getColor())))
                        .append(Component.text(" destroyed")));
                alivePlayer.showTitle(title);
            }
            alivePlayer.playSound(alivePlayer.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
        }

        // broadcast that they lost bed
        Component msg = Component.text().content("A ").color(TextColor.color(NamedTextColor.YELLOW))
                .append(Component.text().content(TeamColor.getFormattedTeamColor(beaconTeam.getColor()) + " beacon").color(TeamColor.getNamedTextColor(beaconTeam.getColor())))
                .append(Component.text().content(" was destroyed!").color(NamedTextColor.YELLOW)).build();

        Cores.getInstance().getServer().broadcast(msg);

        if (beaconTeam.getBeaconCount() < 1) {
            beaconTeam.setAlive(false);
        }
    }
}

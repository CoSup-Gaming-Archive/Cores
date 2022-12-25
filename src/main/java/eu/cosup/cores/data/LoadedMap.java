package eu.cosup.cores.data;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;

public class LoadedMap {

    // PLEASE be sure to change this for your need

    // object for the loaded map

    private String name;
    private ArrayList<Location> teamBlueBeacons;
    private ArrayList<Location> teamRedBeacons;
    private Location spectatorSpawn;

    // these heighs are how high and how low players can place blocks
    private int maxHeight;
    private int minHeight;

    // we dont want to players to fall down too much
    private int deathHeight;

    private Location teamBlueSpawns;
    private Location teamRedSpawns;

    public LoadedMap(String name, ArrayList<Location> teamBlueBeacons, ArrayList<Location> teamRedBeacons, Location teamBlueSpawns, Location teamRedSpawns, Location spectatorSpawn, int maxHeight, int minHeight, int deathHeight) {

        // TODO add a rotation aswell so players spawn facing determined location

        this.name = name;
        this.teamBlueSpawns = teamBlueSpawns;
        this.teamRedSpawns = teamRedSpawns;
        this.teamBlueBeacons = teamBlueBeacons;
        this.teamRedBeacons = teamRedBeacons;
        this.spectatorSpawn = spectatorSpawn;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.deathHeight = deathHeight;

    }

    public String getName() {
        return name;
    }

    public Location getTeamBlueSpawns() {
        return teamBlueSpawns;
    }

    public Location getTeamRedSpawns() {
        return teamRedSpawns;
    }

    public ArrayList<Location> getTeamBlueBeacons() {
        return teamBlueBeacons;
    }

    public ArrayList<Location> getTeamRedBeacons() {
        return teamRedBeacons;
    }

    public int getDeathHeight() {
        return deathHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public Location getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public ArrayList<Location> getTeamSpawns() {
        ArrayList<Location> teamSpawns = new ArrayList<>();

        teamSpawns.add(getTeamBlueSpawns());
        teamSpawns.add(getTeamRedSpawns());

        return teamSpawns;
    }

    public ArrayList<Location> getAllBeaconLocations() {

        ArrayList<Location> beaconLocations = new ArrayList<>();

        beaconLocations.addAll(getTeamBlueBeacons());
        beaconLocations.addAll(getTeamRedBeacons());

        return beaconLocations;
    }

    // returns the team that the beacon belongs to
    public TeamColor whichTeamBeacon(Location beaconLocation) {

        for (Location location : getTeamBlueBeacons()) {

            if (Objects.equals(location.toVector(), beaconLocation.toVector())) {
                return TeamColor.BLUE;
            }

        }

        for (Location location : getTeamRedBeacons()) {

            if (Objects.equals(location.toVector(), beaconLocation.toVector())) {
                return TeamColor.RED;
            }
        }
        // in case the beacon is from noone
        return null;
    }

    public Location getSpawnByTeamColor(TeamColor teamColor) {

        if (teamColor == TeamColor.RED) {
            return getTeamRedSpawns();
        }

        if (teamColor == TeamColor.BLUE) {
            return getTeamBlueSpawns();
        }

        return getSpectatorSpawn();
    }

    public Location getSpawnByPlayer(Player player) {

        TeamColor teamColor = Game.getGameInstance().getTeamManager().whichTeam(player);

        return getSpawnByTeamColor(teamColor);
    }
}

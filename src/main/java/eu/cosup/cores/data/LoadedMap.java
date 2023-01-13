package eu.cosup.cores.data;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

public class LoadedMap {

    // PLEASE be sure to change this for your need

    // object for the loaded map

    private final ArrayList<Location> teamBlueBeacons;
    private final ArrayList<Location> teamRedBeacons;
    private final Location spectatorSpawn;

    // these heighs are how high and how low players can place blocks
    private final int maxHeight;
    private final int minHeight;

    // we dont want to players to fall down too much
    private final int deathHeight;

    private final Location teamBlueSpawns;
    private final Location teamRedSpawns;

    private final int xMax;
    private final int xMin;
    private final int zMax;
    private final int zMin;

    public LoadedMap(ArrayList<Location> teamBlueBeacons, ArrayList<Location> teamRedBeacons, Location teamBlueSpawns, Location teamRedSpawns, Location spectatorSpawn, int maxHeight, int minHeight, int deathHeight
                    , int xMax, int xMin, int zMax, int zMin) {

        this.teamBlueSpawns = teamBlueSpawns;
        this.teamRedSpawns = teamRedSpawns;
        this.teamBlueBeacons = teamBlueBeacons;
        this.teamRedBeacons = teamRedBeacons;
        this.spectatorSpawn = spectatorSpawn;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.deathHeight = deathHeight;
        this.xMax = xMax;
        this.xMin = xMin;
        this.zMax = zMax;
        this.zMin = zMin;
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

    public int getxMax() {
        return xMax;
    }

    public int getxMin() {
        return xMin;
    }

    public int getzMax() {
        return zMax;
    }

    public int getzMin() {
        return zMin;
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

    public static LoadedMap loadMapFromConfig() {

        File configFile = new File(Cores.getInstance().getDataFolder(), "maps.yml");

        if (!configFile.exists()) {
            Bukkit.getLogger().severe("There is no maps.yml in datafolder while trying to load a map");
            return null;
        }

        YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(configFile);

        int maxHeight = customConfig.getInt("maxHeight");
        int minHeight = customConfig.getInt("minHeight");
        int deathHeight = customConfig.getInt("deathHeight");

        // idk this is not mandatory
        if (minHeight == maxHeight) {
            Bukkit.getLogger().severe("   minHeight cannot be the same as the maxHeight plase change that");
            return null;
        }

        Location teamBlueSpawns = customConfig.getLocation("teamBlueSpawns");
        Location teamRedSpawns = customConfig.getLocation("teamRedSpawns");
        Location spectatorSpawn = customConfig.getLocation("spectatorSpawn");

        // should probably split this up
        if (teamBlueSpawns == null) {
            Bukkit.getLogger().severe("blue spawn for " + " was not loaded corectly");
            return null;
        }

        if (teamRedSpawns == null) {
            Bukkit.getLogger().severe("red spawns for " + " was not loaded corretly");
            return null;
        }

        if (spectatorSpawn == null) {
            Bukkit.getLogger().severe("spectator spawn for " + " was not loaded corectly");
            return null;
        }

        ArrayList<Location> teamBlueBeacons = new ArrayList<>();
        try {
            Objects.requireNonNull(customConfig.getList("teamBlueBeacons"))
                    .forEach(o -> teamBlueBeacons.add((Location) o));
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Couldnt load blue team beacons for ");
            return null;
        }

        ArrayList<Location> teamRedBeacons = new ArrayList<>();
        try {
            Objects.requireNonNull(customConfig.getList("teamRedBeacons"))
                    .forEach(o -> teamRedBeacons.add((Location) o));
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Couldnt load red team beacons for ");
            return null;
        }

        int xMax = customConfig.getInt("xMax");
        int zMax = customConfig.getInt("zMax");
        int zMin = customConfig.getInt("zMin");
        int xMin = customConfig.getInt("xMin");

        if (xMax == xMin || zMax == zMin) {
            Bukkit.getLogger().severe("Acording to your configuration for "+" players not by able to place blocks anywhere");
            Bukkit.getLogger().severe("Make sure you have the correct version of maps.yml (check in repository)");
        }

        return new LoadedMap(
                teamBlueBeacons,
                teamRedBeacons,
                teamBlueSpawns,
                teamRedSpawns,
                spectatorSpawn,
                maxHeight,
                minHeight,
                deathHeight,
                xMax,
                xMin,
                zMax,
                zMin
        );
    }
}

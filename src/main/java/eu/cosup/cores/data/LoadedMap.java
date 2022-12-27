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

    public void saveToConfig() {

        File configFile = new File(Cores.getInstance().getDataFolder(), "maps.yml");

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException exception) {
                Bukkit.getLogger().severe("There was no maps file and we were not able to create new one.");
            }
        }

        YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(configFile);

        customConfig.set(name + ".maxHeight", maxHeight);
        customConfig.set(name + ".minHeight", minHeight);
        customConfig.set(name + ".deathHeight", deathHeight);

        customConfig.set(name + ".teamBlueSpawns", teamBlueSpawns);
        customConfig.set(name + ".teamRedSpawns", teamRedSpawns);

        customConfig.set(name + ".teamBlueBeacons", teamBlueBeacons);
        customConfig.set(name + ".teamRedBeacons", teamRedBeacons);

        customConfig.set(name + ".spectatorSpawn", spectatorSpawn);


        try {
            customConfig.save(configFile);
        } catch (IOException exception) {
            Bukkit.getLogger().severe("Were not able to save map to config");
        }
    }

    public static LoadedMap loadMapFromConfig(String name) {

        File configFile = new File(Cores.getInstance().getDataFolder(), "maps.yml");

        if (!configFile.exists()) {
            Bukkit.getLogger().severe("There is no maps.yml in datafolder while trying to load a map");
            return null;
        }

        YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(configFile);

        if (!customConfig.contains(name)) {
            Bukkit.getLogger().severe("The map that we are trying to load does not exists in maps.yml");
            return null;
        }

        int maxHeight = customConfig.getInt(name + ".maxHeight");
        int minHeight = customConfig.getInt(name + ".minHeight");
        int deathHeight = customConfig.getInt(name + ".deathHeight");

        // idk this is not mandatory
        if (minHeight == maxHeight) {
            Bukkit.getLogger().severe(name + "   minHeight cannot be the same as the maxHeight plase change that");
            return null;
        }

        Location teamBlueSpawns = customConfig.getLocation(name + ".teamBlueSpawns");
        Location teamRedSpawns = customConfig.getLocation(name + ".teamRedSpawns");
        Location spectatorSpawn = customConfig.getLocation(name + ".spectatorSpawn");

        // should probably split this up
        if (teamBlueSpawns == null) {
            Bukkit.getLogger().severe("blue spawn for " + name + " was not loaded corectly");
            return null;
        }

        if (teamRedSpawns == null) {
            Bukkit.getLogger().severe("red spawns for " + name + " was not loaded corretly");
            return null;
        }

        if (spectatorSpawn == null) {
            Bukkit.getLogger().severe("spectator spawn for " + name + " was not loaded corectly");
            return null;
        }

        ArrayList<Location> teamBlueBeacons = new ArrayList<>();
        try {
            Objects.requireNonNull(customConfig.getList(name + ".teamBlueBeacons"))
                    .forEach(o -> teamBlueBeacons.add((Location) o));
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Couldnt load blue team beacons for " + name);
            return null;
        }

        ArrayList<Location> teamRedBeacons = new ArrayList<>();
        try {
            Objects.requireNonNull(customConfig.getList(name + ".teamRedBeacons"))
                    .forEach(o -> teamRedBeacons.add((Location) o));
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Couldnt load red team beacons for " + name);
            return null;
        }

        return new LoadedMap(
                name,
                teamBlueBeacons,
                teamRedBeacons,
                teamBlueSpawns,
                teamRedSpawns,
                spectatorSpawn,
                maxHeight,
                minHeight,
                deathHeight
        );
    }

    public static ArrayList<LoadedMap> getLoadedMapsFromConfig() {

        ArrayList<LoadedMap> loadedMaps = new ArrayList<>();

        File configFile = new File(Cores.getInstance().getDataFolder(), "maps.yml");

        if (!configFile.exists()) {
            Bukkit.getLogger().severe("There is no maps.yml in datafolder while trying to load a map");
            return null;
        }

        YamlConfiguration customConfig = YamlConfiguration.loadConfiguration(configFile);

        Set<String> mapNames = customConfig.getKeys(false);

        if (mapNames.size() == 0) {
            Bukkit.getLogger().severe("There are no maps in maps.yml");
            return null;
        }

        for (String name : mapNames) {

            LoadedMap loadedMap = LoadedMap.loadMapFromConfig(name);
            if (loadedMap != null) {
                loadedMaps.add(loadedMap);
            }
        }

        return loadedMaps;
    }
}

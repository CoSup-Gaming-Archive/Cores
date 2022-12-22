package eu.cosup.cores.data;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;

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
}

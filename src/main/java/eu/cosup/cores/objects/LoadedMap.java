package eu.cosup.cores.objects;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class LoadedMap {

    private final Location spectatorSpawn;

    private final int maxHeight;
    private final int minHeight;

    private final int deathHeight;

    private final int xMax;
    private final int xMin;
    private final int zMax;
    private final int zMin;
    private HashMap<TeamColor, Location> teamSpawns;
    private HashMap<TeamColor, Pair<Location, Location>> teamBeacons;
    public LoadedMap(HashMap<TeamColor, Location> teamSpawns, HashMap<TeamColor, Pair<Location, Location>> teamBeacons, Location spectatorSpawn, int maxHeight, int minHeight, int deathHeight
                    , int xMax, int xMin, int zMax, int zMin
    ) {
        this.teamSpawns = teamSpawns;
        this.spectatorSpawn = spectatorSpawn;
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.deathHeight = deathHeight;
        this.xMax = xMax;
        this.xMin = xMin;
        this.zMax = zMax;
        this.zMin = zMin;
        this.teamBeacons = teamBeacons;
    }

    public HashMap<TeamColor, Pair<Location, Location>> getTeamBeacons() {
        return teamBeacons;
    }

    public HashMap<TeamColor, Location> getTeamSpawns() {
        return teamSpawns;
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

    public Location getSpawnByPlayer(Player player) {

        TeamColor teamColor = Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor();

        return teamSpawns.get(teamColor);
    }

    public @Nullable Team whichTeamBeacon(@NotNull Location location) {

        for (TeamColor teamColor : getTeamBeacons().keySet()) {

            if (getTeamBeacons().get(teamColor).left().toVector().equals(location.toVector())) {
                return Game.getGameInstance().getTeamManager().getTeamByColor(teamColor);
            }

            if (getTeamBeacons().get(teamColor).right().toVector().equals(location.toVector())) {
                return Game.getGameInstance().getTeamManager().getTeamByColor(teamColor);
            }

        }

        return null;
    }

    public boolean isLeftBeacon(@NotNull TeamColor teamColor, @NotNull Location location) {

        return getTeamBeacons().get(teamColor).left().toVector().equals(location.toVector());
    }

    public Location getSpawnByColor(TeamColor teamColor) {
        return teamSpawns.get(teamColor);
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

        Location spectatorSpawn = customConfig.getLocation("spectatorSpawn");

        if (spectatorSpawn == null) {
            Bukkit.getLogger().severe("spectator spawn for " + " was not loaded corectly");
            return null;
        }

        ConfigurationSection teamSpawnsSection = customConfig.getConfigurationSection("teams");

        HashMap<TeamColor, Location> spawns = new HashMap<>();
        HashMap<TeamColor, Pair<Location, Location>> beacons = new HashMap<>();

        for (String teamKey : teamSpawnsSection.getKeys(false)) {

            Location spawnLocation = teamSpawnsSection.getLocation(teamKey + ".spawn");
            Location leftBeacon = teamSpawnsSection.getLocation(teamKey + ".beacon-left");
            Location rightBeacon = teamSpawnsSection.getLocation(teamKey + ".beacon-right");

            TeamColor teamColor;
            try {
                teamColor = TeamColor.valueOf(teamKey.toUpperCase());
            } catch (IllegalArgumentException exception) {
                Bukkit.getLogger().severe("No such team color exists as: " + teamKey + " from map: "+" therefore we didnt register map");
                return null;
            }

            spawns.put(teamColor, spawnLocation);
            beacons.put(teamColor, new Pair<>() {
                @Override
                public Location left() {
                    return leftBeacon;
                }

                @Override
                public Location right() {
                    return rightBeacon;
                }
            });
        }

        if (spawns.size() == 0) {
            Bukkit.getLogger().severe("There are no spawns for no teams therefore map was not considered.");
            return null;
        }

        int xMax = customConfig.getInt(".xMax");
        int zMax = customConfig.getInt(".zMax");
        int zMin = customConfig.getInt(".zMin");
        int xMin = customConfig.getInt(".xMin");

        if (xMax == xMin || zMax == zMin) {
            Bukkit.getLogger().severe("Make sure you have the correct version of maps.yml (check in repository)");
        }

        return new LoadedMap(
                spawns,
                beacons,
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

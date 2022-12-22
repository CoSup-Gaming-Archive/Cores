package eu.cosup.cores;

// TODO remove unused imports in all of the files

import eu.cosup.cores.data.LoadedMap;
import eu.cosup.cores.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;

public final class Cores extends JavaPlugin {

    private static Cores instance;

    public static Cores getInstance() {
        return instance;
    }

    private ArrayList<LoadedMap> loadedMaps = new ArrayList<>();
    private Game game;
    private World world;

    @Override
    public void onEnable() {
        instance = this;

        world = Bukkit.getWorlds().get(0);

        reloadConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        loadMaps();

        if (loadedMaps.size() == 0) {
            getLogger().severe("No schematics found...");
            return;
        }


        Random random = new Random();

        // by default we get number 0
        LoadedMap selectedMap = loadedMaps.get(0);

        // if there are more maps to choose from
        if (loadedMaps.size() > 1) {
            selectedMap = loadedMaps.get(random.nextInt()*loadedMaps.size()-1);
        }

        game = new Game(selectedMap);


        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public World getWorld() {
        return world;
    }

    public Game getGame() {
        return game;
    }

    private void loadMaps() {

        // TODO load maps


        // TODO this is a test so remove this
        loadedMaps.add(new LoadedMap(
            "TestMap",
                new ArrayList<>(),
                new ArrayList<>(),
                new Location(getWorld(),-72,81,256),
                new Location(getWorld(),57,81,256),
                new Location(getWorld(),0,135,259),
                89,
                76,
                76
        ));

    }
}

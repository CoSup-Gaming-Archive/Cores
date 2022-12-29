package eu.cosup.cores;

// TODO remove unused imports in all of the files

import eu.cosup.cores.commands.ForceStartCommand;
import eu.cosup.cores.commands.SpectatorCommand;
import eu.cosup.cores.data.LoadedMap;
import eu.cosup.cores.data.WorldLoader;
import eu.cosup.cores.listeners.*;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public final class Cores extends JavaPlugin {

    private static Cores instance;

    //very nice comment
    public static Cores getInstance() {
        return instance;
    }

    private ArrayList<LoadedMap> loadedMaps = new ArrayList<>();
    private Game game;
    private World gameWorld;
    private World lobbyWorld;

    // earlier than onEnable
    @Override
    public void onEnable() {
        instance = this;

        gameWorld = Bukkit.getWorld("world");
        lobbyWorld = Bukkit.getWorld("world_nether");

        reloadConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        // load maps here
        loadMaps();

        if (loadedMaps.size() == 0) {
            Bukkit.getLogger().severe("Were not able to load any maps");
            return;
        }

        // initial creation of game.
        if (!createGame()) {
            return;
        }

        // past this point all of the listeners will be initialized and it is expectted that everything works fine

        getServer().getPluginManager().registerEvents(new ItemThrowListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);

        // player quility
        getServer().getPluginManager().registerEvents(new HungerReceiveListener(), this);

        getServer().getPluginManager().registerEvents(new ItemCraftListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);


        // commands
        Objects.requireNonNull(getCommand("spectate")).setExecutor(new SpectatorCommand());
        Objects.requireNonNull(getCommand("forcestart")).setExecutor(new ForceStartCommand());


    }

    public boolean createGame() {
        LoadedMap selectedMap = selectMap();
        if (selectedMap == null) {
            Bukkit.getLogger().severe("We were not able to create a game.");
            return false;
        }
        game = new Game(selectedMap);
        Bukkit.getLogger().warning("Succesfully started a game.");
        return true;
    }

    private LoadedMap selectMap() {

        Random random = new Random();

        // by default we get number 0
        LoadedMap selectedMap = loadedMaps.get(0);

        // if there are more maps to choose from
        if (loadedMaps.size() > 1) {
            int selection = random.nextInt(loadedMaps.size());
            Bukkit.getLogger().warning("Choosing from: " + loadedMaps + " chose:" + selection);
            selectedMap = loadedMaps.get(selection);
        }

        Bukkit.getLogger().info("Selected map: " + selectedMap.getName());

        if (!WorldLoader.loadNewWorld(selectedMap.getName())) {
            Bukkit.getLogger().severe("Not able to load map");
            return null;
        }

        return selectedMap;
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public World getGameWorld() {
        return gameWorld;
    }

    public World getLobbyWorld() {
        return lobbyWorld;
    }

    public void setGameWorld(World gameWorld) {
        this.gameWorld = gameWorld;
    }

    public Game getGame() {
        return game;
    }

    private void loadMaps() {

        // we just have to have the name of the world and then we can load it from config by name
        ArrayList<LoadedMap> loadedMapsConfig = LoadedMap.getLoadedMapsFromConfig();
        if (loadedMapsConfig != null) {
            loadedMaps = loadedMapsConfig;
        }

        WorldLoader.getWorldNames();

        // this is to sort out the maps that dont exist in the folder
        List<LoadedMap> tempLoadedMaps = loadedMaps.stream().filter(loadedMap -> WorldLoader.getWorldNames().contains(loadedMap.getName())).toList();
        loadedMaps = new ArrayList<>();
        loadedMaps.addAll(tempLoadedMaps);
    }
}

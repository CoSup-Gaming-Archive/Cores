package eu.cosup.cores;

// TODO remove unused imports in all of the files

import eu.cosup.cores.commands.SpectatorCommand;
import eu.cosup.cores.data.LoadedMap;
import eu.cosup.cores.listeners.*;
import eu.cosup.cores.managers.ScoreBoardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.ArrayList;
import java.util.Random;

public final class Cores extends JavaPlugin {

    private static Cores instance;
    //very nice comment
    public static Cores getInstance() {
        return instance;
    }

    private ArrayList<LoadedMap> loadedMaps = new ArrayList<>();
    private Game game;
    private World world;
    public ArrayList<Player> playerList=new ArrayList<Player>();

    @Override
    public void onEnable() {
        instance = this;

        world = Bukkit.getWorlds().get(0);

        reloadConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        // load maps here
        loadMaps();

        if (loadedMaps.size() == 0) {
            getLogger().severe("No schematics found...");
            return;
        }

        // initial creation of game.
        createGame();

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

        // commands
        getCommand("spectate").setExecutor(new SpectatorCommand());
        ScoreBoardManager sbm=new ScoreBoardManager("main");
        sbm.setObjective(Bukkit.getScoreboardManager().getMainScoreboard().getObjective("main"));
        sbm.setDName(ChatColor.translateAlternateColorCodes('&', "&bCores")).addItem(" ").addItem(ChatColor.translateAlternateColorCodes('&', "&9Blue Beacons: &a\u2714 &a\u2714")).addItem(ChatColor.translateAlternateColorCodes('&', "&cRed Beacons: &a\u2714 &a\u2714")).addItem("  ").addItem(ChatColor.translateAlternateColorCodes('&', "   &6CoSup &bGaming")).setSlot(DisplaySlot.SIDEBAR).getObjective();
    }

    public void createGame() {
        game = new Game(selectMap());
    }

    private LoadedMap selectMap() {

        Random random = new Random();

        // by default we get number 0
        LoadedMap selectedMap = loadedMaps.get(0);

        // if there are more maps to choose from
        if (loadedMaps.size() > 1) {
            selectedMap = loadedMaps.get(random.nextInt()*loadedMaps.size()-1);
        }

        return selectedMap;
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

        //TODO load maps


        //this is just for my world (Sakura map i downloaded from intenret)
        //TODO remove this this just my testing world
        ArrayList<Location> blueBeacons = new ArrayList<>();
        blueBeacons.add(new Location(getWorld(), -34,80,237));
        blueBeacons.add(new Location(getWorld(), -34,80,275));
//
//
        ArrayList<Location> redBeacons = new ArrayList<>();
        //LMAO BRUH
        redBeacons.add(new Location(getWorld(), 18,80,275));
        redBeacons.add(new Location(getWorld(), 18,80,237));
//
        //// TODO this is a test so remove this
        loadedMaps.add(new LoadedMap(
            "TestMap",
                blueBeacons,
                redBeacons,
                new Location(getWorld(),-72,81,256),
                new Location(getWorld(),57,81,256),
                new Location(getWorld(),-4,121,257),
                89,
                76,
                76
        ));

    }
}

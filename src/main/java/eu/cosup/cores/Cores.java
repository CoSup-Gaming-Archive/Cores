package eu.cosup.cores;

import eu.cosup.cores.listeners.StartGameCommandListener;
import eu.cosup.cores.listeners.*;
import eu.cosup.cores.listeners.custom.*;
import eu.cosup.cores.listeners.PlayerDeathListener;
import eu.cosup.cores.listeners.PlayerJoinListener;
import eu.cosup.cores.listeners.PlayerLeaveListener;
import eu.cosup.cores.listeners.PlayerMoveListener;
import eu.cosup.cores.managers.ScoreBoardManager;

import eu.cosup.cores.objects.LoadedMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;


public final class Cores extends JavaPlugin {

    private static Cores instance;
    private World gameWorld;
    private Game game;

    public static Cores getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        gameWorld = Bukkit.getWorld("world");

        reloadConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        // initial creation of game.
        if (!createGame()) {
            return;
        }

        // register all the listeners
        getServer().getPluginManager().registerEvents(new ItemDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerLeaveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new ItemThrowListener(), this);
        getServer().getPluginManager().registerEvents(new HungerReceiveListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new EntityExplodeListener(), this);
        getServer().getPluginManager().registerEvents(new TNTPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new PearlTeleportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerShootFireballListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);

        new StartGameCommandListener();
        new EndGameCommandListener();
        new GameFreezeListener();
        new GameUnfreezeListener();

        new GameChangePhaseListener();
        new TeamChangeAliveListener();

    }

    @Override
    public void onDisable() {
        // so this doesnt accidentaly show up next time if the server was to crash
        ScoreBoardManager scoreBoardManager = new ScoreBoardManager("bedwars");
        scoreBoardManager.clearObjective();
        instance = null;
    }


    public boolean createGame() {
        LoadedMap selectedMap = LoadedMap.loadMapFromConfig();
        if (selectedMap == null) {
            Bukkit.getLogger().severe("We were not able to create a game.");
            return false;
        }
        game = new Game(selectedMap);
        Bukkit.getLogger().warning("Succesfully started a game.");
        return true;
    }

    public World getGameWorld() {
        return gameWorld;
    }

    public Game getGame() {
        return game;
    }
}

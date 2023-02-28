package eu.cosup.cores;

import eu.cosup.cores.listeners.*;
import eu.cosup.cores.listeners.custom.*;
import eu.cosup.cores.objects.LoadedMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
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
        getServer().getPluginManager().registerEvents(new PlaceOnCoralsListener(), this);
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
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new BeaconBreakListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantingTableListener(), this);
        getServer().getPluginManager().registerEvents(new DisableItemRenameListener(), this);
        getServer().getPluginManager().registerEvents(new ItemCraftListener(), this);

        new StartGameCommandListener();
        new EndGameCommandListener();
        new GameFreezeListener();
        new GameUnfreezeListener();

        new GameChangePhaseListener();
        new TeamChangeAliveListener();
    }

    @Override
    public void onDisable() {
        instance = null;
    }


    public boolean createGame() {
        LoadedMap selectedMap = LoadedMap.loadMapFromConfig();
        if (selectedMap == null) {
            Bukkit.getLogger().severe("We were not able to create a game.");
            return false;
        }
        game = new Game(selectedMap);
        gameWorld.getEntities().forEach(Entity::remove);
        Bukkit.getLogger().warning("Succesfully started a game.");
        return true;
    }

    public World getGameWorld() {
        return gameWorld;
    }

    public void setGameWorld(World gameWorld) {
        this.gameWorld = gameWorld;
    }

    public Game getGame() {
        return game;
    }
}

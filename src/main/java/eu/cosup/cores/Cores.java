package eu.cosup.cores;

import eu.cosup.cores.commands.ForceStartCommand;
import eu.cosup.cores.commands.SpectatorCommand;
import eu.cosup.cores.data.LoadedMap;
import eu.cosup.cores.listeners.*;
import eu.cosup.cores.managers.NameTagEditor;
import org.bukkit.*;
import org.bukkit.entity.Player;
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
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);

        // player quility
        getServer().getPluginManager().registerEvents(new HungerReceiveListener(), this);

        getServer().getPluginManager().registerEvents(new ItemCraftListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);

        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);



        // commands
        Objects.requireNonNull(getCommand("spectate")).setExecutor(new SpectatorCommand());
        Objects.requireNonNull(getCommand("forcestart")).setExecutor(new ForceStartCommand());


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

    @Override
    public void onDisable() {
        instance = null;
        for (Player player: Game.getGameInstance().getPlayerList()){
            NameTagEditor nameTagEditor = new NameTagEditor(player);
            nameTagEditor.setNameColor(ChatColor.RESET).setPrefix("").setTabName(player.getName());
        }
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
}

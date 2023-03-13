package eu.cosup.cores;

import eu.cosup.cores.core.data.LoadedMap;
import eu.cosup.cores.core.data.Team;
import eu.cosup.cores.core.data.TeamColor;
import eu.cosup.cores.listeners.*;
import eu.cosup.cores.listeners.blocks.BlockBreakListener;
import eu.cosup.cores.listeners.blocks.BlockPlaceListener;
import eu.cosup.cores.listeners.consumers.EndGameCommandListener;
import eu.cosup.cores.listeners.consumers.GameFreezeListener;
import eu.cosup.cores.listeners.consumers.GameUnfreezeListener;
import eu.cosup.cores.listeners.consumers.StartGameCommandListener;
import eu.cosup.cores.listeners.gamelogic.*;
import eu.cosup.cores.listeners.impl.GameChangePhaseListener;
import eu.cosup.cores.listeners.impl.TeamChangeAliveListener;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.tournament.common.utility.PlayerUtility;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;


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
        getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        getServer().getPluginManager().registerEvents(new HungerReceiveListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
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

    public void updateScoreboard(Player toSetScoreboard, Player toCheckPlayer) {
        if (game.getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            Team team = Game.getGameInstance().getTeamManager().whichTeam(toCheckPlayer.getUniqueId());
            if (team != null) {
                if (team.getColor().equals(TeamColor.BLUE)) {
                    Objects.requireNonNull(toSetScoreboard.getScoreboard().getTeam("0009Blue")).addEntry(toCheckPlayer.getName());
                } else if (team.getColor().equals(TeamColor.RED)) {
                    Objects.requireNonNull(toSetScoreboard.getScoreboard().getTeam("0010Red")).addEntry(toCheckPlayer.getName());
                }
                return;
            }
        }


        if (PlayerUtility.isPlayerRef(toCheckPlayer.getUniqueId(), toCheckPlayer.getName())) {
            Objects.requireNonNull(toSetScoreboard.getScoreboard().getTeam("0001Referee")).addEntry(toCheckPlayer.getName());
        } else if (PlayerUtility.isPlayerCommentator(toCheckPlayer.getUniqueId(), toCheckPlayer.getName())) {
            Objects.requireNonNull(toSetScoreboard.getScoreboard().getTeam("0005Commentator")).addEntry(toCheckPlayer.getName());
        } else if (PlayerUtility.isPlayerStreamer(toCheckPlayer.getUniqueId(), toCheckPlayer.getName())) {
            Objects.requireNonNull(toSetScoreboard.getScoreboard().getTeam("0006Streamer")).addEntry(toCheckPlayer.getName());
        } else if (PlayerUtility.isPlayerStaff(toCheckPlayer.getUniqueId(), toCheckPlayer.getName())) {
            Objects.requireNonNull(toSetScoreboard.getScoreboard().getTeam("0000Staff")).addEntry(toCheckPlayer.getName());
        } else {
            Objects.requireNonNull(toSetScoreboard.getScoreboard().getTeam("0015Player")).addEntry(toCheckPlayer.getName());
        }
    }
}

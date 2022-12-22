package eu.cosup.cores;

import eu.cosup.cores.listeners.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.datatransfer.Clipboard;
import java.util.ArrayList;

public final class Cores extends JavaPlugin {

    private static Cores instance;

    public static Cores getInstance() {
        return instance;
    }

    private ArrayList<Clipboard> loadedMaps = new ArrayList<>();
    private Game game;

    @Override
    public void onEnable() {
        instance = this;


        reloadConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();

        loadMaps();

        if (loadedMaps.size() == 0) {
            getLogger().severe("No schematics found.");
            // TODO return bellow
            //return;
        }

        game = new Game();


        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public Game getGame() {
        return game;
    }

    public ArrayList<Clipboard> getLoadedMaps() {
        return loadedMaps;
    }

    private void loadMaps() {

        //LoadedMap matchMap = new LoadedMap(name, );
        // TODO load maps

        }
}

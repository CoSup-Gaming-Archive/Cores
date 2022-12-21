package eu.cosup.cores;

import org.bukkit.plugin.java.JavaPlugin;

public final class Cores extends JavaPlugin {

    private static Cores instance;

    public static Cores getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}

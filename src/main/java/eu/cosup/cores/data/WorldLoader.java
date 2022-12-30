package eu.cosup.cores.data;

import eu.cosup.cores.Cores;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.util.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class WorldLoader {


    // returns false when world failed to load and returns true if it succeeded.
    public static boolean loadNewWorld(String name) {

        // this is a very brute force way and there is probably a better way of doing this.

        Bukkit.unloadWorld("world", true);

        try {
            FileUtils.forceDelete(new File("world"));
        } catch (IOException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Couldnt delete directory");
        }

        try {
            FileUtils.copyDirectory(new File("plugins/Cores/maps/" + name), new File("world"));
        } catch (IOException exception) {
            Bukkit.getLogger().severe("Couldnt copy the folder from maps directory now you should probably worry because this is really bad.");
            exception.printStackTrace();
            return false;
        }

        World world = Bukkit.createWorld(new WorldCreator("world").environment(World.Environment.CUSTOM));
        Cores.getInstance().setGameWorld(world);

        Bukkit.getLogger().info("Loaded: " + name);
        return true;
    }

    public static ArrayList<String> getWorldNames() {

        ArrayList<String> mapNames = new ArrayList<>();

        File mapFolder = new File(Cores.getInstance().getDataFolder(), "maps");

        // make sure the was a folder called maps

        if (mapFolder.mkdir()) {
            Bukkit.getLogger().warning("Created a folder for maps");
        }

        File[] mapFiles = mapFolder.listFiles();

        if (mapFiles == null) {
            Bukkit.getLogger().severe("There are no maps in map folder");
            return null;
        }

        for (File mapFile : mapFiles) {
            if (mapFile.isDirectory()) {
                if (!mapNames.contains(mapFile.getName())) {
                    mapNames.add(mapFile.getName());
                }
            }
        }

        return mapNames;
    }

}

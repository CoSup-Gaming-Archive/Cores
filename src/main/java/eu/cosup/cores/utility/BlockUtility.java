package eu.cosup.cores.utility;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.List;

public class BlockUtility {

    private static final List<String> blockWhitelist = Cores.getInstance().getConfig().getStringList("block-whitelist");

    public static boolean blockWhitelisted(Material material) {

        for (String materialString : blockWhitelist) {

            Material testMaterial = Material.getMaterial(materialString.toUpperCase());

            if (testMaterial == material) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLocationProtected(Location location) {

        for (Location teamSpawn : Game.getGameInstance().getSelectedMap().getTeamSpawns()) {

            if (teamSpawn.distance(location) < Cores.getInstance().getConfig().getDouble("spawn-protection-distance")) {
                return true;
            }
        }

        // players cannot place one block above beacon so this should fix it.
        for (Location beaconLocation : Game.getGameInstance().getSelectedMap().getAllBeaconLocations()) {

            // this means you cannot place above beacon so you dont block the beam
            if (location.getBlockX() == beaconLocation.getBlockX()) {
                if (location.getZ() == location.getZ()) {
                    if (location.getY() >= location.getY()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

package eu.cosup.cores.core.utility;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockUtility {
    private static final double spawnProtectionDistance = Cores.getInstance().getConfig().getDouble("spawn-protection-distance");

    public static boolean isLocationProtected(@NotNull Location location) {

        if (Game.getGameInstance().getGameStateManager().getGamePhase() == GameStateManager.GamePhase.ARENA) {
            return !Game.getGameInstance().getBlockManager().isBlockPlaced(location.getBlock());
        }

        for (Location teamSpawn : Game.getGameInstance().getSelectedMap().getTeamSpawns().values()) {
            if (teamSpawn.distance(location) < spawnProtectionDistance) {
                return true;
            }
        }

        for (Pair<Location, Location> teamBeaconLocations : Game.getGameInstance().getSelectedMap().getTeamBeacons().values()) {


            // above beacon is not breakable
            if (teamBeaconLocations.left().getBlockX() == location.getBlockX()) {
                if (teamBeaconLocations.left().getBlockZ() == location.getBlockZ()) {
                    return teamBeaconLocations.left().distance(location) < 10;
                }
            }
            if (teamBeaconLocations.right().getBlockX() == location.getBlockX()) {
                if (teamBeaconLocations.right().getBlockZ() == location.getBlockZ()) {
                    return teamBeaconLocations.left().distance(location) < 10;
                }
            }

            // around the beacon and one level bellow is not breakable
            if (teamBeaconLocations.left().getBlockY() == location.getBlockY() || teamBeaconLocations.left().getBlockY() == location.getBlockY() + 1) {
                if (teamBeaconLocations.left().distance(location) < 3) {
                    return true;
                }
            }
            if (teamBeaconLocations.right().getBlockY() == location.getBlockY() || teamBeaconLocations.right().getBlockY() == location.getBlockY() + 1) {
                if (teamBeaconLocations.right().distance(location) < 3) {
                    return true;
                }
            }
        }
        
        return false;
    }

    public static boolean isBlacklistedFromCrafting(@NotNull Material material) {
        // TODO: ik its tedious but i  asked you to make a whitelist, not a blacklist
        return material.toString().contains("BOAT") || material.toString().contains("MINECART") || material.toString().contains("HOPPER") || material.toString().contains("DYE") || material.toString().contains("RAIL");
    }

    public static boolean shouldDropItem(@NotNull Block block) {
        if (Game.getGameInstance().getBlockManager().isBlockPlaced(block)) {
            return true;
        }

        List<Material> allowedBlocks = List.of(
            Material.IRON_BLOCK,
            Material.DIAMOND_BLOCK
        );

        // TODO: WRROOOONNNGG, do not drop if spruce, the trees on the maps are out of spruce
        // old:  'return allowedBlocks.contains(block.getType()) || block.getType().toString().contains("SPRUCE") || block.getType().toString().contains("OAK");'
        // new:
        return allowedBlocks.contains(block.getType()) || block.getType().toString().contains("OAK");
    }

    // TODO: the only usage for this is in entity explode listener which is possibly scheduled for removal
    public static RayTraceResult rayTrace(Location start, Vector direction, double maxDistance, ArrayList<Material> targetBlocks) {
        if (direction.lengthSquared() < 1e-5 || maxDistance <= 1e-5) return null;

        // Look for block collisions
        RayTraceResult blockRayTrace = null;

        BlockIterator bIterator = new BlockIterator(start.getWorld(), start.toVector(), direction, 0, (int) Math.ceil(maxDistance));

        while (bIterator.hasNext()) {

            Block block = bIterator.next();
            // First perform a rough collision check with BlockIterator
            if (targetBlocks.contains(block.getType()) && !block.isEmpty()) {

                // Now check if the collision still occurs with the precise collision geometry of the block
                RayTraceResult res = block.rayTrace(start, direction, maxDistance, FluidCollisionMode.ALWAYS);
                if (res != null) {
                    blockRayTrace = res;
                    break;
                }

            }

        }
        // Return closest RayTraceResult
        return blockRayTrace;
    }
}

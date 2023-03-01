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
            return Game.getGameInstance().getBlockManager().isBlockPlaced(location.getBlock());
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
        return material.toString().contains("BOAT") || material.toString().contains("MINECART") || material.toString().contains("HOPPER") || material.toString().contains("DYE") || material.toString().contains("RAIL");
    }

    public static boolean isWhitelistedCrafting(@NotNull Material material) {
        List<Material> allowedBlocks = List.of(
            Material.STICK,
            Material.CRAFTING_TABLE,
            Material.IRON_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.OAK_PLANKS,
            Material.OAK_LOG,
            Material.OAK_WOOD,
            Material.OAK_SLAB,
            Material.OAK_STAIRS,
            Material.OAK_FENCE,
            Material.OAK_FENCE_GATE,
            Material.OAK_LEAVES,
            Material.OAK_SAPLING,
            Material.OAK_SIGN,
            Material.OAK_WALL_SIGN,
            Material.OAK_BUTTON,
            Material.OAK_PRESSURE_PLATE,
            Material.OAK_TRAPDOOR,
            Material.OAK_DOOR,
            Material.OAK_BOAT,
            Material.WOODEN_AXE,
            Material.WOODEN_HOE,
            Material.WOODEN_PICKAXE,
            Material.WOODEN_SHOVEL,
            Material.WOODEN_SWORD,
            Material.IRON_AXE,
            Material.IRON_HOE,
            Material.IRON_PICKAXE,
            Material.IRON_SHOVEL,
            Material.IRON_SWORD,
            Material.IRON_BOOTS,
            Material.IRON_CHESTPLATE,
            Material.IRON_HELMET,
            Material.IRON_LEGGINGS,
            Material.IRON_INGOT,
            Material.IRON_NUGGET,
            Material.DIAMOND_AXE,
            Material.DIAMOND_HOE,
            Material.DIAMOND_PICKAXE,
            Material.DIAMOND_SWORD,
            Material.DIAMOND_BOOTS,
            Material.DIAMOND_CHESTPLATE,
            Material.DIAMOND_HELMET,
            Material.DIAMOND_LEGGINGS,
            Material.DIAMOND,
            Material.DIAMOND_SHOVEL
        );

        return allowedBlocks.contains(material);
    }

    public static boolean shouldDropItem(@NotNull Block block) {
        if (Game.getGameInstance().getBlockManager().isBlockPlaced(block)) {
            return true;
        }

        List<Material> allowedBlocks = List.of(
            Material.IRON_BLOCK,
            Material.DIAMOND_BLOCK
        );

        return allowedBlocks.contains(block.getType()) || block.getType().toString().contains("OAK");
    }
}

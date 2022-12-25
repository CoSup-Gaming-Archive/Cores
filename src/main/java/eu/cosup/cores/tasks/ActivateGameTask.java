package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;

public class ActivateGameTask extends BukkitRunnable {

    private ArrayList<Player> joinedPlayers;

    public ActivateGameTask(ArrayList<Player> joinedPlayers) {

        this.joinedPlayers = joinedPlayers;

    }

    @Override
    public void run() {

        prepareEnviroment();
        preparePlayers();
        spawnBeacons();
    }

    private void prepareEnviroment() {

        Cores.getInstance().getWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Cores.getInstance().getWorld().setGameRule(GameRule.DO_MOB_SPAWNING, false);

        // im pretty sure this is right
        Cores.getInstance().getWorld().setGameRule(GameRule.NATURAL_REGENERATION, false);

        // qol for builders
        Cores.getInstance().getWorld().setGameRule(GameRule.DO_FIRE_TICK, false);

    }

    private void preparePlayers() {
        for (Player player : joinedPlayers) {
            preparePlayerFull(player);
        }
    }

    // ooo so juicy
    public static void preparePlayerFull(Player player) {

        preparePlayerStats(player);
        givePlayerArmor(player);
        givePlayerTools(player);
        teleportPlayerToSpawn(player);

    }

    // prepare player stats
    public static void preparePlayerStats(Player player) {
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(Integer.MAX_VALUE);
        player.setHealth(20);
    }

    public static void teleportPlayerToSpawn(Player player) {
        player.teleport(Game.getGameInstance().getSelectedMap().getSpawnByPlayer(player));
    }

    public static void givePlayerArmor(Player player) {

        // TODO give player colored armor

        List<String> armorPeaces = Cores.getInstance().getConfig().getStringList("armor");

        for (String armorPeaceName : armorPeaces) {

            ItemStack armorPeace = new ItemStack(Material.getMaterial(armorPeaceName));
            ItemMeta meta = armorPeace.hasItemMeta() ? armorPeace.getItemMeta() : Bukkit.getItemFactory().getItemMeta(armorPeace.getType());
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            // from Color:
            leatherArmorMeta.setColor(TeamColor.getColor(Game.getGameInstance().getTeamManager().whichTeam(player)));
            armorPeace.setItemMeta(leatherArmorMeta);

            // cheeky way but maybe there is a better method
            // TODO maybe better method
            if (armorPeaceName.toLowerCase().contains("helmet")) {
                player.getInventory().setHelmet(armorPeace);
            }


            if (armorPeaceName.toLowerCase().contains("chestplate")) {
                player.getInventory().setChestplate(armorPeace);
            }


            if (armorPeaceName.toLowerCase().contains("leggings")) {
                player.getInventory().setLeggings(armorPeace);
            }


            if (armorPeaceName.toLowerCase().contains("boots")) {
                player.getInventory().setBoots(armorPeace);
            }
        }
    }

    public static void givePlayerTools(Player player) {

        int i = 0;
        for (String itemName : Cores.getInstance().getConfig().getConfigurationSection("hotbar").getKeys(false)) {

            // this is ugly
            // TODO make this less ugly
            player.getInventory().setItem(i, new ItemStack(Material.getMaterial(itemName), Cores.getInstance().getConfig().getConfigurationSection("hotbar").getInt(itemName)));

            i++;
        }
    }

    public static boolean isItemDefault(Material item) {

        for (String itemName : Cores.getInstance().getConfig().getConfigurationSection("hotbar").getKeys(false)) {

            if (item == Material.getMaterial(itemName)) {
                return true;
            }
        }

        for (String inventoryItem : Cores.getInstance().getConfig().getStringList("armor")) {

            if (Material.getMaterial(inventoryItem) == item) {
                return true;
            }
        }
        return false;
    }

    private void spawnBeacons() {

        // TODO make this work

        /*
        Field field=net.minecraft.server.Block.class.getDeclaredField("strength");
        field.setAccessible(true);
        field.setFloat(net.minecraft.server.Block.BED, 50.0F);
         */

        for (Location location : Game.getGameInstance().getSelectedMap().getTeamBlueBeacons()) {
            Cores.getInstance().getWorld().setBlockData(location, Material.BEACON.createBlockData());
        }

        for (Location location : Game.getGameInstance().getSelectedMap().getTeamRedBeacons()) {
            Cores.getInstance().getWorld().setBlockData(location, Material.BEACON.createBlockData());
        }

        Bukkit.getLogger().info("Spawned beacons");
    }
}

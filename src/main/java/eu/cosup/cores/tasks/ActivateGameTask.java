package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.BeaconInformation;
import eu.cosup.cores.managers.NameTagEditor;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.*;

public class ActivateGameTask extends BukkitRunnable {

    private ArrayList<Player> joinedPlayers;

    private static final List<String> armorPeaces = Cores.getInstance().getConfig().getStringList("armor");
    private static final ConfigurationSection hotbar = Cores.getInstance().getConfig().getConfigurationSection("hotbar");

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
        
        Cores cores = Cores.getInstance();

        cores.getGameWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        cores.getGameWorld().setGameRule(GameRule.DO_MOB_SPAWNING, false);
        cores.getGameWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        cores.getGameWorld().setGameRule(GameRule.DO_WEATHER_CYCLE, false);

        // im pretty sure this is right
        cores.getGameWorld().setGameRule(GameRule.NATURAL_REGENERATION, false);


        // qol for builders
        cores.getGameWorld().setGameRule(GameRule.DO_FIRE_TICK, false);


    }

    private void preparePlayers() {
        for (Player player : joinedPlayers) {
            preparePlayerFull(player);
        }
    }

    // ooo so juicy
    public static void preparePlayerFull(Player player) {
        TeamColor teamColor = Game.getGameInstance().getTeamManager().whichTeam(player);
        NameTagEditor nameTagEditor = new NameTagEditor(player);
        nameTagEditor.setNameColor(TeamColor.getChatColor(teamColor)).setPrefix(teamColor.toString()+" ").setTabName(TeamColor.getChatColor(teamColor)+player.getName()).setChatName((TeamColor.getChatColor(teamColor)+player.getName()));
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

        for (String armorPeaceName : armorPeaces) {

            ItemStack armorPeace = new ItemStack(Material.getMaterial(armorPeaceName));
            ItemMeta meta = armorPeace.hasItemMeta() ? armorPeace.getItemMeta() : Bukkit.getItemFactory().getItemMeta(armorPeace.getType());
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
            // from Color:
            leatherArmorMeta.setColor(TeamColor.getColor(Game.getGameInstance().getTeamManager().whichTeam(player)));
            armorPeace.setItemMeta(leatherArmorMeta);

            // cheeky way but maybe there is a better method
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
        for (String itemName : hotbar.getKeys(false)) {

            // this is ugly
            // TODO make this less ugly
            player.getInventory().setItem(i, new ItemStack(Material.getMaterial(itemName), hotbar.getInt(itemName)));

            i++;
        }
    }

    public static boolean isItemDefault(Material item) {
        Cores cores = Cores.getInstance();

        for (String itemName : hotbar.getKeys(false)) {

            if (item == Material.getMaterial(itemName)) {
                return true;
            }
        }

        for (String inventoryItem : armorPeaces) {

            if (Material.getMaterial(inventoryItem) == item) {
                return true;
            }
        }
        return false;
    }

    private void spawnBeacons() {

        Cores cores = Cores.getInstance();

        BlockData beaconBlock = Material.BEACON.createBlockData();

        for (Location location : Game.getGameInstance().getSelectedMap().getTeamBlueBeacons()) {
            cores.getGameWorld().setBlockData(location, beaconBlock);
        }

        for (Location location : Game.getGameInstance().getSelectedMap().getTeamRedBeacons()) {
            cores.getGameWorld().setBlockData(location, beaconBlock);
        }

        Bukkit.getLogger().info("Spawned beacons");
        BeaconInformation.update();
    }
}

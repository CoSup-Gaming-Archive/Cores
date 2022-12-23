package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.ItemList;

import java.util.ArrayList;
import java.util.List;

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

        TeamColor teamColor = Game.getGameInstance().getTeamManager().whichTeam(player);

        if (teamColor == TeamColor.RED) {
            player.teleport(Game.getGameInstance().getSelectedMap().getTeamRedSpawns());
        }

        if (teamColor == TeamColor.BLUE) {
            player.teleport(Game.getGameInstance().getSelectedMap().getTeamBlueSpawns());
        }

    }

    public static void givePlayerArmor(Player player) {

        // TODO give player colored armor

        List<String> armorPeaces = Cores.getInstance().getConfig().getStringList("armor");

        // oooof this could print many error nicht gut
        player.getInventory().setBoots(new ItemStack(Material.getMaterial(armorPeaces.get(0))));
        player.getInventory().setLeggings(new ItemStack(Material.getMaterial(armorPeaces.get(1))));
        player.getInventory().setChestplate(new ItemStack(Material.getMaterial(armorPeaces.get(2))));
        player.getInventory().setHelmet(new ItemStack(Material.getMaterial(armorPeaces.get(3))));
    }

    public static void givePlayerTools(Player player) {

        // TODO make give player items in inventory

        for (String inventoryItem : Cores.getInstance().getConfig().getStringList("hotbar")) {

            player.getInventory().setItem(0, new ItemStack(Material.getMaterial(inventoryItem)));
        }
    }

    public static boolean isItemDefault(Material item) {

        for (String inventoryItem : Cores.getInstance().getConfig().getStringList("hotbar")) {

            if (Material.getMaterial(inventoryItem) == item) {
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

        for (Location location : Game.getGameInstance().getSelectedMap().getTeamBlueBeacons()) {
            Cores.getInstance().getWorld().setBlockData(location, Material.BEACON.createBlockData());
        }

        for (Location location : Game.getGameInstance().getSelectedMap().getTeamRedBeacons()) {
            Cores.getInstance().getWorld().setBlockData(location, Material.BEACON.createBlockData());
        }

        Bukkit.getLogger().info("Spawned beacons");
    }
}

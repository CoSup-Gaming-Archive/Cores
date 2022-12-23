package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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

        preparePlayers();
        teleportPlayersToSpawns();
        spawnBeacons();

        // give players armor and stuff

        givePlayerArmor();
        givePlayerTools();
    }

    private void preparePlayers() {

        for (Player player : joinedPlayers) {
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
        }

    }

    private void givePlayerArmor() {

        // TODO give colored armor peaces

        for (Player player : joinedPlayers) {

            List<String> armorPeaces = Cores.getInstance().getConfig().getStringList("armor");

            // oooof this could print many error nicht gut
            player.getInventory().setBoots(new ItemStack(Material.getMaterial(armorPeaces.get(0))));
            player.getInventory().setLeggings(new ItemStack(Material.getMaterial(armorPeaces.get(1))));
            player.getInventory().setChestplate(new ItemStack(Material.getMaterial(armorPeaces.get(2))));
            player.getInventory().setHelmet(new ItemStack(Material.getMaterial(armorPeaces.get(3))));
        }
    }

    private void givePlayerTools() {

        // TODO make give player items in inventory

        for (Player player : joinedPlayers) {

            // this could definetely go wrtong
            for (String inventoryItem : Cores.getInstance().getConfig().getStringList("hotbar")) {

                player.getInventory().setItem(0, new ItemStack(Material.getMaterial(inventoryItem)));
            }
        }
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

    private void teleportPlayersToSpawns() {
        for (Player player : joinedPlayers) {

            TeamColor teamColor = Game.getGameInstance().getTeamManager().whichTeam(player);

            if (teamColor == TeamColor.RED) {
                player.teleport(Game.getGameInstance().getSelectedMap().getTeamRedSpawns());
            }

            if (teamColor == TeamColor.BLUE) {
                player.teleport(Game.getGameInstance().getSelectedMap().getTeamBlueSpawns());
            }

        }
    }

}

package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.objects.Team;
import eu.cosup.cores.objects.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class ActivateGameTask extends BukkitRunnable {

    public ActivateGameTask() {
    }

    public static void prepareEnviroment() {

        Cores.getInstance().getGameWorld().setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        Cores.getInstance().getGameWorld().setGameRule(GameRule.DO_MOB_SPAWNING, false);
        Cores.getInstance().getGameWorld().setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Cores.getInstance().getGameWorld().setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        Cores.getInstance().getGameWorld().setStorm(false);

        // im pretty sure this is right
        Cores.getInstance().getGameWorld().setGameRule(GameRule.NATURAL_REGENERATION, true);


        // qol for builders
        Cores.getInstance().getGameWorld().setGameRule(GameRule.DO_FIRE_TICK, false);


    }

    // ooo so juicy
    public static void preparePlayerFull(@NotNull Player player) {
        player.getInventory().clear();
        preparePlayerStats(player);
        givePlayerArmor(player);
        teleportPlayerToSpawn(player);
        Game.getGameInstance().updatePlayersNameTag(player);
        givePlayerTools(player);
    }

    // prepare player stats
    public static void preparePlayerStats(@NotNull Player player) {
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(Integer.MAX_VALUE);
        player.setHealth(20);
    }

    public static void teleportPlayerToSpawn(@NotNull Player player) {
        player.teleport(Game.getGameInstance().getSelectedMap().getSpawnByPlayer(player));
    }

    public static void givePlayerArmor(@NotNull Player player) {

        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        addColor(leggings, player);
        addColor(boots, player);

        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);

        addColor(helmet, player);
        addColor(chestplate, player);

        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(leggings);
        player.getInventory().setBoots(boots);
    }

    private static void addColor(@NotNull ItemStack armorPeace, @NotNull Player player) {
        ItemMeta meta = armorPeace.hasItemMeta() ? armorPeace.getItemMeta() : Bukkit.getItemFactory().getItemMeta(armorPeace.getType());
        LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) meta;
        // from Color:
        leatherArmorMeta.setColor(TeamColor.getColor(Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor()));
        leatherArmorMeta.setUnbreakable(true);
        armorPeace.setItemMeta(leatherArmorMeta);
    }

    public static void givePlayerTools(@NotNull Player player) {
        player.getInventory().addItem(new ItemStack(Material.STONE_SWORD));
        player.getInventory().addItem(new ItemStack(Material.BOW));
        player.getInventory().addItem(new ItemStack(Material.IRON_AXE));
        player.getInventory().addItem(new ItemStack(Material.OAK_LOG, 32));
        player.getInventory().addItem(new ItemStack(Material.OAK_PLANKS, 64));
        player.getInventory().addItem(new ItemStack(Material.OAK_PLANKS, 64));
        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 8));
        player.getInventory().addItem(new ItemStack(Material.ARROW, 12));
        player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE));
    }

    @Override
    public void run() {
        prepareEnviroment();
        preparePlayers();
    }

    private void preparePlayers() {
        for (Team team : Game.getGameInstance().getTeamManager().getTeams()) {
            team.getPlayers().forEach(ActivateGameTask::preparePlayerFull);
        }
    }
}

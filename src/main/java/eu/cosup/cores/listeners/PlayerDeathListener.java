package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.PlayerDamageManager;
import eu.cosup.cores.tasks.SpectatorTask;
import eu.cosup.cores.utility.ColorUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;


public class PlayerDeathListener implements Listener {

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getPlayer();

        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack != null) {
                player.getInventory().setItem(EquipmentSlot.HAND, itemStack);
                player.dropItem(true);
                player.getInventory().removeItem(itemStack);
            }
        }

        event.setCancelled(true);

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {

            // after game ends and stuff
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            event.getPlayer().teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());
            return;
        }

        Player killer = PlayerDamageManager.getPlayerLastDamage(event.getPlayer());

        TextComponent.Builder killerText = Component.text().content(player.getName()).color(ColorUtility.getStdTextColor(Game.getGameInstance().getTeamManager().whichTeam(player).toString()));

        // that means player was not damaged by other players
        if (killer == null) {
            killerText.append(Component.text().content(" committed suicide").color(ColorUtility.getStdTextColor("yellow")));
        } else {
            killerText
            .append(Component.text().content(" was killed by ").color(ColorUtility.getStdTextColor("yellow")))
            .append(Component.text().content(killer.getName()).color(ColorUtility.getStdTextColor(Game.getGameInstance().getTeamManager().whichTeam((Player) killer).toString())));
            PlayerDamageManager.setPlayerLastDamage(event.getPlayer(), null);
        }

        Cores.getInstance().getServer().sendMessage(killerText);

        new SpectatorTask(event.getPlayer()).runTask(Cores.getInstance());
    }
}

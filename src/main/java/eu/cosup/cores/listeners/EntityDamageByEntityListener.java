package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.PlayerDamageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    private void onEntityDamageEntity(@NotNull EntityDamageByEntityEvent event) {

        if (event.getEntity().getType() == EntityType.ARMOR_STAND) {
            event.setCancelled(true);
            return;
        }

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getDamager() instanceof Player damager)) {
            return;
        }

        if (!(event.getEntity() instanceof Player damaged)) {
            return;
        }

        // creative players can hit anyone
        if (damager.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (Game.getGameInstance().getTeamManager().whichTeam(damaged.getUniqueId()) == null || Game.getGameInstance().getTeamManager().whichTeam(damaged.getUniqueId()) == null) {
            return;
        }

        if (Game.getGameInstance().getTeamManager().whichTeam(damaged.getUniqueId()).getColor() ==
                Game.getGameInstance().getTeamManager().whichTeam(damager.getUniqueId()).getColor()) {

            event.setCancelled(true);
            return;
        }

        if (event.getEntity() instanceof Player player) {

            if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                event.setDamage(0);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }

        }

        if (damaged.getHealth() == 0) {
            if (((Player) event.getDamager()).getInventory().getItemInMainHand().getType() == Material.BOW) {
                damager.getInventory().addItem(new ItemStack(Material.ARROW, 4));
            }
        }
        PlayerDamageManager.setPlayerLastDamage(damaged, damager);
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager(); // Get the object that damaged the entity
        if (damager.getType() == EntityType.ARROW) { // Check if the object is an Arrow
            Arrow arrow = (Arrow) damager; // Cast the damager to the arrow
            if (arrow.getShooter() instanceof Player shooter) { // Check if the object that shot the arrow was a player
                if (
                        Game.getGameInstance().getTeamManager().whichTeam(shooter.getUniqueId()) ==
                        Game.getGameInstance().getTeamManager().whichTeam(event.getEntity().getUniqueId())
                    // 3/1/2023 nope because the event would not be cancelled then so it works as intended
                ) {
                    event.setCancelled(true);
                }
            }
        }
    }
}

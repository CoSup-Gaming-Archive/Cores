package eu.cosup.cores.listeners;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.PlayerDamageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    private void onEntityDamageEntity(@NotNull EntityDamageByEntityEvent event) {

        if (event.getDamager().getType() == EntityType.PRIMED_TNT) {
            // we want less damage from tnt
            event.setDamage(event.getDamage()/8);
        }

        if (event.getEntity().getType() == EntityType.WANDERING_TRADER || event.getEntity().getType() == EntityType.VILLAGER) {
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

            damager.sendMessage(Component.text().content("You cannot damage teammates").color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }

        if (event.getEntity() instanceof Player player) {

            if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                event.setDamage(0);
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
            }

        }
        PlayerDamageManager.setPlayerLastDamage((Player) event.getEntity(), (Player) event.getDamager());
    }
}

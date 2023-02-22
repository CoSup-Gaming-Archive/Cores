package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.PlayerDamageManager;
import eu.cosup.cores.objects.TeamColor;
import eu.cosup.cores.tasks.SpectatorTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;


public class PlayerDeathListener implements Listener {

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getPlayer();

        for (ItemStack itemStack : player.getInventory()) {
            if (itemStack != null) {
                player.getInventory().removeItem(itemStack);
            }
        }

        event.setCancelled(true);

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {

            new SpectatorTask(event.getPlayer(), false);

            return;
        }

        Player killer = PlayerDamageManager.getPlayerLastDamage(event.getPlayer());

        if (killer != null) {
            // same as before no null pointer exeptions
            if (Game.getGameInstance().getTeamManager().whichTeam(killer.getUniqueId()) == null) {
                killer = null;
            }
        }

        // if
        if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()) == null) {
            return;
        }

        Bukkit.getLogger().info(event.getPlayer().getName());

        TextComponent.Builder killerText = Component.text().content(player.getName())
                .color(TeamColor.getNamedTextColor(Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getColor()));

        // that means player was not damaged by other players
        if (killer == null) {
            killerText.append(Component.text().content(" committed suicide").color(NamedTextColor.YELLOW));
        } else {
            killer.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 200f, 2f);
            killerText
                    .append(Component.text().content(" was killed by ").color(NamedTextColor.YELLOW))
                    .append(Component.text().content(killer.getName()).color(TeamColor.getNamedTextColor(Game.getGameInstance().getTeamManager().whichTeam(killer.getUniqueId()).getColor())));
            PlayerDamageManager.setPlayerLastDamage(event.getPlayer(), null);
        }

        if (!Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).isAlive()) {
            killerText.append(Component.text().content(" FINAL KILL").decorate(TextDecoration.BOLD).color(NamedTextColor.AQUA));

            new SpectatorTask(event.getPlayer(), false).runTask(Cores.getInstance());

            if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()) != null) {
                Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).setPlayerDead(player, true);
            }

            Cores.getInstance().getServer().sendMessage(killerText);

            if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).getAlivePlayers().size() == 0) {
                Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.ENDING);
            }
            return;
        }

        Cores.getInstance().getServer().sendMessage(killerText);

        new SpectatorTask(event.getPlayer(), true).runTask(Cores.getInstance());
    }
}

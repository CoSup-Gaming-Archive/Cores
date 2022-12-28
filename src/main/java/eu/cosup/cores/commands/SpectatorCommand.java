package eu.cosup.cores.commands;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.utility.ColorUtility;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpectatorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            Component msg = Component.text().content("only player please").build();
            sender.sendMessage(msg);
            return false;
        }

        if (Game.getGameInstance() == null) {
            Component msg = Component.text().content("There is no game happening").color(ColorUtility.getStdTextColor("yellow")).build();
            player.sendMessage(msg);
            return true;
        }

        // creative players can change team
        if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            Component msg = Component.text().content("You cannot become spectator mid game").color(ColorUtility.getStdTextColor("red")).build();
            player.sendMessage(msg);
            return true;
        }

        if (!Game.getGameInstance().getJoinedPlayers().contains(player)) {

            Component msg = Component.text().content("You joined the game").color(ColorUtility.getStdTextColor("yellow")).build();
            player.sendMessage(msg);
            Game.getGameInstance().getJoinedPlayers().add(player);
            Game.getGameInstance().refreshPlayerCount();
            return true;
        }

        Game.getGameInstance().getJoinedPlayers().remove(player);
        Game.getGameInstance().refreshPlayerCount();
        Component msg = Component.text().content("You are now a spectator").color(ColorUtility.getStdTextColor("gray")).build();
        player.sendMessage(msg);

        return true;
    }

}

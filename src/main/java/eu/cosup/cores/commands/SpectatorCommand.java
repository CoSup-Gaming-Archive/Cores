package eu.cosup.cores.commands;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
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
            sender.sendMessage("only player please");
            return false;
        }

        if (Game.getGameInstance() == null) {
            player.sendMessage("There is no game happening");
            return true;
        }

        // creative players can change team
        if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            player.sendMessage("You cannot change to spectator mid game");
            return true;
        }

        if (!Game.getGameInstance().getJoinedPlayers().contains(player)) {

            player.sendMessage(ChatColor.YELLOW + "You joined the game.");
            Game.getGameInstance().getJoinedPlayers().add(player);
            Game.getGameInstance().refreshPlayerCount();
            return true;
        }

        Game.getGameInstance().getJoinedPlayers().remove(player);
        Game.getGameInstance().refreshPlayerCount();
        player.sendMessage(ChatColor.GRAY + "You are now a spectator");

        return true;
    }

}

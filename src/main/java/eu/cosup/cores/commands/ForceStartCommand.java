package eu.cosup.cores.commands;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.tasks.StartCountdownTask;
import eu.cosup.cores.utility.ColorUtility;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ForceStartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.JOINING) {
            sender.sendMessage("You can only force when players are joining.");
            return true;
        }

        new StartCountdownTask().runTask(Cores.getInstance());
        Cores.getInstance().getServer().broadcast(Component.text(sender.getName()+" issued a force start.").color(ColorUtility.getStdTextColor("red")));

        return true;
    }

}

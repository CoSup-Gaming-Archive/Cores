package eu.cosup.cores.listeners.consumers;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.tasks.GameTimerTask;
import eu.cosup.tournament.server.commands.controls.UnFreezeGameCommand;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

public class GameUnfreezeListener implements Consumer<CommandSender> {

    public GameUnfreezeListener() {
        UnFreezeGameCommand.registerConsumer(this);
    }

    @Override
    public void accept(CommandSender commandSender) {
        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            return;
        }

        new GameTimerTask().runTask(Cores.getInstance());
    }
}

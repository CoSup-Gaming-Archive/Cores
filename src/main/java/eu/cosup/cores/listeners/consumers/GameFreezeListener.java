package eu.cosup.cores.listeners.consumers;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.tasks.GameTimerTask;
import eu.cosup.tournament.server.commands.controls.FreezeGameCommand;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;

public class GameFreezeListener implements Consumer<CommandSender> {

    public GameFreezeListener() {
        FreezeGameCommand.registerConsumer(this);
    }

    @Override
    public void accept(CommandSender commandSender) {
        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            return;
        }

        if (GameTimerTask.getInstance() != null) {
            GameTimerTask.getInstance().cancelTimer();
        }
    }
}

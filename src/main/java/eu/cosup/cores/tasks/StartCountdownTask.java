package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import org.bukkit.scheduler.BukkitRunnable;

public class StartCountdownTask extends BukkitRunnable {

    private Game game;
    private int count;

    public StartCountdownTask(Game game) {
        this.game = game;

        count = Cores.getInstance().getConfig().getInt("start-countdown");
    }

    @Override
    public void run() {

        game.getGameStateManager().setGameState(GameStateManager.GameState.STARTING);

        // ewww this ugly and no timeout
        while (true) {
            if (count <= 0) {
                game.getGameStateManager().setGameState(GameStateManager.GameState.ACTIVE);
                cancel();
                break;
            }

            count--;
            Cores.getInstance().getServer().broadcastMessage("Starting in "+(count+1));

        }

    }
}

package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class StartCountdownTask extends BukkitRunnable {

    private Game game;
    private int startCountdown;

    public StartCountdownTask(Game game) {
        this.game = game;

        startCountdown = Cores.getInstance().getConfig().getInt("start-countdown");

        if (startCountdown <= 0) {
            Bukkit.getLogger().severe("Start countdown cannot be that low");
            startCountdown = 10;
        }

    }

    @Override
    public void run() {

        game.getGameStateManager().setGameState(GameStateManager.GameState.STARTING);

        // ewww this ugly and no timeout

        for (int i = 0; i <= startCountdown; i++) {

            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {

                    // at the alst second
                    if (finalI <= 0) {
                        game.getGameStateManager().setGameState(GameStateManager.GameState.ACTIVE);
                        game.activateGame();
                        return;
                    }

                    Cores.getInstance().getServer().broadcastMessage(ChatColor.YELLOW+"Starting in " + finalI);
                }
            }.runTaskLater(Cores.getInstance(), (startCountdown-i)*20L);
        }
    }
}

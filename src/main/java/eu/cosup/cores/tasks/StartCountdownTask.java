package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class StartCountdownTask extends BukkitRunnable {

    private int startCountdown;
    private boolean stopped = false;

    public StartCountdownTask() {

        startCountdown = Cores.getInstance().getConfig().getInt("start-countdown");

        if (startCountdown <= 0) {
            Bukkit.getLogger().severe("Start countdown cannot be that low, defaulting to 10");
            startCountdown = 10;
        }

    }

    @Override
    public void run() {
        Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.STARTING);

        // yes very nice now

        for (int i = 0; i <= startCountdown; i++) {

            int finalI = i;
            BukkitTask task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.JOINING || stopped) {
                        stopped = true;
                        return;
                    }

                    // at the alst second
                    if (finalI <= 0) {
                        Game.getGameInstance().getTeamManager().makeTeams(Game.getGameInstance().getJoinedPlayers());

                        Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.ACTIVE);
                        Game.getGameInstance().activateGame();
                        // KeinOptifine: This is how you are supposed to do it:
                        Cores.getInstance().getServer().broadcast(
                                Component.text("STARTING").color(NamedTextColor.YELLOW)
                        );

                        return;
                    }

                    // This is how youre supposed to do it
                    Cores.getInstance().getServer().broadcast(
                            Component.text("Starting in " + finalI)
                                    .color(NamedTextColor.YELLOW)
                    );

                }
            }.runTaskLater(Cores.getInstance(), (startCountdown - i) * 20L);
        }
    }
}

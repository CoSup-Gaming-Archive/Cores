package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.utility.ColorUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

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
                                Component.text("STARTING").color(ColorUtility.getStdTextColor("yellow"))
                        );

                        return;
                    }

                    // This is how youre supposed to do it
                    Cores.getInstance().getServer().broadcast(
                            Component.text("Starting in " + finalI)
                                    .color(ColorUtility.getStdTextColor("yellow"))
                    );

                }
            }.runTaskLater(Cores.getInstance(), (startCountdown - i) * 20L);
        }
    }
}

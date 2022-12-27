package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class StartCountdownTask extends BukkitRunnable {

    private int startCountdown;
    private boolean stopped = false;
    // TODO: never queried plus potentially final?
    private ArrayList<BukkitTask> countTasks = new ArrayList<>();

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
                        // i mean its a cheaky way but works for me
                        // TODO should probably find a different way of doing this by canceling tasks but that for some reason didnt work for me.
                        // TODO you can probably lag the server by joining and rejoining really fast
                        stopped = true;
                        return;
                    }

                    // at the alst second
                    if (finalI <= 0) {
                        // change the game state and activate the main loop
                        // TODO make players be able to join as parties because its tournamet
                        // TODO make teams method is used here!!!!
                        // so we probably want to chage it
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
                    // This is how it was before
                    //Cores.getInstance().getServer().broadcastMessage(ChatColor.YELLOW + "Starting in " + finalI);

                }
            }.runTaskLater(Cores.getInstance(), (startCountdown - i) * 20L);
            countTasks.add(task);
        }
    }
}

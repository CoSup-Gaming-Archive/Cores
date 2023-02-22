package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.objects.SideBarInformation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimerTask extends BukkitRunnable {

    private static int secondsElapsed;
    private static GameTimerTask instance;

    public GameTimerTask() {
        instance = this;
    }

    public static void resetTimer() {
        secondsElapsed = 0;
    }

    public static GameTimerTask getInstance() {
        return instance;
    }

    public static int getSecondsElapsed() {
        return secondsElapsed;
    }

    private static void setSecondsElapsed(int secondsElapsed) {
        GameTimerTask.secondsElapsed = secondsElapsed;
    }

    @Override
    public void run() {
        SideBarInformation.update();
        setSecondsElapsed(getSecondsElapsed() + 1);

        if (secondsElapsed == 600) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.ARMOR_UPGRADE);
            Cores.getInstance().getServer().broadcast(Component.text("At 15 minutes all beacons will be destroyed").color(NamedTextColor.RED));
            //Cores.getInstance().getServer().broadcast(Component.text("All team received").color(NamedTextColor.YELLOW).append(Component.text(" IRON ARMOR ").color(NamedTextColor.GRAY)));
        }

        if (secondsElapsed == 800) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.SWORD_UPGRADE);
            Cores.getInstance().getServer().broadcast(Component.text("At 15 minutes all beacons will be destroyed").color(NamedTextColor.RED));
            //Cores.getInstance().getServer().broadcast(Component.text("All team received").color(NamedTextColor.YELLOW).append(Component.text(" IRON SWORDS ").color(NamedTextColor.GRAY)));
        }

        if (secondsElapsed == 900) {
            Cores.getInstance().getServer().broadcast(Component.text("At 17 minutes the arena will start").color(NamedTextColor.RED));
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.BEACON_DESTRUCTION);
        }

        if (secondsElapsed == 1000) {
            Game.getGameInstance().getGameStateManager().setGamePhase(GameStateManager.GamePhase.ARENA);
            Cores.getInstance().getServer().broadcast(Component.text("At 30 minutes the team with more players alive wins").color(NamedTextColor.RED));
        }

        if (secondsElapsed == 1800) {
            Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.ENDING);
        }

        new GameTimerTask().runTaskLater(Cores.getInstance(), 20L);
    }

    public void cancelTimer() {
        this.cancel();
        instance = null;
    }
}

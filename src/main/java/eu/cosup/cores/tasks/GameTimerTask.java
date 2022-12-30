package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.managers.BeaconInformation;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimerTask extends BukkitRunnable {

    private static int secondsElapsed;
    private static GameTimerTask instance;

    public GameTimerTask() {
        instance = this;
    }

    @Override
    public void run() {
        BeaconInformation.update();
        setSecondsElapsed(getSecondsElapsed()+1);
        new GameTimerTask().runTaskLater(Cores.getInstance(), 20L);
    }

    public void cancelTimer() {
        this.cancel();
        instance = null;
    }

    public static void resetTimer() {
        secondsElapsed = 0;
    }

    private static void setSecondsElapsed(int secondsElapsed) {
        GameTimerTask.secondsElapsed = secondsElapsed;
    }

    public static GameTimerTask getInstance() {
        return instance;
    }
    public static int getSecondsElapsed() {
        return secondsElapsed;
    }
}

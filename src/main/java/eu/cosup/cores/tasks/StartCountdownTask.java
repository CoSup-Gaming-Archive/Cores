package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.Team;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StartCountdownTask extends BukkitRunnable {

    private int startCountdown;

    public StartCountdownTask() {

        startCountdown = Cores.getInstance().getConfig().getInt("start-countdown");

        if (startCountdown <= 0) {
            Bukkit.getLogger().severe("Start countdown cannot be that low");
            startCountdown = 10;
        }

    }

    @Override
    public void run() {


        // TODO remove debug loop bellow
        // its just for testing to see that you are on the right team
        for (Team team : Game.getGameInstance().getTeamManager().getTeams()) {
            for (Player player : team.getPlayers()) {
                player.sendMessage(TeamColor.getChatColor(team.getColor())+"You are on yes :))))))))");
            }
        }

        Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.STARTING);

        // yes very nice now

        for (int i = 0; i <= startCountdown; i++) {

            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {

                    // at the alst second
                    if (finalI <= 0) {
                        // change the game state and activate the main loop
                        // TODO make players be able to join as parties because its tournamet
                        Game.getGameInstance().getTeamManager().makeTeams(Game.getGameInstance().getJoinedPlayers());

                        Game.getGameInstance().getGameStateManager().setGameState(GameStateManager.GameState.ACTIVE);
                        Game.getGameInstance().activateGame();

                        return;
                    }

                    Cores.getInstance().getServer().broadcastMessage(ChatColor.YELLOW+"Starting in " + finalI);
                }
            }.runTaskLater(Cores.getInstance(), (startCountdown-i)*20L);
        }
    }
}

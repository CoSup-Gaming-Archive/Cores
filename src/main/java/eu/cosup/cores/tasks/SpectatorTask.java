package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.TeamColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SpectatorTask extends BukkitRunnable {

    private final Player player;

    public SpectatorTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {

        TeamColor team = Game.getGameInstance().getTeamManager().whichTeam(player);
        player.setGameMode(GameMode.SPECTATOR);
        player.setVelocity(new Vector().zero());

        // yay
        player.teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());

        for (int i = 0; i < Cores.getInstance().getConfig().getInt("respawn-delay"); i++) {
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.clearTitle();
                    Component msg = Component.text().content("Respawning in " ).color(NamedTextColor.RED)
                            .append(Component.text().content(String.valueOf(Cores.getInstance().getConfig().getInt("respawn-delay")-finalI))).build();

                    Title title = Title.title(msg, Component.text().build());

                    player.showTitle(title);

                    player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_STEP, 1, finalI);
                }
            }.runTaskLater(Cores.getInstance(), i*20L);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.clearTitle();

                if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ENDING) {
                    cancel();
                    return;
                }

                if (team == TeamColor.RED) {
                    player.teleport(Game.getGameInstance().getSelectedMap().getTeamRedSpawns());
                }

                if (team == TeamColor.BLUE) {
                    player.teleport(Game.getGameInstance().getSelectedMap().getTeamBlueSpawns());
                }

                ActivateGameTask.preparePlayerFull(player);


                player.sendMessage(Component.text().content("You are alive!").color(TeamColor.getNamedTextColor(team)));
            }
        }.runTaskLater(Cores.getInstance(), Cores.getInstance().getConfig().getInt("respawn-delay") * 20L);
    }
}

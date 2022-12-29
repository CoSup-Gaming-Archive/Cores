package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.TeamColor;
import eu.cosup.cores.utility.ColorUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SpectatorTask extends BukkitRunnable {

    // final?
    private Player player;
    private Entity killer;

    public SpectatorTask(Player player) {
        this.player = player;
        if (player.getLastDamageCause() == null) {
            killer = player;
            return;
        }
        killer = player.getLastDamageCause().getEntity();
    }

    @Override
    public void run() {

        if (killer instanceof Player killerPlayer) {
            killerPlayer.playSound(killer.getLocation(), Sound.BLOCK_GILDED_BLACKSTONE_HIT, 1, 3);

        }

        Cores.getInstance().getServer().sendMessage(Component.text().
                content(player.getName()).color(ColorUtility.getStdTextColor("red"))
                .append(Component.text().content(" was killed by ").color(ColorUtility.getStdTextColor("yellow")))
                .append(Component.text().content(killer.getName())).color(ColorUtility.getStdTextColor("red")));

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
                    Component msg = Component.text().content("Respawning in " ).color(ColorUtility.getStdTextColor("red"))
                            .append(Component.text().content(String.valueOf(Cores.getInstance().getConfig().getInt("respawn-delay")-finalI))).build();

                    // Creates a simple title with the default values for fade-in, stay on screen and fade-out durations
                    Title title = Title.title(msg, Component.text().build());

                    // Send the title to your audience
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


                player.sendMessage(Component.text().content("You are alive!").color(ColorUtility.getStdTextColor(team.name())));
            }
        }.runTaskLater(Cores.getInstance(), Cores.getInstance().getConfig().getInt("respawn-delay") * 20L);
    }
}

package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SpectatorTask extends BukkitRunnable {

    private static final int respawnDelay = Cores.getInstance().getConfig().getInt("respawn-delay");
    private final Player player;
    private final Boolean respawn;

    public SpectatorTask(Player player, Boolean respawn) {
        this.player = player;
        this.respawn = respawn;

    }

    @Override
    public void run() {

        player.setGameMode(GameMode.SPECTATOR);
        player.setVelocity(new Vector().zero());

        // yay
        player.teleport(Game.getGameInstance().getSelectedMap().getSpectatorSpawn());

        if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
            if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()) != null) {
                Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).setPlayerDead(player, true);
            }
        }

        if (!respawn) {
            return;
        }


        for (int i = 0; i < respawnDelay; i++) {
            int finalI = i;
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.clearTitle();
                    Component msg = Component.text().content("Respawning in ").color(NamedTextColor.RED)
                            .append(Component.text().content(String.valueOf(respawnDelay - finalI))).build();

                    Title title = Title.title(msg, Component.text().build());

                    player.showTitle(title);

                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1, finalI);
                }
            }.runTaskLater(Cores.getInstance(), i * 20L);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                player.clearTitle();

                if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ENDING) {
                    cancel();
                    return;
                }
                Game.getGameInstance().updatePlayersNameTag(player);
                player.setVelocity(new Vector().zero());
                player.teleport(Game.getGameInstance().getSelectedMap().getSpawnByPlayer(player));
                ActivateGameTask.preparePlayerFull(player);
                Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()).setPlayerDead(player, false);

            }
        }.runTaskLater(Cores.getInstance(), respawnDelay * 20L);
    }

}

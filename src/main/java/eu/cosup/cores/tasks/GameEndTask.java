package eu.cosup.cores.tasks;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.objects.Team;
import eu.cosup.cores.objects.TeamColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

public class GameEndTask extends BukkitRunnable {

    private final TeamColor winner;

    public GameEndTask(@Nullable TeamColor winner) {
        this.winner = winner;
    }

    @Override
    public void run() {

        Team winnerTeam = Game.getGameInstance().getTeamManager().getTeamByColor(winner);

        GameTimerTask.getInstance().cancelTimer();

        if (winnerTeam != null) {
            for (Player player : winnerTeam.getPlayers()) {

                Location playerLocation = player.getLocation();

                for (int i = 0; i < 1; i++) {
                    Cores.getInstance().getGameWorld().spawnEntity(playerLocation, EntityType.FIREWORK);
                }
            }

            Location baseCenter = Game.getGameInstance().getSelectedMap().getSpawnByColor(winner);
            int x = baseCenter.getBlockX();
            int y = baseCenter.getBlockY();
            int z = baseCenter.getBlockZ();
            for (int xIndex = -8; xIndex < 8; xIndex++) {
                for (int yIndex = -8; yIndex < 8; yIndex++) {
                    for (int zIndex = -8; zIndex < 8; zIndex++) {
                        int bx = x + xIndex;
                        int by = y + yIndex;
                        int bz = z + zIndex;
                        double distance = Math.sqrt(Math.pow(xIndex, 2) + Math.pow(yIndex, 2) + Math.pow(zIndex, 2));
                        if (distance <= 8) {
                            baseCenter.getWorld().getBlockAt(x + xIndex, y + yIndex, z + zIndex).setType(Material.AIR, false);
                        }
                    }
                }
            }
            for (int xi = -2; xi <= 2; xi++) {
                for (int yi = 0; yi <= y - 2; yi++) {
                    for (int zi = -2; zi <= 2; zi++) {
                        double distance = Math.sqrt(Math.pow(xi, 2) + Math.pow(zi, 2));
                        if (distance <= 2.3) {
                            if (yi == y - 2) {
                                baseCenter.getWorld().getBlockAt(baseCenter.getBlockX() + xi, yi, baseCenter.getBlockZ() + zi).setType(Material.GOLD_BLOCK);
                            } else if (yi == y - 3) {
                                baseCenter.getWorld().getBlockAt(baseCenter.getBlockX() + xi, yi, baseCenter.getBlockZ() + zi).setType(Material.CHAIN);
                            } else {
                                baseCenter.getWorld().getBlockAt(baseCenter.getBlockX() + xi, yi, baseCenter.getBlockZ() + zi).setType(Material.EMERALD_BLOCK);
                            }
                        }
                    }
                }
            }
            for (Player player : winnerTeam.getOnlinePlayers()) {
                player.teleport(baseCenter);
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (Game.getGameInstance().getTeamManager().whichTeam(player.getUniqueId()) != winnerTeam) {
                    player.setGameMode(GameMode.SPECTATOR);
                    player.teleport(baseCenter);
                }

            }
        }

        Component msg;
        if (winnerTeam != null) {
            msg = Component.text().content(String.valueOf(winner)).color(TeamColor.getNamedTextColor(winner))
                    .append(Component.text().content(" is the winner!").color(NamedTextColor.YELLOW)).build();
        } else {
            msg = Component.text("The game ended").color(NamedTextColor.RED);
        }
        Cores.getInstance().getServer().broadcast(msg);

        Bukkit.getLogger().warning("Shutting down in: " + Cores.getInstance().getConfig().getInt("return-to-lobby-delay"));
        new BukkitRunnable() {
            @Override
            public void run() {

                Component msg = Component.text().content("server is shutting down").color(NamedTextColor.RED).build();
                Cores.getInstance().getServer().broadcast(msg);

                Cores.getInstance().getServer().shutdown();

            }
        }.runTaskLater(Cores.getInstance(), Cores.getInstance().getConfig().getInt("return-to-lobby-delay") * 20L);
    }
}

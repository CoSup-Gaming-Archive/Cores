package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.BeaconInformation;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.Team;
import eu.cosup.cores.managers.TeamColor;
import eu.cosup.cores.utility.BlockUtility;
import eu.cosup.cores.utility.ColorUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockBreakListener implements Listener {


    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {

        Game gameInstance = Game.getGameInstance();

        Player player = event.getPlayer();
        Block block = event.getBlock();

        // if the game doesnt start
        if (gameInstance.getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.setCancelled(true);
                return;
            }
        }
        // why not make a single if block for the above stuff?

        // in case it is a beacon
        if (block.getType() == Material.BEACON) {

            TeamColor beaconTeamColor = gameInstance.getSelectedMap().whichTeamBeacon(block.getLocation());

            TeamColor playerTeamColor = gameInstance.getTeamManager().whichTeam(player);

            if (beaconTeamColor == null || playerTeamColor == null) {
                event.setCancelled(true);
                return;
            }

            // so no own kill
            if (playerTeamColor == beaconTeamColor) {
                // creative players can destroy their own beacons
                // mostly for testing
                if (player.getGameMode() != GameMode.CREATIVE) {
                    event.setCancelled(true);
                    return;
                }
            }


            // it was no accident
            Team loserTeam = gameInstance.getTeamManager().getTeamByColor(beaconTeamColor);

            // minus beacon count
            loserTeam.loseBeacon();
            BeaconInformation.update();

            // broadcast that they lost beacon
            Component msg = Component.text().content("A ").color(TextColor.color(ColorUtility.getStdTextColor("yellow")))
                    .append(Component.text().content("BEACON").color(ColorUtility.getStdTextColor(loserTeam.getColor().toString())))
                    .append(Component.text().content(" was destroyed!").color(ColorUtility.getStdTextColor("yellow"))).build();
            Cores.getInstance().getServer().broadcast(msg);

            // cheeky way of getting the beacon to not drop anything
            block.setType(Material.BARRIER);
            event.setCancelled(true);

            if (loserTeam.getBeaconCount() <= 0) {

                // there is a clear winner

                if (beaconTeamColor == TeamColor.RED) {
                    gameInstance.finishGame(TeamColor.BLUE);
                }

                if (beaconTeamColor == TeamColor.BLUE) {
                    gameInstance.finishGame(TeamColor.RED);
                }
            }
            return;
        }

        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (!BlockUtility.blockWhitelisted(block.getType())) {
                event.setCancelled(true);
                return;
            }
        }
    }
}

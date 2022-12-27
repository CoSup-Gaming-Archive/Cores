package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.BeaconInformation;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.Team;
import eu.cosup.cores.managers.TeamColor;
import eu.cosup.cores.utility.BlockUtility;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockBreakListener implements Listener {


    // TODO maybe clean this up a bit
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

            Bukkit.getLogger().info("beacon: " + beaconTeamColor + "   player: " + playerTeamColor);

            if (beaconTeamColor == null || playerTeamColor == null) {
                event.setCancelled(true);
                return;
            }

            // so no own kill
            if (playerTeamColor == beaconTeamColor) {
                // creative players can destroy their own beacons
                // mostly for testing
                if (player.getGameMode() != GameMode.CREATIVE) {
                    player.sendMessage("You cannot break your own beacon you dum dum");
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
            Cores.getInstance().getServer().broadcastMessage(TeamColor.getChatColor(loserTeam.getColor()) + "A " + loserTeam.getColor() + " beacon" + ChatColor.WHITE + " was destroyed");

            // cheeky way of getting the beacon to not drop anything
            block.setType(Material.AIR);
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

        // ? Combine the if statements into one block. I didnt change cus maybe this is WIP
    }


}

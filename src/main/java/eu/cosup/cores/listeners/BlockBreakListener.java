package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.Team;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.List;

public class BlockBreakListener implements Listener {

    private static List<String> breakableList = Cores.getInstance().getConfig().getStringList("whitelist-break");

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();

        // if the game doesnt start
        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            event.setCancelled(true);
        }

        // in case it is a beacon
        if (event.getBlock().getType() == Material.BEACON) {

            TeamColor beaconTeamColor = Game.getGameInstance().getSelectedMap().whichTeamBeacon(event.getBlock().getLocation());

            TeamColor playerTeamColor = Game.getGameInstance().getTeamManager().whichTeam(player);

            Bukkit.getLogger().info("beacon: "+beaconTeamColor+"   player: "+playerTeamColor);

            if (beaconTeamColor == null || playerTeamColor == null) {
                event.setCancelled(true);
                return;
            }

            // so no own kill
            if (playerTeamColor == beaconTeamColor) {
                player.sendMessage("You cannot break your own beacon you dum dum");
                event.setCancelled(true);
                return;
            }

            // it was no accident

            Team loserTeam = Game.getGameInstance().getTeamManager().getTeamByColor(beaconTeamColor);

            // minus beacon count
            loserTeam.loseBeacon();

            // broadcast that they lost beacon
            Cores.getInstance().getServer().broadcastMessage(ChatColor.RED+"A "+loserTeam.getColor()+" was destroyed");

            event.getBlock().setType(Material.AIR);
            event.setCancelled(true);

            if (loserTeam.getBeaconCount() <= 0) {

                // there is a clear winner

                if (beaconTeamColor == TeamColor.RED) {
                    Game.getGameInstance().finishGame(TeamColor.BLUE);
                }

                if (beaconTeamColor == TeamColor.BLUE) {
                    Game.getGameInstance().finishGame(TeamColor.RED);
                }
            }


        }

    }

}

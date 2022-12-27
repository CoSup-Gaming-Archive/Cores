package eu.cosup.cores.managers;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import javax.annotation.Nullable;

public class BeaconInformation {
    public static void update() {

        // player list
        for (Player p : Cores.getInstance().getServer().getOnlinePlayers()) {
            p.setPlayerListHeaderFooter(
                    ChatColor.translateAlternateColorCodes('&', "&l&6CoSup&b Gaming"),
                    ChatColor.translateAlternateColorCodes('&', "\n"
                            + getBeaconText(TeamColor.BLUE) + "\n"
                            + getBeaconText(TeamColor.RED) + "\n\n&bCores"));
        }

        // scoreboard
        ScoreBoardManager scoreBoardManager = new ScoreBoardManager("beacons");
        scoreBoardManager.clearObjective();
        scoreBoardManager.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bCores"));
        scoreBoardManager.addItem("");
        scoreBoardManager.addItem(ChatColor.translateAlternateColorCodes('&', getBeaconText(TeamColor.BLUE)));
        scoreBoardManager.addItem(ChatColor.translateAlternateColorCodes('&', getBeaconText(TeamColor.RED)));
        scoreBoardManager.addItem("");
        scoreBoardManager.addItem(ChatColor.translateAlternateColorCodes('&', "   &6CoSup &bGaming"));
        scoreBoardManager.setSlot(DisplaySlot.SIDEBAR);
        scoreBoardManager.getObjective();

    }

    public static String getBeaconText(TeamColor teamColor) {

        if (TeamColor.getChatColor(teamColor) == ChatColor.GRAY) {
            return "";
        }

        StringBuilder displayString = new StringBuilder();

        if (teamColor.equals(TeamColor.RED)) {
            displayString = new StringBuilder("&cRed Beacons: ");
        }

        if (teamColor.equals(TeamColor.BLUE)) {
            displayString = new StringBuilder("&9Blue Beacons: ");
        }

        // we dont want null errors
        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            return displayString.toString();
        }

        int beaconCount = Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getBeaconCount();
        int maxBeaconCount = Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getMaxBeaconCount();
        int missingBeaconCount = maxBeaconCount - beaconCount;

        // this is the ✔ symbol
        displayString.append("&a\u2714".repeat(Math.max(0, beaconCount)));

        // this is the ✖ symbol
        displayString.append("&c\u2716".repeat(Math.max(0, missingBeaconCount)));

        return displayString.toString();
    }
}

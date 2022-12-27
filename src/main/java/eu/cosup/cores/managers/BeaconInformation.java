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

        String displayString = "";

        if (teamColor.equals(TeamColor.RED)) {
            displayString = "&cRed Beacons: ";
        }

        if (teamColor.equals(TeamColor.BLUE)) {
            displayString = "&9Blue Beacons: ";
        }

        // we dont want null errors
        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            return displayString;
        }

        int beaconCount = Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getBeaconCount();
        int maxBeaconCount = Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getMaxBeaconCount();
        int missingBeaconCount = maxBeaconCount - beaconCount;

        for (int i = 0; i < beaconCount; i++) {
            // this is the ✔ symbol
            displayString += "&a\u2714";
        }

        for (int i = 0; i < missingBeaconCount; i++) {
            // this is the ✖ symbol
            displayString += "&c\u2716";
        }

        return displayString;
    }
}

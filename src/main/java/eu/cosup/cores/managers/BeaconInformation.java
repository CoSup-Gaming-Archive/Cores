package eu.cosup.cores.managers;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.utility.ColorUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import javax.annotation.Nullable;

public class BeaconInformation {
    public static void update() {

        Component header = Component.text().content("Cosup Gaming").build();
        Component footer = (Component.text().content("Blue beacons: ").color(ColorUtility.getStdTextColor("blue")))
                .append(Component.text().content(getAliveSymbols(TeamColor.BLUE)).color(ColorUtility.getStdTextColor("green")))
                .append(Component.text().content(getDeadSymbols(TeamColor.BLUE)).color(ColorUtility.getStdTextColor("gray")))

                .append(Component.text().content("\n"))

                .append(Component.text().content("Red beacons: ").color(ColorUtility.getStdTextColor("red")))
                .append(Component.text().content(getAliveSymbols(TeamColor.RED)).color(ColorUtility.getStdTextColor("green")))
                .append(Component.text().content(getDeadSymbols(TeamColor.RED)).color(ColorUtility.getStdTextColor("gray")))

                .build();

        // player list
        for (Player player : Cores.getInstance().getServer().getOnlinePlayers()) {
            player.sendPlayerListHeaderAndFooter(header, footer);
        }

        // scoreboard
        ScoreBoardManager scoreBoardManager = new ScoreBoardManager("beacons");
        scoreBoardManager.clearObjective();
        scoreBoardManager.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bCores"));

        scoreBoardManager.addItem(Component.text().content(" ").build());
        scoreBoardManager.addItem(Component.text().content("Blue beacons: ").color(ColorUtility.getStdTextColor("blue"))
                .append(Component.text().content(getAliveSymbols(TeamColor.BLUE)).color(ColorUtility.getStdTextColor("green")))
                .append(Component.text().content(getDeadSymbols(TeamColor.BLUE)).color(ColorUtility.getStdTextColor("red")))
                .build());
        scoreBoardManager.addItem(Component.text().content("Red beacons: ").color(ColorUtility.getStdTextColor("red"))
                .append(Component.text().content(getAliveSymbols(TeamColor.RED)).color(ColorUtility.getStdTextColor("green")))
                .append(Component.text().content(getDeadSymbols(TeamColor.RED)).color(ColorUtility.getStdTextColor("red")))
                .build());

        scoreBoardManager.addItem(Component.text().content(" ").build());
        scoreBoardManager.addItem(Component.text().content("CoSup Gaming").color(ColorUtility.getStdTextColor("gray")).build());
        scoreBoardManager.setSlot(DisplaySlot.SIDEBAR);
        scoreBoardManager.getObjective();

    }

    // i cannot be bothered to chage this again.
    private static String getAliveSymbols(TeamColor teamColor) {
        StringBuilder aliveSymbols = new StringBuilder();

        // we dont want null errors
        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            return "";
        }

        int beaconCount = Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getBeaconCount();

        // this is the ✔ symbol
        aliveSymbols.append("\u2714".repeat(Math.max(0, beaconCount)));

        return aliveSymbols.toString();
    }

    private static String getDeadSymbols(TeamColor teamColor) {
        StringBuilder deadSymbols = new StringBuilder();

        // we dont want null errors
        if (Game.getGameInstance().getGameStateManager().getGameState() != GameStateManager.GameState.ACTIVE) {
            return "";
        }

        int beaconCount = Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getBeaconCount();
        int maxBeaconCount = Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getMaxBeaconCount();
        int missingBeaconCount = maxBeaconCount - beaconCount;

        // this is the ✖ symbol
        deadSymbols.append("\u2716".repeat(Math.max(0, missingBeaconCount)));

        return deadSymbols.toString();
    }
}

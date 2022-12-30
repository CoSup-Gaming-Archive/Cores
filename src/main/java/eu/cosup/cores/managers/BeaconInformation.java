package eu.cosup.cores.managers;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class BeaconInformation {
    public static void update() {

        Component header = Component.text().content("Cosup Gaming").build();
        Component footer = (Component.text().content("\n"))
                .append((Component.text().content("Blue beacons: ").color(NamedTextColor.BLUE)))
                .append(Component.text().content(getAliveSymbols(TeamColor.BLUE)).color(NamedTextColor.GREEN))
                .append(Component.text().content(getDeadSymbols(TeamColor.BLUE)).color(NamedTextColor.GRAY))

                .append(Component.text().content("\n"))

                .append(Component.text().content("Red beacons: ").color(NamedTextColor.RED))
                .append(Component.text().content(getAliveSymbols(TeamColor.RED)).color(NamedTextColor.GREEN))
                .append(Component.text().content(getDeadSymbols(TeamColor.RED)).color(NamedTextColor.GRAY))

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
        scoreBoardManager.addItem(Component.text().content("Blue beacons: ").color(NamedTextColor.BLUE)
                .append(Component.text().content(getAliveSymbols(TeamColor.BLUE)).color(NamedTextColor.GREEN))
                .append(Component.text().content(getDeadSymbols(TeamColor.BLUE)).color(NamedTextColor.GRAY))
                .build());
        scoreBoardManager.addItem(Component.text().content("Red beacons: ").color(NamedTextColor.RED)
                .append(Component.text().content(getAliveSymbols(TeamColor.RED)).color(NamedTextColor.GREEN))
                .append(Component.text().content(getDeadSymbols(TeamColor.RED)).color(NamedTextColor.GRAY))
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

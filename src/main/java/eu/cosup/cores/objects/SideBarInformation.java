package eu.cosup.cores.objects;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.managers.ScoreBoardManager;
import eu.cosup.cores.tasks.GameTimerTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.jetbrains.annotations.NotNull;

public class SideBarInformation {
    public static void update() {

        // scoreboard
        ScoreBoardManager scoreBoardManager = new ScoreBoardManager("bedwars");

        scoreBoardManager.clearObjective();
        scoreBoardManager.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&BCores"));
        addFormattedTeamStrings(scoreBoardManager);
        scoreBoardManager.addItem(Component.text().content(getFormattedTime()).color(NamedTextColor.YELLOW).build());
        scoreBoardManager.setSlot(DisplaySlot.SIDEBAR);
        scoreBoardManager.getObjective();
    }

    private static String getFormattedTime() {

        int seconds = GameTimerTask.getSecondsElapsed();

        int minutes = seconds / 60;
        seconds = seconds - minutes * 60;

        if (minutes > 0) {
            return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds) + " elapsed";
        }

        return seconds+" seconds";
    }

    private static void addFormattedTeamStrings(ScoreBoardManager scoreBoardManager) {

        for (Team team : Game.getGameInstance().getTeamManager().getTeams()) {

            TextComponent.Builder teamText = Component.text().content(team.getSlug()).color(TeamColor.getNamedTextColor(team.getColor()));
            scoreBoardManager.addItem(teamText.build());
            if (team.isLeftBeaconAlive()) {
                scoreBoardManager.addItem(Component.text("Left beacon").color(NamedTextColor.YELLOW).append(Component.text(" ON").color(NamedTextColor.GREEN)));
            } else {
                scoreBoardManager.addItem(Component.text("Left beacon").color(NamedTextColor.YELLOW).append(Component.text(" OFF").color(NamedTextColor.RED)));
            }

            if (team.isRightBeaconAlive()) {
                scoreBoardManager.addItem(Component.text("Right beacon").color(NamedTextColor.YELLOW).append(Component.text(" ON").color(NamedTextColor.GREEN)));
            } else {
                scoreBoardManager.addItem(Component.text("Right beacon").color(NamedTextColor.YELLOW).append(Component.text(" OFF").color(NamedTextColor.RED)));
            }

            scoreBoardManager.addItem(Component.text("------------------").color(NamedTextColor.GOLD));
        }
    }
}

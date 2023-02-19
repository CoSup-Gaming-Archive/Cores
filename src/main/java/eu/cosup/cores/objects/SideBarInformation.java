package eu.cosup.cores.objects;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.ScoreBoardManager;
import eu.cosup.cores.tasks.GameTimerTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;

public class SideBarInformation {
    public static void update() {

        // scoreboard
        ScoreBoardManager scoreBoardManager = new ScoreBoardManager("bedwars");

        scoreBoardManager.clearObjective();
        scoreBoardManager.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&BCores"));
        addFormattedTeamStrings(scoreBoardManager);
        scoreBoardManager.addItem(Component.text().content(getFormattedTime()).color(NamedTextColor.YELLOW).build());
        scoreBoardManager.setSlot(DisplaySlot.SIDEBAR);
        scoreBoardManager.refreshObjective();
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

            switch (team.getLeftBeaconState()) {
                case ON -> {
                    scoreBoardManager.addItem(Component.text("Left ").color(NamedTextColor.YELLOW).append(Component.text("beacon").color(TeamColor.getNamedTextColor(team.getColor()))).append(Component.text(" ON").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD)));
                }
                case OFF -> {
                    scoreBoardManager.addItem(Component.text("Left ").color(NamedTextColor.YELLOW).append(Component.text("beacon").color(TeamColor.getNamedTextColor(team.getColor()))).append(Component.text(" OFF").color(NamedTextColor.RED).decorate(TextDecoration.BOLD)));
                }
                case ATTACK -> {
                    if (GameTimerTask.getSecondsElapsed() % 2 == 0) {
                        scoreBoardManager.addItem(Component.text("Left ").color(NamedTextColor.YELLOW).append(Component.text("beacon").color(TeamColor.getNamedTextColor(team.getColor()))).append(Component.text(" \u26A0").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)));
                    } else {
                        scoreBoardManager.addItem(Component.text("Left ").color(NamedTextColor.YELLOW).append(Component.text("beacon").color(TeamColor.getNamedTextColor(team.getColor()))).append(Component.text(" \u26A0").color(NamedTextColor.RED).decorate(TextDecoration.BOLD)));
                    }
                }
            }

            switch (team.getRightBeaconState()) {
                case ON -> {
                    scoreBoardManager.addItem(Component.text("Right ").color(NamedTextColor.YELLOW).append(Component.text("beacon").color(TeamColor.getNamedTextColor(team.getColor()))).append(Component.text(" ON").color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD)));
                }
                case OFF -> {
                    scoreBoardManager.addItem(Component.text("Right ").color(NamedTextColor.YELLOW).append(Component.text("beacon").color(TeamColor.getNamedTextColor(team.getColor()))).append(Component.text(" OFF").color(NamedTextColor.RED).decorate(TextDecoration.BOLD)));
                }
                case ATTACK -> {
                    if (GameTimerTask.getSecondsElapsed() % 2 == 0) {
                        scoreBoardManager.addItem(Component.text("Right ").color(NamedTextColor.YELLOW).append(Component.text("beacon").color(TeamColor.getNamedTextColor(team.getColor()))).append(Component.text(" \u26A0").color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)));
                    } else {
                        scoreBoardManager.addItem(Component.text("Right ").color(NamedTextColor.YELLOW).append(Component.text("beacon").color(TeamColor.getNamedTextColor(team.getColor()))).append(Component.text(" \u26A0").color(NamedTextColor.RED).decorate(TextDecoration.BOLD)));
                    }
                }
            }

            scoreBoardManager.addItem(Component.text("------------------").color(TeamColor.getNamedTextColor(team.getColor())));
        }
    }
}

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

        int i = 0;
        for (Team team : Game.getGameInstance().getTeamManager().getTeams()) {
            i++;
            StringBuilder extra = new StringBuilder();
            extra.append("ㅤ".repeat(Math.max(0, i)));

            TextComponent.Builder teamText = Component.text().content(team.getSlug()).color(TeamColor.getNamedTextColor(team.getColor()));
            scoreBoardManager.addItem(teamText.build());

            switch (team.getLeftBeaconState()) {
                case ON -> {
                    scoreBoardManager.addItem(Component.text("Left beacon").color(NamedTextColor.YELLOW).append(Component.text(" ✔" + extra).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD)));
                }
                case OFF -> {
                    scoreBoardManager.addItem(Component.text("Left beacon").color(NamedTextColor.YELLOW).append(Component.text(" ✖" + extra).color(NamedTextColor.RED).decorate(TextDecoration.BOLD)));
                }
                case ATTACK -> {
                    if (GameTimerTask.getSecondsElapsed() % 2 == 0) {
                        scoreBoardManager.addItem(Component.text("Left beacon").color(NamedTextColor.YELLOW).append(Component.text(" \u26A0" + extra).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)));
                    } else {
                        scoreBoardManager.addItem(Component.text("Left beacon").color(NamedTextColor.YELLOW).append(Component.text(" \u26A0" + extra).color(NamedTextColor.RED).decorate(TextDecoration.BOLD)));
                    }
                }
            }

            switch (team.getRightBeaconState()) {
                case ON -> {
                    scoreBoardManager.addItem(Component.text("Right beacon").color(NamedTextColor.YELLOW).append(Component.text(" ✔" + extra).color(NamedTextColor.GREEN).decorate(TextDecoration.BOLD)));
                }
                case OFF -> {
                    scoreBoardManager.addItem(Component.text("Right beacon").color(NamedTextColor.YELLOW).append(Component.text(" ✖" + extra).color(NamedTextColor.RED).decorate(TextDecoration.BOLD)));
                }
                case ATTACK -> {
                    if (GameTimerTask.getSecondsElapsed() % 2 == 0) {
                        scoreBoardManager.addItem(Component.text("Right beacon").color(NamedTextColor.YELLOW).append(Component.text(" \u26A0" + extra).color(NamedTextColor.YELLOW).decorate(TextDecoration.BOLD)));
                    } else {
                        scoreBoardManager.addItem(Component.text("Right beacon").color(NamedTextColor.YELLOW).append(Component.text(" \u26A0" + extra).color(NamedTextColor.RED).decorate(TextDecoration.BOLD)));
                    }
                }
            }

            scoreBoardManager.addItem(Component.text("------------------" + extra).color(NamedTextColor.YELLOW));
        }
    }
}

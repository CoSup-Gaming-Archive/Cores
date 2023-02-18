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

        scoreBoardManager.addItem(Component.text().content(" ").build());

        addFormattedTeamStrings(scoreBoardManager);
        scoreBoardManager.addItem(Component.text().content(" ").build());


        scoreBoardManager.addItem(Component.text().content(getFormattedTime()).color(NamedTextColor.YELLOW).build());
        scoreBoardManager.addItem(Component.text().content(" ").build());


        scoreBoardManager.addItem(Component.text().content("CoSup Gaming").color(NamedTextColor.GRAY).build());
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
            scoreBoardManager.addItem(Component.text("\u0020"));


            if (team.isLeftBeaconAlive()) {
                scoreBoardManager.addItem(Component.text("Left beacon").color(TeamColor.getNamedTextColor(team.getColor())).append(Component.text("\u2714").color(NamedTextColor.GREEN)));
            } else {
                scoreBoardManager.addItem(Component.text("Left beacon").color(TeamColor.getNamedTextColor(team.getColor())).append(Component.text("\u2716").color(NamedTextColor.RED)));
            }

            if (team.isRightBeaconAlive()) {
                scoreBoardManager.addItem(Component.text("Right beacon").color(TeamColor.getNamedTextColor(team.getColor())).append(Component.text("\u2714").color(NamedTextColor.GREEN)));
            } else {
                scoreBoardManager.addItem(Component.text("Right beacon").color(TeamColor.getNamedTextColor(team.getColor())).append(Component.text("\u2716").color(NamedTextColor.RED)));
            }

            scoreBoardManager.addItem(Component.text("\u0020"));
        }
    }
}

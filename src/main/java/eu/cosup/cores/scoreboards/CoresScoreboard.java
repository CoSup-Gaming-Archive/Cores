package eu.cosup.cores.scoreboards;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.builders.EntryName;
import eu.cosup.cores.builders.ScoreboardBuilder;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.objects.TeamColor;
import eu.cosup.cores.tasks.GameTimerTask;
import eu.cosup.tournament.common.utility.PlayerUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.jetbrains.annotations.NotNull;

public class CoresScoreboard extends ScoreboardBuilder {

    public CoresScoreboard(@NotNull Player player) {
        super(player, "coresScoreboard");
    }

    @Override
    public void build() {

        addTeam("0010Staff",
                Component.text()
                        .append(Component.text().content("Staff").color(NamedTextColor.DARK_AQUA).build())
                        .append(Component.text().content(" ┃ ").color(NamedTextColor.DARK_GRAY).build())
                        .build(),
                Component.empty()
        );
        addTeam("0008Referee",
                Component.text()
                        .append(Component.text().content("Referee").color(NamedTextColor.AQUA).build())
                        .append(Component.text().content(" ┃ ").color(NamedTextColor.DARK_GRAY).build())
                        .build(),
                Component.empty()
        );
        addTeam("0006Commentator",
                Component.text()
                        .append(Component.text().content("Commentator").color(NamedTextColor.LIGHT_PURPLE).build())
                        .append(Component.text().content(" ┃ ").color(NamedTextColor.DARK_GRAY).build())
                        .build(),
                Component.empty()
        );
        addTeam("0005Streamer",
                Component.text()
                        .append(Component.text().content("Streamer").color(NamedTextColor.DARK_PURPLE).build())
                        .append(Component.text().content(" ┃ ").color(NamedTextColor.DARK_GRAY).build())
                        .build(),
                Component.empty()
        );

        displayname(Component.text("Cores").color(NamedTextColor.GOLD));
        displaySlot(DisplaySlot.SIDEBAR);

        addScore(EntryName.ENTRY_9.entryName(), 9);
        addUpdatingScore(EntryName.ENTRY_8.entryName(), 8, "cores_teamRed", Component.text("Team Red").color(NamedTextColor.RED), null);
        addUpdatingScore(EntryName.ENTRY_7.entryName(), 7, "cores_teamRedCoreLeft", Component.text("\u2714").color(NamedTextColor.GREEN), Component.text(" Left beacon"));
        addUpdatingScore(EntryName.ENTRY_6.entryName(), 6, "cores_teamRedCoreRight", Component.text("\u2714").color(NamedTextColor.GREEN), Component.text(" Right beacon"));
        addScore(EntryName.ENTRY_5.entryName(), 5);

        addUpdatingScore(EntryName.ENTRY_4.entryName(), 4, "cores_teamBlue", Component.text("Team Blue").color(NamedTextColor.BLUE), null);
        addUpdatingScore(EntryName.ENTRY_3.entryName(), 3, "cores_teamBlueCoreLeft", Component.text("\u2714").color(NamedTextColor.GREEN), Component.text(" Left beacon"));
        addUpdatingScore(EntryName.ENTRY_2.entryName(), 2, "cores_teamBlueCoreRight", Component.text("\u2714").color(NamedTextColor.GREEN), Component.text(" Right beacon"));
        addScore(EntryName.ENTRY_1.entryName(), 1);
        addUpdatingScore(EntryName.ENTRY_0.entryName(), 0, "cores_gameTimer", Component.text("0").color(NamedTextColor.YELLOW), Component.text(" seconds").color(NamedTextColor.YELLOW));
    }

    @Override
    public void update() {
        Bukkit.getScheduler().runTaskTimer(Cores.getInstance(), () -> {
            if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
                updateScore("cores_teamRed", Component.text(Game.getGameInstance().getTeamManager().getTeamByColor(TeamColor.RED).getName()).color(NamedTextColor.RED), null);
                updateScore("cores_teamBlue", Component.text(Game.getGameInstance().getTeamManager().getTeamByColor(TeamColor.BLUE).getName()).color(NamedTextColor.BLUE), null);
                updateScore("cores_gameTimer", Component.text(getFormattedTime()).color(NamedTextColor.YELLOW), Component.empty());
                for (eu.cosup.cores.objects.Team team : Game.getGameInstance().getTeamManager().getTeams()) {

                    switch (team.getLeftBeaconState()) {
                        case ON -> updateScore("cores_team" + TeamColor.getFormattedTeamColor(team.getColor()) + "CoreLeft", Component.text("\u2714").color(NamedTextColor.GREEN), Component.text(" Left beacon"));
                        case OFF -> updateScore("cores_team" + TeamColor.getFormattedTeamColor(team.getColor()) + "CoreLeft", Component.text("\u2716").color(NamedTextColor.RED), Component.text(" Left beacon"));
                        case ATTACK -> {
                            if (GameTimerTask.getSecondsElapsed() % 2 == 0) {
                                updateScore("cores_team" + TeamColor.getFormattedTeamColor(team.getColor()) + "CoreLeft", Component.text("\u26A0").color(NamedTextColor.WHITE), Component.text(" Left beacon").color(NamedTextColor.WHITE));
                            } else {
                                updateScore("cores_team" + TeamColor.getFormattedTeamColor(team.getColor()) + "CoreLeft", Component.text("\u26A0").color(NamedTextColor.YELLOW), Component.text(" Left beacon").color(NamedTextColor.YELLOW));
                            }
                        }
                    }
                    switch (team.getRightBeaconState()) {
                        case ON -> updateScore("cores_team" + TeamColor.getFormattedTeamColor(team.getColor()) + "CoreRight", Component.text("\u2714").color(NamedTextColor.GREEN), Component.text(" Right beacon"));
                        case OFF -> updateScore("cores_team" + TeamColor.getFormattedTeamColor(team.getColor()) + "CoreRight", Component.text("\u2716").color(NamedTextColor.RED), Component.text(" Right beacon"));
                        case ATTACK -> {
                            if (GameTimerTask.getSecondsElapsed() % 2 == 0) {
                                updateScore("cores_team" + TeamColor.getFormattedTeamColor(team.getColor()) + "CoreRight", Component.text("\u26A0").color(NamedTextColor.WHITE), Component.text(" Right beacon").color(NamedTextColor.WHITE));
                            } else {
                                updateScore("cores_team" + TeamColor.getFormattedTeamColor(team.getColor()) + "CoreRight", Component.text("\u26A0").color(NamedTextColor.YELLOW), Component.text(" Right beacon").color(NamedTextColor.YELLOW));
                            }
                        }
                    }
                }
            }
        }, 0, 20);
    }

    private static String getFormattedTime() {

        int seconds = GameTimerTask.getSecondsElapsed();

        int minutes = seconds / 60;
        seconds = seconds - minutes * 60;

        if (minutes > 0) {
            return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds) + " elapsed";
        }

        return seconds + " seconds";
    }
}

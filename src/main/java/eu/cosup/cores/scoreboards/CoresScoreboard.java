package eu.cosup.cores.scoreboards;

import com.google.gson.JsonParser;
import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.core.data.TeamColor;
import eu.cosup.cores.core.utility.builders.EntryName;
import eu.cosup.cores.core.utility.builders.ScoreboardBuilder;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.tasks.GameTimerTask;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;

public class CoresScoreboard extends ScoreboardBuilder {

    private static HashMap<String, CoresScoreboard> scoreboards = new HashMap<>();

    public static HashMap<String, CoresScoreboard> getScoreboards() {
        return scoreboards;
    }

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

        addUpdatingScore(EntryName.ENTRY_10.entryName(), -1, "cores_twitchViews", Component.text("OFFLINE").color(NamedTextColor.RED), null);
    }

    @Override
    public void update() {
        Bukkit.getScheduler().runTaskTimer(Cores.getInstance(), () -> {
            if (Game.getGameInstance().getGameStateManager().getGameState() == GameStateManager.GameState.ACTIVE) {
                updateScore("cores_teamRed", Component.text(Game.getGameInstance().getTeamManager().getTeamByColor(TeamColor.RED).getName()).color(NamedTextColor.RED), null);
                updateScore("cores_teamBlue", Component.text(Game.getGameInstance().getTeamManager().getTeamByColor(TeamColor.BLUE).getName()).color(NamedTextColor.BLUE), null);
                updateScore("cores_gameTimer", Component.text(getFormattedTime()).color(NamedTextColor.YELLOW), Component.empty());
                for (eu.cosup.cores.core.data.Team team : Game.getGameInstance().getTeamManager().getTeams()) {

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

        Bukkit.getScheduler().runTaskTimerAsynchronously(Cores.getInstance(), () -> {
            updateScore("cores_twitchViews", getTwitchViewers(), null);
        }, 0L, 20L*5);
    }

    private static Component getTwitchViewers() {

        String bearer = System.getenv("TWITCH_BEARER");
        String client_id = System.getenv("TWITCH_CLIENT_ID");

        if (bearer == null || client_id == null) {
            throw new RuntimeException("One of the environment variables isnt set up TWITCH_BEARER OR CLIENT_ID");
        }

        URLConnection connection;

        try {
            connection = new URL("https://api.twitch.tv/helix/streams?user_login=CoSup_Gaming").openConnection();
            connection.setRequestProperty("Authorization", "Bearer "+bearer);
            connection.setRequestProperty("client-id", client_id);
            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");

        } catch (IllegalArgumentException | IOException ignored) {
            return Component.text("ERROR").color(NamedTextColor.RED);
        }

        try {
            InputStream is = connection.getInputStream();

            String response = Arrays.toString(is.readAllBytes());

            String[] byteValues = response.substring(1, response.length() - 1).split(",");
            byte[] bytes = new byte[byteValues.length];

            for (int i=0, len=bytes.length; i<len; i++) {
                bytes[i] = Byte.parseByte(byteValues[i].trim());
            }

            String data = JsonParser.parseString(new String(bytes)).getAsJsonObject().get("data").toString().replace("[", "").replace("]", "");

            if (data.length() == 0) {
                return Component.text("OFFLINE").color(NamedTextColor.RED);
            }

            int viewers = Integer.parseInt(data.split("viewer_count")[1].split(",")[0].replace("\":", ""));

            if (viewers < 10) {
                return Component.text("");
            }

            return Component.text(viewers + "").color(NamedTextColor.WHITE).append(Component.text(" viewers").color(TextColor.fromCSSHexString("#6441a5")));

        } catch (IOException ignored) {
            return Component.text("").color(NamedTextColor.RED);
        }
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

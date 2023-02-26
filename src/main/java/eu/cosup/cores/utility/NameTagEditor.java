package eu.cosup.cores.utility;

import eu.cosup.cores.scoreboards.CoresScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class NameTagEditor {
    Player player;
    Team team;

    public NameTagEditor(@NotNull Player player) {
        this.player = player;
        Scoreboard scoreboard = CoresScoreboard.getScoreboards().get(player.getName()).getScoreboard();

        if (scoreboard == null) {
            scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        }

        this.team = scoreboard.getTeam(player.getName());
        if (team == null) {
            this.team = scoreboard.registerNewTeam(player.getName());
            this.team.addEntry(player.getName());
        }
    }

    public NameTagEditor setNameColor(ChatColor color) {
        this.team.setColor(color);
        return this;
    }

    public NameTagEditor resetNameColor() {
        this.team.setColor(ChatColor.RESET);
        return this;
    }

    public NameTagEditor setPrefix(String newPrefix) {
        team.setPrefix(newPrefix);
        return this;
    }

    public NameTagEditor addPrefix(String addPrefix, boolean before) {
        if (before) {
            team.setPrefix(addPrefix + team.getPrefix());
        } else {
            team.setPrefix(team.getPrefix() + addPrefix);
        }
        return this;
    }

    public NameTagEditor setSuffix(String newSuffix) {
        team.setSuffix(newSuffix);
        return this;
    }

    public NameTagEditor setSuffix(String addSuffix, boolean before) {
        if (before) {
            team.setPrefix(addSuffix + team.getPrefix());
        } else {
            team.setPrefix(team.getPrefix() + addSuffix);
        }
        return this;
    }

    public Team getTeam() {
        return team;
    }

    public NameTagEditor setChatName(String chatName) {
        player.setDisplayName(chatName);
        return this;
    }

    public NameTagEditor resetChatName() {
        player.setDisplayName(team.getPrefix() + "§r" + player.getName() + team.getSuffix());
        return this;
    }

    public NameTagEditor setTabName(String chatName) {
        player.setPlayerListName(chatName);
        return this;
    }

    public NameTagEditor resetTabName() {
        player.setPlayerListName(this.player.getName());
        return this;
    }
}


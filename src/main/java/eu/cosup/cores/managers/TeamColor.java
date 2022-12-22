package eu.cosup.cores.managers;

import org.bukkit.ChatColor;

public enum TeamColor {

    RED,
    BLUE,
    SPECTATOR;

    public static ChatColor getChatColor(TeamColor teamColor) {

        if (teamColor == TeamColor.SPECTATOR) {
            return ChatColor.GRAY;
        }

        return ChatColor.valueOf(teamColor.toString());
    }

}


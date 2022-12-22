package eu.cosup.cores.managers;

import org.bukkit.ChatColor;

public enum TeamColor {

    RED,
    BLUE;

    public static ChatColor getChatColor(TeamColor teamColor) {
        return ChatColor.valueOf(teamColor.toString());
    }

}


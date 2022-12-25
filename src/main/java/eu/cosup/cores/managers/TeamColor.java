package eu.cosup.cores.managers;

import org.bukkit.ChatColor;
import org.bukkit.Color;


public enum TeamColor {

    RED,
    BLUE;

    public static ChatColor getChatColor(TeamColor teamColor) {

        return ChatColor.valueOf(teamColor.toString());
    }

    // cheeky way maybe there is a better method
    public static Color getColor(TeamColor teamColor) {

        if (teamColor == TeamColor.RED) {

            return Color.RED;
        }

        if (teamColor == TeamColor.BLUE) {

            return Color.BLUE;
        }
        return Color.GRAY;
    }
}


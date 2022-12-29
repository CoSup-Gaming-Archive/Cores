package eu.cosup.cores.managers;

import eu.cosup.cores.utility.ColorUtility;
import org.bukkit.ChatColor;
import org.bukkit.Color;


public enum TeamColor {

    RED,
    BLUE;

    // cheeky way maybe there is a better method
    public static Color getColor(TeamColor teamColor) {

        if (teamColor == TeamColor.RED) {
            return ColorUtility.getStdColor("red");
        }

        if (teamColor == TeamColor.BLUE) {
            return ColorUtility.getStdColor("blue");
        }
        return Color.GRAY;
    }
    public static ChatColor getChatColor(TeamColor teamColor){
        if (teamColor == TeamColor.RED) {
            return ChatColor.RED;
        }

        if (teamColor == TeamColor.BLUE) {
            return ChatColor.BLUE;
        }
        return ChatColor.GRAY;
    }
}


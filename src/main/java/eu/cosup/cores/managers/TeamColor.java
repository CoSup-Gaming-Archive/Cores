package eu.cosup.cores.managers;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Color;


public enum TeamColor {

    RED,
    BLUE;

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
    public static ChatColor getChatColor(TeamColor teamColor){
        if (teamColor == TeamColor.RED) {
            return ChatColor.RED;
        }

        if (teamColor == TeamColor.BLUE) {
            return ChatColor.BLUE;
        }
        return ChatColor.GRAY;
    }

    public static NamedTextColor getNamedTextColor(TeamColor teamColor){

        if (teamColor == TeamColor.RED) {
            return NamedTextColor.RED;
        }

        if (teamColor == TeamColor.BLUE) {
            return NamedTextColor.BLUE;
        }
        return NamedTextColor.GRAY;
    }
}


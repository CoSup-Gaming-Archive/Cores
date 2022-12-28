package eu.cosup.cores.utility;


import eu.cosup.cores.Cores;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

// TODO clean this up
public class ColorUtility {

    public static TextColor getStdTextColor(String colorName) {
        ConfigurationSection configurationSection = Cores.getInstance().getConfig().getConfigurationSection("std-colors");
        for (String string : configurationSection.getKeys(false)) {
            if (string.contains(colorName.toLowerCase())) {

                Color color = getColorFromString(configurationSection.getString(string));
                TextColor.fromCSSHexString("\\u00A76");
                return TextColor.color(color.getRed(), color.getGreen(), color.getBlue());

            }
        }

        return TextColor.color(0);
    }

    private static Color getColorFromString(String string) {

        String[] values = string.split(",");

        Color color;
        try {
            color = Color.fromRGB(Integer.valueOf(values[0]), Integer.valueOf(values[1]), Integer.valueOf(values[2]));
        } catch (IllegalArgumentException exception) {
            exception.printStackTrace();
            Bukkit.getLogger().severe("Probably wrong values in config make sure you have std-colors in your config");
            return Color.BLACK;
        }

        return color;

    }

    public static Color getStdColor(String colorName) {
        ConfigurationSection configurationSection = Cores.getInstance().getConfig().getConfigurationSection("std-colors");
        for (String string : configurationSection.getKeys(false)) {
            if (string.contains(colorName.toLowerCase())) {

                return getColorFromString(configurationSection.getString(string));

            }
        }

        return Color.BLACK;
    }

}


package eu.cosup.cores.builders;

import org.bukkit.ChatColor;

public enum EntryName {

    ENTRY_0(0, ChatColor.BLACK.toString()),
    ENTRY_1(1, ChatColor.DARK_BLUE.toString()),
    ENTRY_2(2, ChatColor.DARK_GREEN.toString()),
    ENTRY_3(3, ChatColor.DARK_AQUA.toString()),
    ENTRY_4(4, ChatColor.DARK_RED.toString()),
    ENTRY_5(5, ChatColor.DARK_PURPLE.toString()),
    ENTRY_6(6, ChatColor.GOLD.toString()),
    ENTRY_7(7, ChatColor.GRAY.toString()),
    ENTRY_8(8, ChatColor.DARK_GRAY.toString()),
    ENTRY_9(9, ChatColor.BLUE.toString()),
    ;

    private final int entry;
    private final String entryName;

    EntryName(int entry, String entryName) {
        this.entry = entry;
        this.entryName = entryName;
    }

    public int entry() {
        return this.entry;
    }
    public String entryName() {
        return this.entryName;
    }
}

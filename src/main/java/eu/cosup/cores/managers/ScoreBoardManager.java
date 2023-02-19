package eu.cosup.cores.managers;

import eu.cosup.cores.Cores;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoardManager {

    private Objective objective;

    private String name;
    private List<Component> displayStrings = new ArrayList<>();

    public ScoreBoardManager(String name) {
        if (name.length() == 0) {
            return;
        }

        this.name = name;
        registerScoreBoard(name);
    }

    private void registerScoreBoard(String name) {
        try {
            // try registering the objective
            this.objective = Cores.getInstance().getServer().getScoreboardManager().getMainScoreboard().registerNewObjective(
                    name,
                    Criteria.create("dummy"),
                    Component.text("name")
            );
        } catch (IllegalArgumentException ignored) {
            // if the objective already exists
            this.objective = Cores.getInstance().getServer().getScoreboardManager().getMainScoreboard().getObjective(name);
        }
    }

    public void setDisplayName(String name) {
        objective.setDisplayName(name);
    }

    public void setSlot(DisplaySlot displaySlot) {
        objective.setDisplaySlot(displaySlot);
    }

    public void addItem(Component component) {
        displayStrings.add(component);
    }

    public void refreshObjective() {


        int i = 0;
        for (Component displayString : displayStrings) {
            String displayText = LegacyComponentSerializer.legacy(LegacyComponentSerializer.HEX_CHAR).serialize(displayString);
            this.objective.getScore(ChatColor.translateAlternateColorCodes('#', displayText)).setScore(i);
            i--;
        }
    }

    public void clearObjective() {
        displayStrings = new ArrayList<>();
        String id = objective.getName();
        Scoreboard scoreboard = objective.getScoreboard();
        Component name = objective.displayName();
        objective.unregister();

        if (scoreboard == null) {
            registerScoreBoard(this.name);
        }

        assert scoreboard != null;
        objective = scoreboard.registerNewObjective(id, Criteria.create("dummy"), name);
    }
}


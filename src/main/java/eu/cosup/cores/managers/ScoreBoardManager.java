package eu.cosup.cores.managers;

import eu.cosup.cores.Cores;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreBoardManager {

    private Objective objective;
    ArrayList<String> displayStrings = new ArrayList<>();

    public ScoreBoardManager(String name) {
        if (name.length() == 0) {
            return;
        }

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

    public void addItem(String item) {
        displayStrings.add(item);
    }

    public void getObjective() {
        int displayStringCount = displayStrings.size() - 1;
        for (int i = 0; i < displayStrings.size(); i++) {
            this.objective.getScore(displayStrings.get(i)).setScore(displayStringCount - i);
        }
    }

    public void clearObjective() {
        String id = objective.getName();
        Scoreboard scoreboard = objective.getScoreboard();
        Component name = objective.displayName();
        objective.unregister();

        if (scoreboard == null) {
            // TODO: Handle this case
        }

        // TODO: and if the case is handled then remove the following line:
        assert scoreboard != null;
        objective = scoreboard.registerNewObjective(id, Criteria.create("dummy"), name);
    }
}


package eu.cosup.cores.managers;

import eu.cosup.cores.Cores;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreBoardManager{

    private Objective objective;
    ArrayList<String> displayStrings = new ArrayList<>();
    public ScoreBoardManager(String name){
        if (name.length() == 0) {
            return;
        }

        try {
            // try registering the objective
            this.objective = Cores.getInstance().getServer().getScoreboardManager().getMainScoreboard().registerNewObjective(name, "dummy", "name");
        } catch (IllegalArgumentException ignored) {
            // if the objective already exists
            this.objective = Cores.getInstance().getServer().getScoreboardManager().getMainScoreboard().getObjective(name);
        }
        Bukkit.getLogger().info(""+objective);
    }

    public void setDisplayName(String name){
        objective.setDisplayName(name);
    }
    public void setSlot(DisplaySlot displaySlot){
        objective.setDisplaySlot(displaySlot);
    }
    public void addItem(String item){
        displayStrings.add(item);
    }

    public Objective getObjective(){
        int displayStringCount = displayStrings.size()-1;
        for (int i = 0; i<displayStrings.size(); i++){
            this.objective.getScore(displayStrings.get(i)).setScore(displayStringCount-i);
        }
        return objective;
    }

    public void clearObjective(){
        String id = objective.getName();
        Scoreboard scoreboard = objective.getScoreboard();
        String name = objective.getDisplayName();
        objective.unregister();
        objective = scoreboard.registerNewObjective(id, "dummy", name);
    }
}


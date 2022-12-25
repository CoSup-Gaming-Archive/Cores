package eu.cosup.cores.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ScoreBoardManager{

    Objective o;
    ArrayList<String> show=new ArrayList<String >();
    public ScoreBoardManager(String name){
        if (name==""){return;}
        try {
            this.o= Bukkit.getScoreboardManager().getMainScoreboard().registerNewObjective(name, "dummy", "name");
        } catch (Exception e){

        }
    }
    public ScoreBoardManager setObjective(Objective o){
        this.o=o;
        return this;
    }
    public ScoreBoardManager setDName(String n){
        o.setDisplayName(n);
        return this;
    }
    public ScoreBoardManager setSlot(DisplaySlot ds){
        o.setDisplaySlot(ds);
        return this;
    }
    public ScoreBoardManager addItem(String item){
        show.add(item);
        return this;
    }
    public ScoreBoardManager setItem(Integer index, String item){
        show.set(index, item);
        return this;
    }
    public ScoreBoardManager removeItem(Integer index){
        show.remove(index);
        return this;
    }

    public Objective getObjective(){
        Integer len = show.size()-1;
        for (int i = 0; i<show.size(); i++){
            this.o.getScore(show.get(i)).setScore(len-i);
        }
        return o;
    }
    public ScoreBoardManager removeItem(String item){
        String id=o.getName();
        Scoreboard sb=o.getScoreboard();
        final HashMap<String, Integer> map = new HashMap<>();
        for (String entry : sb.getEntries()) {
            if(o.getScore(entry).isScoreSet()) {
                //p.sendMessage("§b" + oldScore + " XP §7(Level "+plugin.expManager.xpToLevel(oldScore) + ")");
                //p.sendMessage(entry);

                if (entry.equals(item)) {
                    //map.put("§b" + newScore + " XP §7(Level "+plugin.expManager.xpToLevel(newScore, p) + ")", o.getScore(entry).getScore());
                    continue;
                }
                map.put(entry, o.getScore(entry).getScore());
            }
        }
        String name = o.getDisplayName();
        o.unregister();
        o = sb.registerNewObjective(id, "dummy", name);
        for (final Map.Entry<String, Integer> entry : map.entrySet()){
            o.getScore(entry.getKey()).setScore(entry.getValue());
        }
        return this;
    }
    public ScoreBoardManager replaceItem(String replace, String with){
        String id=o.getName();
        Scoreboard sb=o.getScoreboard();
        final HashMap<String, Integer> map = new HashMap<>();
        for (String entry : sb.getEntries()) {
            if(o.getScore(entry).isScoreSet()) {
                //p.sendMessage("§b" + oldScore + " XP §7(Level "+plugin.expManager.xpToLevel(oldScore) + ")");
                //p.sendMessage(entry);

                if (entry.equals(replace)) {
                    map.put(with, o.getScore(entry).getScore());
                    continue;
                }
                map.put(entry, o.getScore(entry).getScore());
            }
        }
        String name = o.getDisplayName();
        o.unregister();
        o = sb.registerNewObjective(id, "dummy", name);
        for (final Map.Entry<String, Integer> entry : map.entrySet()){
            o.getScore(entry.getKey()).setScore(entry.getValue());
        }

        return this;
    }
    public ScoreBoardManager clearObjective(){
        String id=o.getName();
        Scoreboard sb=o.getScoreboard();
        String name = o.getDisplayName();
        o.unregister();
        o = sb.registerNewObjective(id, "dummy", name);
        return this;
    }
    public void showScoreboardToPlayer(Player p){
        o=getObjective();
        p.setScoreboard(o.getScoreboard());
    }

}


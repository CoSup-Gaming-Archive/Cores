package eu.cosup.cores.managers;

import eu.cosup.cores.Game;
import eu.cosup.cores.utility.someMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import javax.annotation.Nullable;

public class BeaconInformation {
    public static void update(){
        //Bukkit.getLogger().info(String.valueOf(Game.getGameInstance().getPlayerList().size()));
        for (Player p: Bukkit.getOnlinePlayers()){
            /*Team t=Game.getGameInstance().getTeamManager().getTeamByColor(Game.getGameInstance().getTeamManager().whichTeam(p));
            String onTeam="";
            if (t==null){
                onTeam="&7You Specate";
            } else {
                onTeam=TeamColor.getChatColor(t.getColor())+"You're team "+t.toString();
            }*/
            p.setPlayerListHeaderFooter(ChatColor.translateAlternateColorCodes('&', "&l&6CoSup&b Gaming"), ChatColor.translateAlternateColorCodes('&', "\n"+getText(TeamColor.BLUE, null)+"\n"+getText(TeamColor.RED, null)+"\n\n&bCores"));
            Bukkit.getLogger().info("updated for player "+p.getName());
        }
        ScoreBoardManager sbm=new ScoreBoardManager("e");
        sbm.setObjective(Bukkit.getScoreboardManager().getMainScoreboard().getObjective("main"));
        sbm.clearObjective();
        sbm.setDName(ChatColor.translateAlternateColorCodes('&', "&bCores")).addItem(" ").addItem(ChatColor.translateAlternateColorCodes('&', getText(TeamColor.BLUE, null))).addItem(ChatColor.translateAlternateColorCodes('&', getText(TeamColor.RED, null))).addItem("  ").addItem(ChatColor.translateAlternateColorCodes('&', "   &6CoSup &bGaming")).setSlot(DisplaySlot.SIDEBAR).getObjective();
        sbm.setSlot(DisplaySlot.SIDEBAR).getObjective();
        //sb
    }
    public static String getText(TeamColor teamColor, @Nullable Integer beacons){
        if (teamColor!=TeamColor.RED && teamColor!=TeamColor.BLUE){
            return null;
        }
        String f="";
        if (teamColor.equals(TeamColor.RED)){
            f="&cRed Beacons: ";
            Integer b;
            if (beacons!=null){
                b=beacons;
            } else {
                b= someMethods.getBeaconCount(TeamColor.RED);
            }

            if (b==0){
                f+="&c\u2716 &c\u2716";
            } else if (b==1){
                f+="&a\u2714 &c\u2716";
            } else if (b==2){
                f+="&a\u2714 &a\u2714";
            }
        } else if (teamColor.equals(TeamColor.BLUE)){
            f="&9Blue Beacons: ";
            Integer b;
            if (beacons!=null){
                b=beacons;
            } else {
                b=someMethods.getBeaconCount(TeamColor.BLUE);
            }

            if (b==0){
                f+="&c\u2716 &c\u2716";
            } else if (b==1){
                f+="&a\u2714 &c\u2716";
            } else if (b==2){
                f+="&a\u2714 &a\u2714";
            }
        }

        return f;
    }
}

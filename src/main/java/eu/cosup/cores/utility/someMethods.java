package eu.cosup.cores.utility;

import eu.cosup.cores.Game;
import eu.cosup.cores.managers.TeamColor;
import org.bukkit.Bukkit;


public class someMethods {
    public static Integer getBeaconCount(TeamColor teamColor){
        if (!(teamColor==TeamColor.BLUE) && !(teamColor==TeamColor.RED)){
            Bukkit.getLogger().info(teamColor.toString());
            return null;

        }
        Integer bc=2;
        try {
            bc= Game.getGameInstance().getTeamManager().getTeamByColor(teamColor).getBeaconCount();
        } catch (Exception e){
            bc=2;
        }
        return bc;
    }
}

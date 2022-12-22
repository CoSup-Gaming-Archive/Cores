package eu.cosup.cores.managers;

import eu.cosup.cores.Cores;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class TeamManager {

    private ArrayList<Team> teams = new ArrayList<>();

    public ArrayList<Team> getTeams() {
        return teams;
    }

    // this will probably be thrown away later
    public void makeTeams(ArrayList<Player> players) {

        // we only have two teams

        ArrayList<Player> teamPlayers = new ArrayList<>();

        // filter out spectator noone likes him
        // this wont work in future since parties will be athing im guessing
        for (Object teamColor : Arrays.stream(TeamColor.values()).filter(teamColor -> teamColor != TeamColor.SPECTATOR).toArray()) {

            Bukkit.getLogger().info(""+teamColor.toString());

            for (Player player : players) {

                if (teamPlayers.size() >= players.size()/2) {

                    teams.add(new Team((TeamColor) teamColor, teamPlayers));

                    teamPlayers = new ArrayList<>();

                }

                teamPlayers.add(player);

            }
        }

    }

    // which team player is in
    public TeamColor whichTeam(Player player) {

        for (Team team : teams) {
            if (team.isPlayerInTeam(player)) {
                return team.getColor();
            }
        }

        // uhhhs
        return null;

    }
}

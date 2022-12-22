package eu.cosup.cores.managers;

import eu.cosup.cores.Cores;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class TeamManager {

    private ArrayList<Team> teams = new ArrayList();

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void makeTeams(ArrayList<Player> players) {

        // we only have two teams

        ArrayList<Player> teamPlayers = new ArrayList<>();

        for (TeamColor teamColor : TeamColor.values()) {

            for (Player player : players) {

                if (teamPlayers.size() >= players.size()/2) {

                    teams.add(new Team(teamColor, teamPlayers));

                    teamPlayers = new ArrayList<>();

                }

                teamPlayers.add(player);

            }
        }
    }

    // TODO make this work
    //public ArrayList<Team> getActiveTeams() {
    //    return teams.stream().filter(team ->
    //        team.isBeaconAlive() && team.hasAlivePlayers()
    //    );
    //}
}

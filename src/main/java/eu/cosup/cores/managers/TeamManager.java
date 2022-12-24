package eu.cosup.cores.managers;

import eu.cosup.cores.Game;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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

            Bukkit.getLogger().info(""+teamColor.toString()+" is being registered.");

            for (Player player : players) {

                if (teamPlayers.size() >= players.size()/2) {

                    if ((TeamColor) teamColor == TeamColor.BLUE) {
                        teams.add(new Team((TeamColor) teamColor, teamPlayers, Game.getGameInstance().getSelectedMap().getTeamBlueBeacons().size()));
                    }

                    if ((TeamColor) teamColor == TeamColor.RED) {
                        teams.add(new Team((TeamColor) teamColor, teamPlayers, Game.getGameInstance().getSelectedMap().getTeamRedBeacons().size()));
                    }

                    teamPlayers = new ArrayList<>();

                }

                teamPlayers.add(player);

            }
        }

        teams.add(new Team(TeamColor.SPECTATOR, new ArrayList<>(), 0));
    }

    // which team player is in
    public TeamColor whichTeam(Player player) {

        for (Team team : teams) {
            if (team.isPlayerInTeam(player)) {
                return team.getColor();
            }
        }

        return null;

    }

    public Team getTeamByColor(TeamColor teamColor) {

        for (Team team : teams) {

            if (Objects.equals(team.getColor().toString(), teamColor.toString())) {
                return team;
            }

        }

        return null;

    }

    public void addPlayerToTeam(Player player, TeamColor teamColor) {

        getTeamByColor(teamColor).addPlayerToTeam(player);

    }
}

package eu.cosup.cores.managers;

import eu.cosup.cores.Cores;
import eu.cosup.cores.core.data.Team;
import eu.cosup.cores.core.data.TeamColor;
import eu.cosup.tournament.common.objects.GameTeam;
import eu.cosup.tournament.server.TournamentServer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TeamManager {

    private final ArrayList<Team> teams = new ArrayList<>();

    public ArrayList<Team> getTeams() {
        return teams;
    }

    // this will probably be thrown away later
    public void makeTeams() {

        List<GameTeam> gameTeams = TournamentServer.getInstance().getTeams();

        for (int i = 0; i < gameTeams.size(); i++) {
            TeamColor teamColor = TeamColor.values()[i];
            List<Player> teamPlayers = new ArrayList<>();
            for (UUID playerUUID : gameTeams.get(i).getPlayerUUIDs()) {
                Player player = Cores.getInstance().getServer().getPlayer(playerUUID);
                if (player != null) {
                    teamPlayers.add(player);
                }
            }
            teams.add(new Team(teamColor, teamPlayers, true, gameTeams.get(i).getSlug(), gameTeams.get(i).getName()));
        }
    }

    public @Nullable Team getTeamWithName(@NotNull String name) {

        for (Team team : teams) {
            if (name.toLowerCase().contains(team.getColor().toString().toLowerCase())) {
                return team;
            }
        }

        return null;
    }

    // which team player is in
    public @Nullable Team whichTeam(@NotNull UUID playerUUID) {

        for (Team team : teams) {

            if (team.isPlayerInTeam(playerUUID)) {
                return team;
            }
        }

        return null;
    }

    public @Nullable Team getTeamByColor(TeamColor teamColor) {

        if (teamColor == null) {
            return null;
        }

        for (Team team : teams) {
            if (Objects.equals(team.getColor().toString(), teamColor.toString())) {
                return team;
            }
        }
        return null;
    }
}

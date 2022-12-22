package eu.cosup.cores.managers;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {

    ArrayList<Player> players = new ArrayList<>();
    private final TeamColor color;

    public Team(TeamColor teamColor, ArrayList<Player> players) {
        this.players = players;
        this.color = teamColor;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public TeamColor getColor() {
        return color;
    }

    public boolean isPlayerInTeam(Player player) {
        return players.contains(player);
    }


}

package eu.cosup.cores.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {

    ArrayList<Player> players;
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

    // check for player UUID and not player object beacues yes
    public boolean isPlayerInTeam(Player player) {
        for (Player player1 : players) {
            if (player1.getName().equals(player.getName())) {
                Bukkit.getLogger().info(player1.getUniqueId()+"   "+player.getUniqueId());
                return true;
            }
        }
        return false;
    }
}

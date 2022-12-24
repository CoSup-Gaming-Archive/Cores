package eu.cosup.cores.managers;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Team {

    ArrayList<Player> players;
    private final TeamColor color;
    private int beaconCount;

    public Team(TeamColor teamColor, ArrayList<Player> players, int beaconCount) {
        this.players = players;
        this.color = teamColor;
        this.beaconCount = beaconCount;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public TeamColor getColor() {
        return color;
    }

    public int getBeaconCount() {
        return beaconCount;
    }

    public void loseBeacon() {
        this.beaconCount--;
    }

    // check for player UUID and not player object beacues yes
    public boolean isPlayerInTeam(Player player) {
        for (Player player1 : players) {
            if (player1.getName().equals(player.getName())) {
                return true;
            }
        }
        return false;
    }

    public void addPlayerToTeam(Player player) {

        if (player == null) {
            throw new NullPointerException("Player was null nonono");
        }

        players.add(player);

    }
}

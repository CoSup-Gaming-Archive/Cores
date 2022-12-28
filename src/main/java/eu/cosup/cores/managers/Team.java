package eu.cosup.cores.managers;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Team {

    ArrayList<Player> players;
    private final TeamColor color;
    private int beaconCount;
    // TODO: final?
    private int maxBeaconCount;

    public Team(TeamColor teamColor, ArrayList<Player> players, int beaconCount) {
        this.players = players;
        this.color = teamColor;
        this.beaconCount = beaconCount;
        maxBeaconCount = beaconCount;
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

    public int getMaxBeaconCount() {
        return maxBeaconCount;
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
}

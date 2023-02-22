package eu.cosup.cores.events;

import eu.cosup.cores.interfaces.TeamListener;
import eu.cosup.cores.objects.TeamColor;

import java.util.ArrayList;
import java.util.List;


public record TeamChangeAliveEvent(boolean teamAlive, TeamColor teamColor) {

    private static List<TeamListener> listeners = new ArrayList<>();

    public TeamChangeAliveEvent(boolean teamAlive, TeamColor teamColor) {
        this.teamAlive = teamAlive;
        this.teamColor = teamColor;
        changeGamePhase();
    }

    public static void addListener(TeamListener listener) {
        listeners.add(listener);
    }

    private void changeGamePhase() {
        for (TeamListener listener : listeners)
            listener.firedTeamChangeState(this);
    }
}

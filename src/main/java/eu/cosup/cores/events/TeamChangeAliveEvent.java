package eu.cosup.cores.events;

import eu.cosup.cores.core.data.TeamColor;
import eu.cosup.cores.core.interfaces.TeamListener;

import java.util.ArrayList;
import java.util.List;


public record TeamChangeAliveEvent(boolean teamAlive, TeamColor teamColor) {

    private static final List<TeamListener> listeners = new ArrayList<>();

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

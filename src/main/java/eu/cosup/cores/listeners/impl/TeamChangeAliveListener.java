package eu.cosup.cores.listeners.impl;

import eu.cosup.cores.core.interfaces.TeamListener;
import eu.cosup.cores.events.TeamChangeAliveEvent;


public class TeamChangeAliveListener implements TeamListener {

    public TeamChangeAliveListener() {
        TeamChangeAliveEvent.addListener(this);
    }


    @Override
    public void firedTeamChangeState(TeamChangeAliveEvent event) {

    }
}

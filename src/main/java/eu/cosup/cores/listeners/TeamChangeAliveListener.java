package eu.cosup.cores.listeners;

import eu.cosup.cores.events.TeamChangeAliveEvent;
import eu.cosup.cores.interfaces.TeamListener;


public class TeamChangeAliveListener implements TeamListener {

    public TeamChangeAliveListener() {
        TeamChangeAliveEvent.addListener(this);
    }


    @Override
    public void firedTeamChangeState(TeamChangeAliveEvent event) {

    }
}

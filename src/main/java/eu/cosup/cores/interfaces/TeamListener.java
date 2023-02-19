package eu.cosup.cores.interfaces;

import eu.cosup.cores.events.TeamChangeAliveEvent;

public interface TeamListener {

    void firedTeamChangeState(TeamChangeAliveEvent event);

}

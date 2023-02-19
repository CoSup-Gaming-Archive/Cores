package eu.cosup.cores.interfaces;

import eu.cosup.cores.events.ChangeGamePhaseEvent;
import eu.cosup.cores.events.ChangeGameStateEvent;

public interface GameListener {
    void firedChangeGamePhaseEvent(ChangeGamePhaseEvent event);
    void firedChangeGameStateEvent(ChangeGameStateEvent event);
}

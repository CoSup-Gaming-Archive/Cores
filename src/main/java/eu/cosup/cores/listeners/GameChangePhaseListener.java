package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.events.ChangeGamePhaseEvent;
import eu.cosup.cores.events.ChangeGameStateEvent;
import eu.cosup.cores.interfaces.GameListener;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.objects.Team;
import eu.cosup.cores.objects.TeamColor;
import eu.cosup.cores.tasks.TeamLoseBeaconTask;
import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.List;


// this could be usefull for later
public class GameChangePhaseListener implements GameListener {

    public GameChangePhaseListener() {
        ChangeGamePhaseEvent.addListener(this);
        ChangeGameStateEvent.addListener(this);
    }

    @Override
    public void firedChangeGamePhaseEvent(@NotNull ChangeGamePhaseEvent event) {

        if (event.newGamePhase() == GameStateManager.GamePhase.ARENA) {

        }

        if (event.newGamePhase() == GameStateManager.GamePhase.BEACON_DESTRUCTION) {

            Component msg = Component.text().content("All beacons have been destroyed!").color(NamedTextColor.RED).build();

            Title title = Title.title(msg, Component.text().build());

            for (Player player : Cores.getInstance().getServer().getOnlinePlayers()) {
                player.showTitle(title);
            }

            for (Pair<Location, Location> beaconLocations : Game.getGameInstance().getSelectedMap().getTeamBeacons().values()) {
                new TeamLoseBeaconTask(beaconLocations.first(), null);
                new TeamLoseBeaconTask(beaconLocations.second(), null);
            }

            Cores.getInstance().getGameWorld().playSound(
                    Game.getGameInstance().getSelectedMap().getSpectatorSpawn(),
                    Sound.ENTITY_ENDER_DRAGON_AMBIENT,
                    20,
                    1
            );
        }
    }

    @Override
    public void firedChangeGameStateEvent(ChangeGameStateEvent event) {

        if (event.newGameState().equals(GameStateManager.GameState.ACTIVE)) {
            Game.getGameInstance().activateGame();
        }

        if (event.newGameState().equals(GameStateManager.GameState.ENDING)) {
            List<Team> teams = Game.getGameInstance().getTeamManager().getTeams().stream().filter(team1 -> team1.getAlivePlayers().size() > 0).toList();
            if (teams.size() == 0) {
                Game.getGameInstance().finishGame(null);
            }
            if (teams.size() == 1) {
                Game.getGameInstance().finishGame(teams.get(0).getColor());
            }
            if (teams.size() == 2) {
                // TODO team with more players wins or if its a draw
            }
        }
    }
}

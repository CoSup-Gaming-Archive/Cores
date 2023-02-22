package eu.cosup.cores.listeners;

import eu.cosup.cores.Cores;
import eu.cosup.cores.Game;
import eu.cosup.cores.events.ChangeGamePhaseEvent;
import eu.cosup.cores.events.ChangeGameStateEvent;
import eu.cosup.cores.interfaces.GameListener;
import eu.cosup.cores.managers.GameStateManager;
import eu.cosup.cores.objects.Team;
import eu.cosup.cores.tasks.ActivateGameTask;
import eu.cosup.cores.tasks.TeamLoseBeaconTask;
import it.unimi.dsi.fastutil.Pair;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
            Cores.getInstance().setGameWorld(Bukkit.createWorld(new WorldCreator("arena")));

            Cores.getInstance().getGameWorld().getEntities().forEach(Entity::remove);

            // teleport all the player to the arena
            Cores.getInstance().getServer().getOnlinePlayers().forEach(player -> {
                player.teleport(new Location(
                        Cores.getInstance().getGameWorld(),
                        15.7,
                        -45,
                        10));

                player.getInventory().clear();
                ActivateGameTask.preparePlayerStats(player);
                ActivateGameTask.givePlayerArmor(player);
                Game.getGameInstance().updatePlayersNameTag(player);
                ActivateGameTask.givePlayerTools(player);
            });

            // teleport team one to their spawn
            for (Player alivePlayer : Game.getGameInstance().getTeamManager().getTeams().get(0).getAlivePlayers()) {

                alivePlayer.teleport(new Location(
                        Cores.getInstance().getGameWorld(),
                        -8.6,
                        -58,
                        9.3
                ));
            }

            // teleport team two to their spawn
            for (Player alivePlayer : Game.getGameInstance().getTeamManager().getTeams().get(1).getAlivePlayers()) {

                alivePlayer.teleport(new Location(
                        Cores.getInstance().getGameWorld(),
                        36,
                        -58,
                        9.6
                ));
            }
        }

        if (event.newGamePhase() == GameStateManager.GamePhase.BEACON_DESTRUCTION) {

            Component msg = Component.text().content("All beacons have been destroyed!").color(NamedTextColor.RED).build();

            Title title = Title.title(msg, Component.text().build());

            for (Player player : Cores.getInstance().getServer().getOnlinePlayers()) {
                player.showTitle(title);
            }

            for (Pair<Location, Location> beaconLocations : Game.getGameInstance().getSelectedMap().getTeamBeacons().values()) {
                new TeamLoseBeaconTask(beaconLocations.left(), null);
                new TeamLoseBeaconTask(beaconLocations.right(), null);
            }

            Cores.getInstance().getGameWorld().playSound(
                    Game.getGameInstance().getSelectedMap().getSpectatorSpawn(),
                    Sound.ENTITY_LIGHTNING_BOLT_IMPACT,
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

                Component notificationMessage = Component.text("Team with more players wins!").append(Component.text("-------------------------------------------")).color(NamedTextColor.RED);
                Cores.getInstance().getServer().broadcast(notificationMessage);

                if (teams.get(0).getAlivePlayers().size() == teams.get(1).getAlivePlayers().size()) {

                    Cores.getInstance().getServer().getOnlinePlayers().forEach(player -> {
                        player.showTitle(Title.title(Component.text("Draw").color(NamedTextColor.GRAY), Component.text("No one won").color(NamedTextColor.YELLOW)));
                    });
                    Game.getGameInstance().finishGame(null);

                    return;
                }

                Team winnerTeam;
                Team loserTeam;

                if (teams.get(0).getAlivePlayers().size() > teams.get(1).getAlivePlayers().size()) {

                    winnerTeam = teams.get(0);
                    loserTeam = teams.get(1);

                } else {
                    winnerTeam = teams.get(1);
                    loserTeam = teams.get(0);
                }

                winnerTeam.getAlivePlayers().forEach(winnerTeamPlayer -> {
                    Title title = Title.title(Component.text("You are the winner").color(NamedTextColor.GREEN), Component.text("Congrats!").color(NamedTextColor.YELLOW));
                    winnerTeamPlayer.showTitle(title);
                });

                loserTeam.getAlivePlayers().forEach(loserTeamPlayer -> {
                    Title title = Title.title(Component.text("Game over").color(NamedTextColor.RED), Component.text("You lost :(").color(NamedTextColor.YELLOW));
                    loserTeamPlayer.showTitle(title);
                });

                Game.getGameInstance().finishGame(winnerTeam.getColor());
            }
        }
    }
}

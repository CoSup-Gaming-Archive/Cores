package eu.cosup.cores.core.utility.builders;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.util.Objects.requireNonNull;

public abstract class ScoreboardBuilder {

    private final Scoreboard scoreboard;
    private final Objective objective;

    private final Player player;

    public ScoreboardBuilder(@NotNull Player player, @NotNull String name) {
        requireNonNull(player, "player cannot be null");
        requireNonNull(name, "name cannot be null");

        this.player = player;

        if (player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard())
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        this.scoreboard = player.getScoreboard();

        if (this.scoreboard.getObjective(name) != null)
            requireNonNull(this.scoreboard.getObjective(name)).unregister();

        this.objective = this.scoreboard.registerNewObjective(name, Criteria.DUMMY, Component.empty());

        this.build();
        this.update();
    }

    public void displayname(Component component) {
        this.objective.displayName(component);
    }

    public void displaySlot(DisplaySlot displaySlot) {
        this.objective.setDisplaySlot(displaySlot);
    }

    public void addScore(String entry, int score) {
        this.objective.getScore(entry).setScore(score);
    }
    public void removeScore(String entry) {
        this.scoreboard.resetScores(entry);
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Player getPlayer() {
        return player;
    }

    public Objective getObjective() {
        return objective;
    }

    public void addUpdatingScore(@NotNull String entry, int score, @NotNull String name, @Nullable Component prefix, @Nullable Component suffix) {
        this.objective.getScore(entry).setScore(score);
        Team team = this.scoreboard.registerNewTeam(name);
        team.prefix(prefix);
        team.suffix(suffix);
        team.addEntry(entry);
    }
    public void removeUpdatingScore(@NotNull String entry, @NotNull String name) {
        this.scoreboard.resetScores(entry);
        this.scoreboard.getTeam(name).unregister();
    }

    public void updateScore(@NotNull String name, @Nullable Component prefix, @Nullable Component suffix) {
        Team team = this.scoreboard.getTeam(name);
        assert team != null;
        team.prefix(prefix);
        team.suffix(suffix);
    }

    public void addTeam(@NotNull String name, @Nullable Component prefix, @Nullable Component suffix) {
        Team team = this.scoreboard.registerNewTeam(name);
        team.prefix(prefix);
        team.suffix(suffix);
    }
    public void removeTeam(@NotNull String name) {
        requireNonNull(this.scoreboard.getTeam(name)).unregister();
    }
    public Team getTeam(@NotNull String name) {
        return this.scoreboard.getTeam(name);
    }


    public abstract void build();
    public abstract void update();

}

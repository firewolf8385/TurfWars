package net.jadedmc.turfwars.game;

import net.jadedmc.turfwars.TurfWars;
import net.jadedmc.turfwars.game.arena.Arena;
import net.jadedmc.turfwars.game.team.Team;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Manages all active games.
 */
public class GameManager {
    private final TurfWars plugin;
    private final Collection<Game> games = new HashSet<>();

    /**
     * Creates the Game Manager and loads all available arenas.
     * @param plugin Instance of the plugin.
     */
    public GameManager(TurfWars plugin) {
        this.plugin = plugin;

        for(Arena arena : plugin.getArenaManager().getArenas()) {
            games.add(new Game(plugin, arena));
        }
    }

    public void addToGame(Player player) {
        List<Game> possibleGames = new ArrayList<>();

        for(Game game : games) {
            // Skip if the game is running.
            if(game.getGameState() != GameState.WAITING && game.getGameState() != GameState.COUNTDOWN) {
                continue;
            }

            possibleGames.add(game);
        }

        // Shuffles list of possible games.
        Collections.shuffle(possibleGames);

        // Returns null if no games are available.
        if(possibleGames.size() == 0) {
            return;
        }

        // Checks if any of these games have players waiting.
        List<Game> possibleGamesWithPlayers = new ArrayList<>();
        for(Game game : possibleGames) {
            if(game.getPlayers().size() == 0) {
                continue;
            }

            possibleGamesWithPlayers.add(game);
        }

        // If there is a game with players waiting, return that one.
        if(!possibleGamesWithPlayers.isEmpty()) {
            possibleGamesWithPlayers.get(0).addPlayer(player);
            return;
        }

        possibleGames.get(0).addPlayer(player);
    }

    /**
     * Gets the game the player is currently in.
     * If they are not in a game, returns null.
     * @param player Player to get game of.
     * @return Game they are in.
     */
    public Game getGame(Player player) {
        for(Game game : games) {
            if(game.getPlayers().contains(player)) {
                return game;
            }
        }

        return null;
    }

    public Collection<Game> getGames() {
        return games;
    }
}
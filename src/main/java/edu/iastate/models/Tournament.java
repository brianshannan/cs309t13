package edu.iastate.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonManagedReference;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import edu.iastate.dao.GameDao;
import edu.iastate.utils.JsonDateSerializer;
import edu.iastate.utils.MathUtils;
import edu.iastate.utils.TeamComparer;

/**
 * Tournament class
 *
 * @author brianshannan
 *
 */

@Entity
@Table(name = "Tournament")
public class Tournament {
    @Id
    @GeneratedValue
    @Column(name = "tournament_id")
    private int id;

    @Column(name = "tournament_name")
    private String name;

    @Column(name = "min_players")
    private int minPlayers;

    @Column(name = "max_players")
    private int maxPlayers;

    @Column(name = "teams_per_game")
    private int teamsPerGame;

    @Column(name = "officials_per_game")
    private int officialsPerGame;

    @JsonSerialize(using = JsonDateSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_start")
    private Date registrationStart;

    @JsonSerialize(using = JsonDateSerializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registration_close")
    private Date registrationClose;

    @Column(name = "is_started")
    private boolean isStarted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="coordinator_id")
    private Member gameCoordinator;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "tournament")
    private List<Team> teams;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "tournament")
    private List<Game> games;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Member getGameCoordinator() {
        return gameCoordinator;
    }

    public void setGameCoordinator(Member gameCoordinator) {
        this.gameCoordinator = gameCoordinator;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getTeamsPerGame() {
        return teamsPerGame;
    }

    public void setTeamsPerGame(int teamsPerGame) {
        this.teamsPerGame = teamsPerGame;
    }

    public int getOfficialsPerGame() {
        return officialsPerGame;
    }

    public void setOfficialsPerGame(int officialsPerGame) {
        this.officialsPerGame = officialsPerGame;
    }

    public Date getRegistrationStart() {
        return registrationStart;
    }

    public void setRegistrationStart(Date registrationStart) {
        this.registrationStart = registrationStart;
    }

    public Date getRegistrationClose() {
        return registrationClose;
    }

    public void setRegistrationClose(Date registrationClose) {
        this.registrationClose = registrationClose;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    /**
     * Adds a team to the tournament, doesn't add the team if it is null or
     * already is in the tournament.
     *
     * @param team The team to add to the tournament.
     */
    public void addTeam(Team team) {
        if(team == null || this.teams.contains(team)) {
            return;
        }
        this.teams.add(team);
        team.setTournament(this);
    }

    /**
     * Removes a team from the tournament.
     *
     * @param team The team to remove from the tournament.
     */
    public void removeTeam(Team team) {
        if(team == null || !this.teams.contains(team)) {
            return;
        }
        this.teams.remove(team);
        team.setTournament(null);
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    /**
     * The registration start date of the tournament formatted as a string,
     * empty string if the date is null.
     * 
     * @return The registration start date formatted as a string.
     */
    public String getRegistrationStartPretty() {
        if(this.registrationStart == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(this.registrationStart);
    }

    /**
     * The registration close date of the tournament formatted as a string,
     * empty string if the date is null.
     * 
     * @return The registration close date formatted as a string.
     */
    public String getRegistrationClosePretty() {
        if(this.registrationClose == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(this.registrationClose);
    }
    /**
     * Determines if a bracket has already been formed for the tournament. A
     * bracket has been formed if there are already games linked to the
     * tournament.
     *
     * @return
     */
    public boolean isBracketFormed() {
        return !this.games.isEmpty();
    }

    /**
     * Forms the bracket for the tournament. Doesn't do anything if the bracket
     * has already been formed.
     */
    public void formBracket(GameDao gameDao) {
        if(this.isBracketFormed()) {
            return;
        }
        // Get number of rounds without the play in games
        int roundsWithoutPlayin = (int) Math.floor(MathUtils.log(this.teams.size(), this.teamsPerGame));
        int numGamesFirstFullRound = (int) Math.pow(this.teamsPerGame, roundsWithoutPlayin - 1);
        int leftoverTeams = this.teams.size() - numGamesFirstFullRound * this.teamsPerGame;
        int leftoverTeamsPerPlayinGame = this.teamsPerGame - 1;

        // Get teams for play in games
        int numPlayinGames = (int) Math.ceil(1.0 * leftoverTeams / leftoverTeamsPerPlayinGame);
        int numPlayinTeams = leftoverTeams + numPlayinGames;

        List<Team> nonPlayinTeams = this.teams.subList(numPlayinTeams, this.teams.size());
        int roundNumber = 1;

        // If there are playin games, the first full round is round 2
        List<Game> playinGames = new ArrayList<Game>();
        if(numPlayinGames != 0) {
            List<Team> teamsPlayinGames = this.teams.subList(0, numPlayinTeams);
            playinGames = groupTeamsIntoGames(teamsPlayinGames, roundNumber, numPlayinGames, 0);
            roundNumber++;
        }

        // The teams that didn't have a play in game
        int numNonPlayinGames = (int) Math.ceil(1.0 * nonPlayinTeams.size() / this.teamsPerGame);
        int playinToFirst = (int) Math.ceil(1.0 * numPlayinGames / this.teamsPerGame);
        List<Game> secondRoundNonPlayinGames = groupTeamsIntoGames(nonPlayinTeams, roundNumber, numNonPlayinGames, playinToFirst);
        List<Game> secondRoundPlayinGames = new ArrayList<Game>();
        for(int i = 0; i < numPlayinGames / this.teamsPerGame; i++) {
            Game game = new Game();
            game.setTournament(this);
            game.setRoundNumber(2);
            secondRoundPlayinGames.add(game);
        }

        List<Game> secondRoundGames = new ArrayList<Game>();
        secondRoundGames.addAll(secondRoundPlayinGames);
        secondRoundGames.addAll(secondRoundNonPlayinGames);

        // Form and link the games
        formRoundsAndLink(secondRoundGames, roundNumber, gameDao);

        // If there's playin games
        if(numPlayinGames != 0) {
            // Playin games to second round games
            List<Integer> playinGamesToGames = getBalancedTeamsPerGame(numPlayinGames, secondRoundPlayinGames.size(), 0);

            int count = 0;
            for(int i = 0; i < playinGamesToGames.size(); i++) {
                Game nextGame = secondRoundPlayinGames.get(i);
                for(int j = 0; j < playinGamesToGames.get(i); j++) {
                    Game game = playinGames.get(count);
                    game.setNextGame(nextGame);
                    gameDao.createGame(game);
                    count++;
                }
            }

            // Account for the game where there's teams that haven't played a
            // game and teams advancing from playin games
            if(count != numPlayinGames) {
                Game nextGame = secondRoundNonPlayinGames.get(0);
                for(int i = 0; i < numPlayinGames - count; i++) {
                    Game game = playinGames.get(count);
                    game.setNextGame(nextGame);
                    gameDao.createGame(game);
                    count++;
                }
            }
        }
    }

    /**
     * Forms games for the given teams.
     *
     * @param currRoundTeams The teams to form games for.
     * @param roundNumber The round number for the games you are forming.
     * @param gamesNeeded The number of games you need for these teams.
     * @param playinToFirst How many slots to save in the first game for playin
     *            game winners.
     * @return A list of games formed based on the given teams.
     */
    public List<Game> groupTeamsIntoGames(List<Team> currRoundTeams, int roundNumber, int gamesNeeded, int playinToFirst) {
        List<Integer> teamsPerGame = getBalancedTeamsPerGame(currRoundTeams.size(), gamesNeeded, playinToFirst);
        List<Game> currRoundGames = new ArrayList<Game>();
        sortTeamsBasedOnSkill(currRoundTeams);
        int count = 0;
        for(int i = 0; i < gamesNeeded; i++) {
            Game game = new Game();
            game.setTournament(this);
            game.setRoundNumber(roundNumber);
            for(int j = 0; j < teamsPerGame.get(i); j++) {
                game.addTeam(currRoundTeams.get(count++));
            }
            currRoundGames.add(game);
        }

        return currRoundGames;
    }
    
    /**
     * Sorts the teams based on skill level from least skilled to
     * most skilled teams
     * 
     * @param currRoundTeams the teams to be sorted
     */
    private void sortTeamsBasedOnSkill(List<Team> currRoundTeams) {
        TeamComparer teamComparer = new TeamComparer();
        Collections.sort(currRoundTeams, teamComparer);
    }
    

    /**
     * Takes the games in the current round and forms the rest of the brackets
     * for the tournament, recursively.
     * 
     * @param currRoundGames The games in the current round.
     * @param roundNumber The round number.
     * @param gameDao A GameDao object that will allow the saving of games.
     */
    public void formRoundsAndLink(List<Game> currRoundGames, int roundNumber, GameDao gameDao) {
        if(currRoundGames.size() == 1) {
            Game game = currRoundGames.get(0);
            game.setTournament(this);
            game.setRoundNumber(roundNumber);
            gameDao.createGame(game);
            return;
        }

        // Form next round
        int nextRoundLen = (int) Math.ceil(1.0 * currRoundGames.size() / this.teamsPerGame);
        List<Integer> teamsPerGame = getBalancedTeamsPerGame(currRoundGames.size(), nextRoundLen, 0);
        List<Game> nextRoundGames = new ArrayList<Game>();

        for(int i = 0; i < nextRoundLen; i++) {
            Game game = new Game();
            nextRoundGames.add(game);
        }

        // Link next round with next next round
        formRoundsAndLink(nextRoundGames, roundNumber + 1, gameDao);

        // Link current round to next round
        int count = 0;
        for(int i = 0; i < nextRoundLen; i++) {
            Game nextGame = nextRoundGames.get(i);
            for(int j = 0; j < teamsPerGame.get(i); j++) {
                Game game = currRoundGames.get(count);
                game.setNextGame(nextGame);
                game.setTournament(this);
                game.setRoundNumber(roundNumber);
                gameDao.createGame(game);
                count++;
            }
        }
    }

    /**
     * Tries to balance (as evenly as possible) the number of teams that should
     * be in each game. Assumes there will be one winning team per game.
     *
     * @param currRoundCount The number of games in the current round.
     * @param nextRoundCount The number of games in the next round.
     * @param playinToFirst The number of slots to save in the first game for
     *            playin game winners
     * @return A list with the game number as the index and the number of teams
     *         that should be in it as the value at that index.
     */
    public List<Integer> getBalancedTeamsPerGame(int currRoundCount, int nextRoundCount, int playinToFirst) {
        if(nextRoundCount == 0) {
            return new ArrayList<Integer>();
        }

        Integer[] arr = new Integer[nextRoundCount];
        for(int i = 0; i < arr.length; i++) {
            arr[i] = 0;
        }

        int iterations = currRoundCount + playinToFirst;
        for(int i = 0; i < iterations; i++) {
            int ind = i % nextRoundCount;
            // Save slots for playin game winners
            if(ind == 0 && playinToFirst > 0) {
                playinToFirst--;
                continue;
            }
            // Don't allow more teams per game than the tournament settting
            if(arr[ind] >= this.teamsPerGame) {
                continue;
            }
            arr[ind]++;
        }

        return Arrays.asList(arr);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        Tournament other = (Tournament) obj;
        if(id != other.id)
            return false;
        return true;
    }
}

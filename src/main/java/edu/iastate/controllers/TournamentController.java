package edu.iastate.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.iastate.dao.GameDao;
import edu.iastate.dao.TeamDao;
import edu.iastate.dao.TournamentDao;
import edu.iastate.models.Member;
import edu.iastate.models.Team;
import edu.iastate.models.Tournament;

@Controller
@RequestMapping("/tournament")
public class TournamentController {

    @RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
    public String viewTournament(Model model, HttpSession session, @PathVariable int id) {
        model.addAttribute("tournamentId", id);

        if (session.getAttribute("member") == null) {
            return "redirect:denied";
        }

        TournamentDao tournamentDao = new TournamentDao();
        List<Tournament> tournaments = tournamentDao.getLastXTournaments(5);
        model.addAttribute("tournaments", tournaments);
        return "tournament";
    }

    // TODO check users permission
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody void createTournament(@RequestParam(value = "name") String name,
            @RequestParam(value = "minPlayersPerTeam") int minPlayers,
            @RequestParam(value = "maxPlayersPerTeam") int maxPlayers,
            @RequestParam(value = "teamsPerGame") int teamsPerGame,
            @RequestParam(value = "officialsPerGame") int officialsPerGame) {
        Tournament tournament = new Tournament();
        tournament.setName(name);
        tournament.setMinPlayers(minPlayers);
        tournament.setMaxPlayers(maxPlayers);
        tournament.setTeamsPerGame(teamsPerGame);
        tournament.setOfficialsPerGame(officialsPerGame);

        TournamentDao tournamentDao = new TournamentDao();
        tournamentDao.saveTournament(tournament);
    }

    /**
     * Returns the tournament given by id as JSON.
     * 
     * @param id The id of the tournament.
     * @return JSON representation of the tournament given by id.
     */
    // TODO check if valid tournament
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Tournament getTournamentById(@PathVariable int id) {
        TournamentDao tournamentDao = new TournamentDao();
        return tournamentDao.getTournamentById(id, true, true);
    }

    /**
     * Updates the tournament given by id with the given parameters.
     * 
     * @param id The id of the tournament.
     * @param name The new name for the tournament.
     * @param minPlayers The new minimum players per team.
     * @param maxPlayers The new maximum players per team.
     * @param teamsPerGame The new number of teams per game.
     * @param officialsPerGame The new number of officials per game.
     * @return true if the tournament was successfully updated, false otherwise.
     */
    // TODO check users permissions
    // TODO check if valid tournament
    @RequestMapping(value = "/{id}/update", method = RequestMethod.POST)
    public @ResponseBody boolean updateTournament(
            @PathVariable int id,
            @RequestParam(value = "name") String name,
            @RequestParam(value = "minPlayersPerTeam") int minPlayers,
            @RequestParam(value = "maxPlayersPerTeam") int maxPlayers,
            @RequestParam(value = "teamsPerGame") int teamsPerGame,
            @RequestParam(value = "officialsPerGame") int officialsPerGame) {
        TournamentDao tournamentDao = new TournamentDao();
        Tournament tournament = tournamentDao.getTournamentById(id, false, false);

        tournament.setName(name);
        tournament.setMinPlayers(minPlayers);
        tournament.setMaxPlayers(maxPlayers);
        tournament.setTeamsPerGame(teamsPerGame);
        tournament.setOfficialsPerGame(officialsPerGame);

        tournamentDao.saveTournament(tournament);
        return true;
    }

    /**
     * Sets the name of the tournament given by id to the given name.
     * 
     * POST request data: name
     * 
     * @param id The id of the tournament.
     * @param name The name you wish to set the tournament to.
     * @return true if the name was successfully set, false otherwise.
     */
    // TODO check users permissions
    // TODO check if valid tournament
    @RequestMapping(value = "/{id}/name", method = RequestMethod.POST)
    public @ResponseBody boolean setTournamentName(@PathVariable int id, @RequestParam(value = "name") String name) {
        TournamentDao tournamentDao = new TournamentDao();
        Tournament tournament = tournamentDao.getTournamentById(id, false, false);
        if (tournament == null) {
            return false;
        }

        tournament.setName(name);
        tournamentDao.saveTournament(tournament);
        return true;
    }

    /**
     * Sets the minimum number of players per team for the tournament given by
     * id to the given number. This can't be set if the bracket is already
     * formed for the tournament.
     * 
     * POST request data: minPlayers
     * 
     * @param id The id of the tournament.
     * @param minPlayers The minimum number of players you wish to set for the
     *            tournament.
     * @return true if the minimum number of players was set, false otherwise
     */
    // TODO check users permissions
    // TODO check if valid tournament
    @RequestMapping(value = "/{id}/minPlayersPerTeam", method = RequestMethod.POST)
    public @ResponseBody boolean setMinPlayersPerTeam(
            @PathVariable int id,
            @RequestParam(value = "minPlayers") int minPlayers) {
        TournamentDao tournamentDao = new TournamentDao();
        Tournament tournament = tournamentDao.getTournamentById(id, false, false);

        tournament.setMinPlayers(minPlayers);
        tournamentDao.saveTournament(tournament);
        return true;
    }

    /**
     * Sets the maximum number of players per team for the tournament given by
     * id to the given number. This can't be set if the bracket is already
     * formed for the tournament.
     * 
     * POST request data: maxPlayers
     * 
     * @param id The id of the tournament.
     * @param maxPlayers The maximum number of players you wish to set for the
     *            tournament.
     * @return true if the maximum number of players was set, false otherwise
     */
    // TODO check users permissions
    // TODO check if valid tournament
    @RequestMapping(value = "/{id}/maxPlayersPerTeam", method = RequestMethod.POST)
    public @ResponseBody boolean setMaxPlayersPerTeam(
            @PathVariable int id,
            @RequestParam(value = "maxPlayers") int maxPlayers) {
        TournamentDao tournamentDao = new TournamentDao();
        Tournament tournament = tournamentDao.getTournamentById(id, false, false);

        tournament.setMaxPlayers(maxPlayers);
        tournamentDao.saveTournament(tournament);
        return true;
    }

    /**
     * Sets whether the tournament given by id is a double elimination
     * tournament. This can't be set if the bracket is already formed for the
     * tournament.
     * 
     * POST request data: doubleElimination
     * 
     * @param id The id of the tournament.
     * @param doubleElimination Whether the tournament is a double elimination
     *            tournament.
     * @return true if the tournament was set by doubleElimination, false
     *         otherwise
     */
    // TODO check users permissions
    // TODO check if valid tournament
    @RequestMapping(value = "/{id}/doubleElimination", method = RequestMethod.POST)
    public @ResponseBody boolean setDoubleElimination(
            @PathVariable int id,
            @RequestParam(value = "doubleElimination") boolean doubleElimination) {
        TournamentDao tournamentDao = new TournamentDao();
        Tournament tournament = tournamentDao.getTournamentById(id, false, false);

        tournament.setDoubleElimination(doubleElimination);
        tournamentDao.saveTournament(tournament);
        return true;
    }

    /**
     * Adds the given team to the given tournament. Only adds the team if the
     * tournament bracket isn't formed yet.
     * 
     * POST request data: teamId
     * 
     * @param id The id of the tournament to add the team to.
     * @param teamId The id of the team to add
     * @return true if the team was added to the tournament, false otherwise
     */
    // TODO check users permissions
    // TODO check if valid tournament
    @RequestMapping(value = "/{id}/addTeam", method = RequestMethod.POST)
    public @ResponseBody boolean addTeamToTournament(
            @PathVariable int id,
            @RequestParam(value = "teamName") String teamName,
            @RequestParam(value = "acceptFreeAgents") boolean acceptFreeAgents,
            HttpSession session) {
        TournamentDao tournamentDao = new TournamentDao();
        Tournament tournament = tournamentDao.getTournamentById(id, false, true);

        Member teamLeader = (Member) session.getAttribute("member");

        Team team = new Team();
        team.setName(teamName);
        team.setAcceptFreeAgents(acceptFreeAgents);
        team.setTournament(tournament);
        team.setTeamLeader(teamLeader);

        TeamDao teamDao = new TeamDao();
        teamDao.saveTeam(team);

        tournament.addTeam(team);
        tournamentDao.saveTournament(tournament);
        return true;
    }

    /**
     * Removes the given team from the given tournament. Only removes the team
     * if the tournament bracket isn't formed yet.
     * 
     * POST request data: teamId
     * 
     * @param id The id of the tournament to remove the team from.
     * @param teamId The id of the team to remove
     * @return true if the team was removed from the tournament, false otherwise
     */
    // TODO check users permissions
    // TODO check if valid tournament
    @RequestMapping(value = "/{id}/removeTeam", method = RequestMethod.POST)
    public @ResponseBody boolean removeTeamFromTournament(
            @PathVariable int id,
            @RequestParam(value = "teamId") int teamId) {
        TournamentDao tournamentDao = new TournamentDao();
        Tournament tournament = tournamentDao.getTournamentById(id, false, true);

        TeamDao teamDao = new TeamDao();
        Team team = teamDao.getTeamById(teamId, false, false, false);

        tournament.removeTeam(team);
        teamDao.deleteTeamById(teamId);
        return true;
    }

    // TODO check users permissions
    // TODO check if valid tournament
    @RequestMapping(value = "/{id}/form", method = RequestMethod.POST)
    public @ResponseBody void formTournamentById(@PathVariable int id) {
        TournamentDao tournamentDao = new TournamentDao();
        Tournament tournament = tournamentDao.getTournamentById(id, true, true);
        tournament.formBracket(new GameDao());
        tournamentDao.saveTournament(tournament);
    }
}

package tests;

import java.util.ArrayList;

import org.junit.Test;

import antlr.collections.List;
import edu.iastate.dao.GameDao;
import edu.iastate.dao.PlayerDao;
import edu.iastate.models.*;
import edu.iastate.dao.TeamDao;
import edu.iastate.dao.TournamentDao;

public class TeamTests {
    @Test
    public void saveTeamTest() {
        Team team = new Team();
        GameDao gamedao = new GameDao();
        PlayerDao playerdao = new PlayerDao();
        TournamentDao tournamentdao = new TournamentDao();
        team.setAcceptFreeAgents(false);
        team.setGames(gamedao.getAllGames());
        team.setName("TestAddGame");
        ArrayList<Player> players = new ArrayList<Player>();
        players.add(playerdao.getPlayerById(1));
        players.add(playerdao.getPlayerById(2));
        team.setPlayers(players);
        team.setTournament(tournamentdao.getTournamentById(1, false, false));
        team.setTeamLeader(playerdao.getPlayerById(2));
        TeamDao teamdao = new TeamDao();
        teamdao.saveTeam(team);

        System.out.println(teamdao.getTeamById(11, false, true).getTeamLeader().getName());
        /*System.out.println("Names:");
        for (Player player : players) {
            System.out.println(player.getName());
        }*/
    }

    @Test
    public void addPlayerToTeamTest() {
        TeamDao teamdao = new TeamDao();
        Team team = teamdao.getTeamById(10, true, true);
        PlayerDao playerdao = new PlayerDao();
        Player player = playerdao.getPlayerById(5);
        team.addPlayer(player);
        for (Player p: team.getPlayers()) {
            System.out.println(p.getName());
        }

        teamdao.saveTeam(team);
    }

    @Test
    public void removePlayerFromTeamTest() {
        TeamDao teamdao = new TeamDao();
        Team team = teamdao.getTeamById(10, true, true);
        PlayerDao playerdao = new PlayerDao();
        Player player = playerdao.getPlayerById(5);
        team.removePlayer(player);
        for (Player p: team.getPlayers()) {
            System.out.println(p.getId());
        }

        teamdao.saveTeam(team);
    }
}
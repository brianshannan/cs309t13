package tests;

import java.util.ArrayList;

import org.junit.Test;






import antlr.collections.List;
import edu.iastate.dao.*;
import edu.iastate.models.Player;
import edu.iastate.models.Team;

public class TeamTests {
//    @Test
//    public void saveTeamTest() {
//        Team team = new Team();
//        GameDao gamedao = new GameDao();
//        PlayerDao playerdao = new PlayerDao();
//        TournamentDao tournamentdao = new TournamentDao();
//        team.setAcceptFreeAgents(false);
//        team.setGames(gamedao.getAllGames());
//        team.setName("TestInvitedPlayer2");
//        ArrayList<Player> players = new ArrayList<Player>();
//        players.add(playerdao.getPlayerById(1, true));
//        players.add(playerdao.getPlayerById(2, true));
//        team.setTournament(tournamentdao.getTournamentById(1, false, false));
//        team.setPlayers(players);
//        team.addPlayer(playerdao.getPlayerById(3, true));
//        team.addInvitedPlayer(playerdao.getPlayerById(4, false));
//        team.setTeamLeader(playerdao.getPlayerById(3, false));
//        TeamDao teamdao = new TeamDao();
//        teamdao.saveTeam(team);
//
//        System.out.println(teamdao.getTeamById(11, false, true, false).getTeamLeader().getName());
//        /*System.out.println("Names:");
//        for (Player player : players) {
//            System.out.println(player.getName());
//        }*/
//    }

    @Test
    public void addPlayerToTeamTest() {
        TeamDao teamdao = new TeamDao();
        Team team = teamdao.getTeamById(13, true, true, true);
        PlayerDao playerdao = new PlayerDao();
        Player player = playerdao.getPlayerById(4, true);
        team.addPlayer(player);
//        for (Player p: team.getPlayers()) {
//            System.out.println(p.getName());
//        }

        teamdao.saveTeam(team);
    }
//
//    @Test
//    public void removePlayerFromTeamTest() {
//        TeamDao teamdao = new TeamDao();
//        Team team = teamdao.getTeamById(10, true, true);
//        PlayerDao playerdao = new PlayerDao();
//        Player player = playerdao.getPlayerById(5);
//        team.removePlayer(player);
//        for (Player p: team.getPlayers()) {
//            System.out.println(p.getId());
//        }
//
//        teamdao.saveTeam(team);
//    }
}

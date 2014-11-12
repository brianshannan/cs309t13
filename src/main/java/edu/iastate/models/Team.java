package edu.iastate.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Team")
public class Team {

    @Id
    @GeneratedValue
    @Column(name = "team_id")
    private int id;

    @Column(name = "team_name")
    private String name;

    @Column(name = "accepts_free_agents")
    private boolean acceptFreeAgents;

    @JoinTable(name = "teamplayermapper", joinColumns={@JoinColumn(name = "team_id", referencedColumnName = "team_id")}, 
            inverseJoinColumns={ @JoinColumn(name = "member_id", referencedColumnName = "member_id")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Player> players;
    
    @JoinTable(name = "teaminvitedplayermapper", joinColumns={@JoinColumn(name = "team_id", referencedColumnName = "team_id")}, 
            inverseJoinColumns={ @JoinColumn(name = "member_id", referencedColumnName = "member_id")})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Player> invitedPlayers;

    @ManyToMany(mappedBy = "teams")
    private List<Game> games;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Player teamLeader;

    @ManyToOne
    @JoinColumn(name = "tournament_id")
    private Tournament tournament;

    @Column(name = "team_skill")
    private int teamSkillLevel; 

    @OneToMany(mappedBy = "team")
    private Set<Score> scores;

    public Team() {
        players = new ArrayList<Player>();
        games = new ArrayList<Game>();
        invitedPlayers = new ArrayList<Player>();
    }

    public Team(int id, String name, boolean acceptFreeAgents, List<Player> players, 
            List<Game> games, Player teamLeader){
        this.id = id;
        this.name = name;
        this.acceptFreeAgents = acceptFreeAgents;
        this.players = players;
        this.games = games;
        this.teamLeader = teamLeader;
    }

    public Player getTeamLeader() {
        return teamLeader;
    }

    public void setTeamLeader(Player teamLeader) {
        this.teamLeader = teamLeader;
    }

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

    public boolean isAcceptFreeAgents() {
        return acceptFreeAgents;
    }

    public void setAcceptFreeAgents(boolean acceptFreeAgents) {
        this.acceptFreeAgents = acceptFreeAgents;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        calculateSkillLevel();
    }
    
    public List<Player> getInvitedPlayers() {
        return invitedPlayers;
    }

    public void setInvitedPlayers(List<Player> invitedPlayers) {
        this.invitedPlayers = invitedPlayers;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public int getTeamSkillLevel() {
        return teamSkillLevel;
    }

    /**
     * Calculates the skill level of the team based on
     * skill level of players of team
     */
    private void calculateSkillLevel() {
        int skillLevel = 0;
        for(Player player : players){
            Survey s = player.getSurveyByTournament(tournament);
            if(s==null) {
                skillLevel+= 0;
                continue;
            }
            skillLevel+=s.getSurveyScore();
        }
        teamSkillLevel = skillLevel/players.size();
    }

    /**
     * Adds player to this team. Does nothing if player is null or
     * player already exists in current team
     * 
     * @param player
     * The player to be added
     * @return
     * -1 if null or player already exists
     * 0 if maximum has reached
     * 1 if successful
     */
    public int addPlayer(Player player) {
        
        if(player == null || this.players.contains(player)) {
            
            return -1;
        }
        if(this.players.size() == tournament.getMaxPlayers()) {
            return 0;
        }
        this.players.add(player);
        removeInvitedPlayer(player);
        calculateSkillLevel(); //Updates the skill level
        return 1;
    }

    /**
     * Removes player from team. Does nothing if player is null
     * or player does not exist in team
     * 
     * @param player
     * The player to be removed 
     */
    public void removePlayer(Player player) {
        if(player == null || !this.players.contains(player)) {
            return;
        }
        player.removeSurvey(player.getSurveyByTournament(this.tournament));
        this.players.remove(player);
        calculateSkillLevel(); //Updates the skill level
    }
    
    /**
     * Adds player to this team's invited player list. Does nothing if player is null or
     * player already exists in current invited list
     * 
     * @param player
     * The player to be added
     * @return
     * -1 if null or player already exists
     * 0 if player is already in team
     * 1 if successful
     * 
     */
    public int addInvitedPlayer(Player player) {
        if(player == null || this.invitedPlayers.contains(player)) {
            return -1;
        }
        if(this.players.contains(player)) {
            return 0;
        }
        
        this.invitedPlayers.add(player);
        return 1;
    }

    /**
     * Removes player from team. Does nothing if player is null
     * or player does not exist in team
     * 
     * @param player
     * The player to be removed 
     */
    public void removeInvitedPlayer(Player player) {
        if(player == null || !this.invitedPlayers.contains(player)) {
            return;
        }
        this.invitedPlayers.remove(player);
    }
    
    /**
     * Returns true if this team has minimum number of required players 
     * 
     * @return
     * true of condition is met
     */
    public boolean hasMinPlayers() {
        return this.players.size()>=tournament.getMinPlayers();
    }

    public void setTeamSkillLevel(int teamSkillLevel) {
        this.teamSkillLevel = teamSkillLevel;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Team other = (Team) obj;
        if (id != other.id)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }
}

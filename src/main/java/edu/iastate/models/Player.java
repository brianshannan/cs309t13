package edu.iastate.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author nawaf
 *
 */

@Entity
@Table(name = "Player")
public class Player extends Member {
	
    @Column(name = "free_agent")
    private Boolean freeAgent;
    
    /**
     * A list of surveys completed by player for each game
     */
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "player")
//    private List<Survey> surveys;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "player")
//	private List<Badge> badges;
	   
//    public List<Survey> getSurveys() {
//		return surveys;
//	}
//
//	public void setSurveys(List<Survey> surveys) {
//		this.surveys = surveys;
//	}
    
//	public List<Badge> getBadges() {
//	     return badges;
//	}
//
//	public void setBadges(List<Badge> badges) {
//	     this.badges = badges;
//	}
	    
	public void setFreeAgent(Boolean freeAgent) {
		this.freeAgent = freeAgent;
	}
    
    public Boolean getFreeAgent() {
    	return freeAgent;
    }
    
    

}


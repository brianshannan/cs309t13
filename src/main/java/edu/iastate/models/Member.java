package edu.iastate.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonManagedReference;

/**
 * 
 * @author nawaf
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Member")
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private int member_id;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "sex")
    private String sex;

    @Column(name = "height")
    private Integer height;

    @Column(name = "weight")
    private Integer weight;

    public enum UserType {
        PLAYER, OFFICIAL, COORDINATOR, ADMIN
    };

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "user_type")
    private UserType userType;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "player")
    private List<Survey> surveys;

    public Member() {}

    protected Member(UserType userType) {
        this.userType = userType;
    }

    public Member(String name, String username, String password,
            UserType userType) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public List<Survey> getSurveys() {
        return surveys;
    }

    public void setSurveys(List<Survey> surveys) {
        this.surveys = surveys;
    }

    /**
     * Returns the survey pertaining to a particular tournament
     * 
     * @param tournament The tournament whose survey we are interested in
     * @return Survey object pertaining to that tournament
     */
    public Survey getSurveyByTournament(Tournament tournament) {
        for(Survey s : surveys) {
            if(s.getTournament().equals(tournament)) {
                return s;
            }
        }
        return null;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public int getId() {
        return member_id;
    }

    public void setId(int id) {
        this.member_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + member_id;
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
        Member other = (Member) obj;
        if(member_id != other.member_id)
            return false;
        return true;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}

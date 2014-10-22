package edu.iastate.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Admin")
public class Admin extends Member {

    public Admin() {
        super();
    }

    public Admin(String name, String username, String password) {
        super(name, username, password, UserType.ADMIN);
    }

    @Column(name = "current_view")
    private UserType currentView;

    public UserType getCurrentView() {
        return currentView;
    }

    public void setCurrentView(UserType view) {
        this.currentView = view;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        return true;
    }

}

package edu.iastate.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import edu.iastate.models.Availability;
import edu.iastate.utils.EntityManagerFactorySingleton;

public class AvailabilityDao {

    private final EntityManagerFactory entityManagerFactory;
    
    public AvailabilityDao() {
        this.entityManagerFactory = EntityManagerFactorySingleton.getFactory();
    }
    
    public AvailabilityDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    /**
     * Saves the given availability to the database
     * 
     * @param member The member to save to the database
     */
    public void saveAvailability(Availability availability) {
        
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.persist(availability);

        entityManager.merge(availability);
        
        transaction.commit();
        entityManager.close();
    }

}

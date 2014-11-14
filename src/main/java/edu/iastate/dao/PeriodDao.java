package edu.iastate.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import edu.iastate.models.Period;
import edu.iastate.utils.EntityManagerFactorySingleton;

public class PeriodDao {

    private final EntityManagerFactory entityManagerFactory;
    
    public PeriodDao() {
        this.entityManagerFactory = EntityManagerFactorySingleton.getFactory();
    }
    
    public PeriodDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }
    
    /**
     * Saves the given availability to the database
     * 
     * @param member The member to save to the database
     */
    public void savePeriod(Period period) {
        
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        entityManager.merge(period);
        
        transaction.commit();
        entityManager.close();
    }
    
    public void savePeriods(List<Period> periods) {
        
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        for (Period period : periods)
            entityManager.merge(period);
        
        transaction.commit();
        entityManager.close();
    }

}
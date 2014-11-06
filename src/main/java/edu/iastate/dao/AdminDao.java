package edu.iastate.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import edu.iastate.models.Admin;

public class AdminDao extends MemberDao {

    public AdminDao() {
        super();
    }

    public AdminDao(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public List<Admin> getAllAdmins() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        TypedQuery<Admin> query = entityManager.createQuery("from Admin", Admin.class);
        List<Admin> admins = query.getResultList();

        transaction.commit();
        entityManager.close();

        return admins;
    }

    /**
     * Gets an admin matching the given id
     * 
     * @param id The id of the admin you wish to fetch
     * @return admin by id
     */
    public Admin getAdminById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        TypedQuery<Admin> query = entityManager.createQuery("from Admin a where a.member_id = :id", Admin.class);
        query.setParameter("id", id);
        Admin admin = query.getSingleResult();

        transaction.commit();
        entityManager.close();
        return admin;
    }

//    public <T extends Member> void promote(T member) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        EntityTransaction transaction = entityManager.getTransaction();
//        transaction.begin();
//        TypedQuery<Player> query = entityManager.createQuery("INSERT INTO Player (:id) SELECT member_id from Member", Player.class);
//        TypedQuery<Member> query = entityManager.createQuery("UPDATE Member SET member_name=:name WHERE member_id=:id", Member.class);
//        query.setParameter("id", member.getId());
//
//        transaction.commit();
//        entityManager.close();
//    }
}

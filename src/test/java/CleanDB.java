/*
 * Copyright (c) Justin Aarden. info@justinaarden.nl.
 */

import domain.Role;
import domain.Tweet;
import domain.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.EntityType;
import java.sql.SQLException;

/**
 * Created by Justin on 21-3-2016.
 */
public class CleanDB {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("kwettertest_pu");

    //Clean DataBase

    private static final Class<?>[] ENTITY_TYPES = {
            Tweet.class,
            Role.class,
            User.class
    };

    public void Clean()
            throws SQLException
    {
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate();


        for (Class<?> entityType : ENTITY_TYPES) {
            deleteEntities(entityType, entityManager);
        }
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate();

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @SuppressWarnings("JPQLValidation")
    private static void deleteEntities(Class<?> entityType, EntityManager entityManager) {
        entityManager.createQuery("delete from " + getEntityName(entityType, entityManager)).executeUpdate();
    }

    protected static String getEntityName(Class<?> clazz, EntityManager entityManager) {
        EntityType et = entityManager.getMetamodel().entity(clazz);
        return et.getName();
    }

    CleanDB() throws SQLException
    {
        Clean();
    }
}

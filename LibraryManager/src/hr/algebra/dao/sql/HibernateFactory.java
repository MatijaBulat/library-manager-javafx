/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Matija
 */
public class HibernateFactory {

    private static final String PERSISTANCE_UNIT = "LibraryManagerPU";
    private static final EntityManagerFactory EMF
            = Persistence.createEntityManagerFactory(PERSISTANCE_UNIT);

    public static final String SELECT_BOOKS = "Books.findAll";
    public static final String SELECT_AUTHORS = "Author.findAll";
    public static final String SELECT_PUBLISHERS = "Publisher.findAll";

    public static EntityManagerWrapper getEntityManager() {
        return new EntityManagerWrapper(EMF.createEntityManager());
    }

    public static void release() {
        EMF.close();
    }

    private HibernateFactory() {
    }
}

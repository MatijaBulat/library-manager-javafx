/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.Repository;
import hr.algebra.models.Book;
import hr.algebra.models.Publisher;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Matija
 */
public class HibernatePublisherRepository implements Repository<Publisher> {

    @Override
    public int add(Publisher data) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();

            Publisher publisher = new Publisher(data);
            em.persist(publisher);
            em.getTransaction().commit();

            return publisher.getId();
        }
    }

    @Override
    public void update(Publisher data) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.find(Publisher.class, data.getId()).updateDetails(data);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Publisher data) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.remove(em.contains(data) ? data : em.merge(data));
            em.getTransaction().commit();
        }
    }

    @Override
    public Publisher get(int id) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            return em.find(Publisher.class, id);
        }
    }

    @Override
    public List<Publisher> getAll() throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            return em.createNamedQuery(HibernateFactory.SELECT_PUBLISHERS).getResultList();
        }
    }
}

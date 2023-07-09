/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.Repository;
import hr.algebra.models.Author;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Matija
 */
public class HibernateAuthorRepository implements Repository<Author> {

    @Override
    public int add(Author data) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();

            Author author = new Author(data);
            em.persist(author);
            em.getTransaction().commit();

            return author.getId();
        }
    }

    @Override
    public void update(Author data) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.find(Author.class, data.getId()).updateDetails(data);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Author data) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.remove(em.contains(data) ? data : em.merge(data));
            em.getTransaction().commit();
        }
    }

    @Override
    public Author get(int id) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            return em.find(Author.class, id);
        }
    }

    @Override
    public List<Author> getAll() throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            return em.createNamedQuery(HibernateFactory.SELECT_AUTHORS).getResultList();
        }
    }
}

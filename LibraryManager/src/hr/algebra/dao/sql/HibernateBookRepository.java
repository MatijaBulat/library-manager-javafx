/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.Repository;
import hr.algebra.models.Book;
import java.util.List;
import javax.persistence.EntityManager;

public class HibernateBookRepository implements Repository<Book> {

    @Override
    public int add(Book data) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();

            Book book = new Book(data);
            em.persist(book);
            em.getTransaction().commit();

            return book.getId();
        }
    }

    @Override
    public void update(Book data) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.find(Book.class, data.getId()).updateDetails(data);
            em.getTransaction().commit();
        }
    }

    @Override
    public void delete(Book data) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.remove(em.contains(data) ? data : em.merge(data));
            em.getTransaction().commit();
        }
    }

    @Override
    public Book get(int id) throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            return em.find(Book.class, id);
        }
    }

    @Override
    public List<Book> getAll() throws Exception {
        try (EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()) {
            EntityManager em = wrapper.get();
            return em.createNamedQuery(HibernateFactory.SELECT_BOOKS).getResultList();
        }
    }

    @Override
    public void release() {
        HibernateFactory.release();
    }
}

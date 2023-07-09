/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.dao.sql.HibernateAuthorRepository;
import hr.algebra.dao.sql.HibernateBookRepository;
import hr.algebra.dao.sql.HibernatePublisherRepository;
import hr.algebra.models.Author;
import hr.algebra.models.Book;
import hr.algebra.models.Publisher;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Matija
 */
public class RepositoryFactory {
    
    private RepositoryFactory() {
    }
    
    private static final Map<String, Repository> map = new HashMap<>();

    static {
        map.put(Book.class.getSimpleName(), new HibernateBookRepository());
        map.put(Author.class.getSimpleName(), new HibernateAuthorRepository());
        map.put(Publisher.class.getSimpleName(), new HibernatePublisherRepository());
    }

    public static Repository getRepository(Class clazz) {
        return map.get(clazz.getSimpleName());
    }
}

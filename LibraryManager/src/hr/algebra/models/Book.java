/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.models;

import hr.algebra.dao.sql.HibernateFactory;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Matija
 */
@Entity
@Table(name = "Book")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = HibernateFactory.SELECT_BOOKS, query = "SELECT b FROM Book b")
    , @NamedQuery(name = "Book.findById", query = "SELECT b FROM Book b WHERE b.id = :id")
    , @NamedQuery(name = "Book.findByTitle", query = "SELECT b FROM Book b WHERE b.title = :title")
    , @NamedQuery(name = "Book.findByYearPublished", query = "SELECT b FROM Book b WHERE b.yearPublished = :yearPublished")
    , @NamedQuery(name = "Book.findByIsbn", query = "SELECT b FROM Book b WHERE b.isbn = :isbn")})
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Title")
    private String title;
    @Column(name = "YearPublished")
    private Integer yearPublished;
    @Column(name = "ISBN")
    private String isbn;
    @Lob
    @Column(name = "Picture")
    private byte[] picture;
    @JoinColumn(name = "AuthorId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Author authorId;
    @JoinColumn(name = "PublisherId", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Publisher publisherId;

    public Book() {
    }

    public Book(Integer id) {
        this.id = id;
    }

    public Book(Integer id, String title, Integer yearPublished, String isbn, Author authorId, Publisher publisherId) {
        this.id = id;
        this.title = title;
        this.yearPublished = yearPublished;
        this.isbn = isbn;
        this.authorId = authorId;
        this.publisherId = publisherId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYearPublished() {
        return yearPublished;
    }

    public void setYearPublished(Integer yearPublished) {
        this.yearPublished = yearPublished;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Author getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Author authorId) {
        this.authorId = authorId;
    }

    public Publisher getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Publisher publisherId) {
        this.publisherId = publisherId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.models.Book[ id=" + id + " ]";
    }

    public Book(Book data) {
        updateDetails(data);
    }

    public void updateDetails(Book data) {
        title = data.title;
        yearPublished = data.yearPublished;
        isbn = data.isbn;
        picture = data.picture;
        authorId = data.authorId;
        publisherId = data.publisherId;
    }
}

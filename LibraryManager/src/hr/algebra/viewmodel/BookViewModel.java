/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.models.Author;
import hr.algebra.models.Book;
import hr.algebra.models.Publisher;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Matija
 */
public class BookViewModel {

    private final Book book;

    public BookViewModel(Book book) {
        if (book == null) {
            book = new Book(0, "", 0, "", new Author(), new Publisher());
        }
        this.book = book;
    }

    public Book getBook() {
        return book;
    }

    public StringProperty getTitleProperty() {
        return new SimpleStringProperty(book.getTitle());
    }

    public IntegerProperty getYearPublishedProperty() {
        return new SimpleIntegerProperty(book.getYearPublished());
    }

    public StringProperty getISBNProperty() {
        return new SimpleStringProperty(book.getIsbn());
    }

    public IntegerProperty getIdProperty() {
        return new SimpleIntegerProperty(book.getId());
    }

    public ObjectProperty<byte[]> getPictureProperty() {
        return new SimpleObjectProperty(book.getPicture());
    }

    public StringProperty getAuthorStringProperty() {
        return new SimpleStringProperty(book.getAuthorId().getName());
    }

    public StringProperty getPublisherStringProperty() {
        return new SimpleStringProperty(book.getPublisherId().getName());
    }

    public AuthorViewModel getAuthorViewModel() {
        return new AuthorViewModel(book.getAuthorId());
    }

    public PublisherViewModel getPublisherViewModel() {
        return new PublisherViewModel(book.getPublisherId());
    }
}

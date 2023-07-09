/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.models.Author;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Matija
 */
public class AuthorViewModel {

    private final Author author;

    public AuthorViewModel(Author author) {
        if (author == null) {
            author = new Author(0, "");
        }
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(author.getName());
    }

    public IntegerProperty getIdProperty() {
        return new SimpleIntegerProperty(author.getId());
    }

    @Override
    public String toString() {
        return author.getName();
    }
}

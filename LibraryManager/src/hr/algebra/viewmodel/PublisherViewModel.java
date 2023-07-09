/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.models.Publisher;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Matija
 */
public class PublisherViewModel {

    private final Publisher publisher;

    public PublisherViewModel(Publisher publisher) {
        if (publisher == null) {
            publisher = new Publisher(0, "");
        }
        this.publisher = publisher;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(publisher.getName());
    }

    public IntegerProperty getIdProperty() {
        return new SimpleIntegerProperty(publisher.getId());
    }

    @Override
    public String toString() {
        return publisher.getName();
    }
}

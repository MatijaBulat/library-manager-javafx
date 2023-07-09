/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.models.Author;
import hr.algebra.models.Book;
import hr.algebra.models.Publisher;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.ImageUtils;
import hr.algebra.utilities.ValidationUtils;
import hr.algebra.viewmodel.AuthorViewModel;
import hr.algebra.viewmodel.BookViewModel;
import hr.algebra.viewmodel.PublisherViewModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

/**
 * FXML Controller class
 *
 * @author Matija
 */
public class BookController implements Initializable {

    private Map<TextField, Label> bookValidationMap;
    private Map<TextField, Label> authorValidationMap;
    private Map<TextField, Label> publisherValidationMap;

    private final ObservableList<BookViewModel> bookList = FXCollections.observableArrayList();
    private final ObservableList<AuthorViewModel> authorList = FXCollections.observableArrayList();
    private final ObservableList<PublisherViewModel> publisherList = FXCollections.observableArrayList();

    private BookViewModel selectedBookViewModel;
    private AuthorViewModel selectedAuthorViewModel;
    private PublisherViewModel selectedPublisherViewModel;

    private Tab previusTab;

    @FXML
    private TabPane tpContent;
    @FXML
    private Tab tabListBooks;
    @FXML
    private TableView<BookViewModel> tvBooks;
    @FXML
    private TableColumn<BookViewModel, String> tcTitle, tcISBN, tcAuthor, tcPublisher;
    @FXML
    private TableColumn<BookViewModel, Integer> tcYearPublished;

    @FXML
    private Tab tabEditBook;
    @FXML
    private ImageView ivPerson;
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfYearPublished;
    @FXML
    private TextField tfISBN;
    @FXML
    private ComboBox<PublisherViewModel> cbPublishers;
    @FXML
    private ComboBox<AuthorViewModel> cbAuthors;
    @FXML
    private Label lbTitle;
    @FXML
    private Label lbYearPublished;
    @FXML
    private Label lbISBN;
    @FXML
    private Tab tabListAuthors;
    @FXML
    private TableView<AuthorViewModel> tvAuthors;
    @FXML
    private TableColumn<AuthorViewModel, String> tcAuthorName;
    @FXML
    private Tab tabEditAuthor;
    @FXML
    private TextField tfAuthorName;
    @FXML
    private Tab tabListPublishers;
    @FXML
    private TableView<PublisherViewModel> tvPublishers;
    @FXML
    private TableColumn<PublisherViewModel, String> tcPublisherName;
    @FXML
    private Tab tabEditPublisher;
    @FXML
    private TextField tfPublisherName;
    @FXML
    private Label lbAuthorName;
    @FXML
    private Label lbPublisherName;
    @FXML
    private Label lbIcon;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initValidation();
        initBooks();
        initAuthors();
        initPublishers();
        initBooksTable();
        initAuthorsTable();
        initpublishersTable();
        addIntegerMask(tfYearPublished);
        setListeners();
        initAuthorsComboBox();
        initPublisherComboBox();
    }

    @FXML
    private void deleteBook(ActionEvent event) {
        if (tvBooks.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Book");
            alert.setHeaderText("Are you sure you want to delete this book?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                bookList.remove(tvBooks.getSelectionModel().getSelectedItem());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Book selected");
            alert.setContentText("Please select a book to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void editBook(ActionEvent event) {
        if (tvBooks.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Book selected");
            alert.setContentText("Please select a book to edit.");
            alert.showAndWait();
        } else {
            bindBook(tvBooks.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEditBook);
            previusTab = tabEditBook;
        }
    }

    @FXML
    private void upload(ActionEvent event) {
        File file = FileUtils.uploadFileDialog(tfYearPublished.getScene().getWindow(), "jpg", "jpeg", "png");
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            try {
                selectedBookViewModel.getBook().setPicture(ImageUtils.imageToByteArray(image, ext));
                ivPerson.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void commitBook(ActionEvent event) {
        if (formValid()) {
            selectedBookViewModel.getBook().setTitle(tfTitle.getText().trim());
            selectedBookViewModel.getBook().setYearPublished(Integer.valueOf(tfYearPublished.getText().trim()));
            selectedBookViewModel.getBook().setIsbn(tfISBN.getText().trim());
            selectedBookViewModel.getBook().setAuthorId(cbAuthors.getSelectionModel().getSelectedItem().getAuthor());
            selectedBookViewModel.getBook().setPublisherId(cbPublishers.getSelectionModel().getSelectedItem().getPublisher());

            if (selectedBookViewModel.getBook().getId() == 0) {
                bookList.add(selectedBookViewModel);
            } else {
                try {
                    RepositoryFactory.getRepository(Book.class).update(selectedBookViewModel.getBook());
                    tvBooks.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedBookViewModel = null;
            tpContent.getSelectionModel().select(tabListBooks);
            resetForm();
        }
    }

    @FXML
    private void deleteAuthor(ActionEvent event) {
        if (tvAuthors.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Author");
            alert.setHeaderText("Are you sure you want to delete this author?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                authorList.remove(tvAuthors.getSelectionModel().getSelectedItem());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Auhtor selected");
            alert.setContentText("Please select a author to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void editAuthor(ActionEvent event) {
        if (tvAuthors.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Auhtor selected");
            alert.setContentText("Please select a author to edit.");
            alert.showAndWait();
        } else {
            bindAuthor(tvAuthors.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEditAuthor);
        }
    }

    @FXML
    private void commitAuthor(ActionEvent event) {
        if (formValid()) {
            selectedAuthorViewModel.getAuthor().setName(tfAuthorName.getText().trim());

            if (selectedAuthorViewModel.getAuthor().getId() == 0) {
                authorList.add(selectedAuthorViewModel);
            } else {
                try {
                    RepositoryFactory.getRepository(Author.class).update(selectedAuthorViewModel.getAuthor());
                    tvAuthors.refresh();
                    tvBooks.refresh();
                    tvPublishers.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedAuthorViewModel = null;
            tpContent.getSelectionModel().select(tabListAuthors);
            resetForm();
        }
    }

    @FXML
    private void deletePublisher(ActionEvent event) {
        if (tvPublishers.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Publisher");
            alert.setHeaderText("Are you sure you want to delete this publisher?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                publisherList.remove(tvPublishers.getSelectionModel().getSelectedItem());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Publisher selected");
            alert.setContentText("Please select a publisher to delete.");
            alert.showAndWait();
        }
    }

    @FXML
    private void editPublisher(ActionEvent event) {
        if (tvPublishers.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Publisher selected");
            alert.setContentText("Please select a publisher to edit.");
            alert.showAndWait();
        } else {
            bindPublisher(tvPublishers.getSelectionModel().getSelectedItem());
            tpContent.getSelectionModel().select(tabEditPublisher);
        }
    }

    @FXML
    private void commitPublisher(ActionEvent event) {
        if (formValid()) {
            selectedPublisherViewModel.getPublisher().setName(tfPublisherName.getText().trim());

            if (selectedPublisherViewModel.getPublisher().getId() == 0) {
                publisherList.add(selectedPublisherViewModel);
            } else {
                try {
                    RepositoryFactory.getRepository(Publisher.class).update(selectedPublisherViewModel.getPublisher());
                    tvPublishers.refresh();
                    tvAuthors.refresh();
                    tvBooks.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectedPublisherViewModel = null;
            tpContent.getSelectionModel().select(tabListPublishers);
            resetForm();
        }
    }

    private void initValidation() {
        bookValidationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfTitle, lbTitle),
                new AbstractMap.SimpleImmutableEntry<>(tfYearPublished, lbYearPublished),
                new AbstractMap.SimpleImmutableEntry<>(tfISBN, lbISBN)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        authorValidationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfAuthorName, lbAuthorName)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        publisherValidationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfPublisherName, lbPublisherName)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initBooks() {
        try {
            RepositoryFactory.getRepository(Book.class).getAll().forEach(b -> bookList.add(new BookViewModel((Book) b)));
        } catch (Exception ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initAuthors() {
        try {
            RepositoryFactory.getRepository(Author.class).getAll().forEach(a -> authorList.add(new AuthorViewModel((Author) a)));
        } catch (Exception ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initPublishers() {
        try {
            RepositoryFactory.getRepository(Publisher.class).getAll().forEach(p -> publisherList.add(new PublisherViewModel((Publisher) p)));
        } catch (Exception ex) {
            Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initBooksTable() {
        tcTitle.setCellValueFactory(book -> book.getValue().getTitleProperty());
        tcYearPublished.setCellValueFactory(book -> book.getValue().getYearPublishedProperty().asObject());
        tcISBN.setCellValueFactory(book -> book.getValue().getISBNProperty());
        tcAuthor.setCellValueFactory(book -> book.getValue().getAuthorStringProperty());
        tcPublisher.setCellValueFactory(book -> book.getValue().getPublisherStringProperty());

        tvBooks.setItems(bookList);
    }

    private void initAuthorsTable() {
        tcAuthorName.setCellValueFactory(a -> a.getValue().getNameProperty());
        tvAuthors.setItems(authorList);
    }

    private void initpublishersTable() {
        tcPublisherName.setCellValueFactory(p -> p.getValue().getNameProperty());
        tvPublishers.setItems(publisherList);
    }

    private void addIntegerMask(TextField tf) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String input = change.getText();

            if (input.matches("\\d*")) {
                return change;
            }

            return null;
        };

        tf.setTextFormatter(
                new TextFormatter<>(
                        new IntegerStringConverter(), 0, filter
                )
        );
    }

    private void setListeners() {
        tpContent.setOnMouseClicked(event -> {
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabEditBook) && !tabEditBook.equals(previusTab)) {
                bindBook(null);
                previusTab = tabEditBook;
            }
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabEditAuthor) && !tabEditAuthor.equals(previusTab)) {
                bindAuthor(null);
                previusTab = tabEditAuthor;
            }
            if (tpContent.getSelectionModel().getSelectedItem().equals(tabEditPublisher) && !tabEditPublisher.equals(previusTab)) {
                bindPublisher(null);
                previusTab = tabEditPublisher;
            }
        });

        bookList.addListener(new ListChangeListener<BookViewModel>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends BookViewModel> change) {
                if (change.next()) {
                    if (change.wasRemoved()) {
                        change.getRemoved().forEach(bvm -> {
                            try {
                                RepositoryFactory.getRepository(Book.class).delete(bvm.getBook());
                            } catch (Exception ex) {
                                Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    } else if (change.wasAdded()) {
                        change.getAddedSubList().forEach(bvm -> {
                            try {
                                int idBook = RepositoryFactory.getRepository(Book.class).add(bvm.getBook());
                                bvm.getBook().setId(idBook);
                            } catch (Exception ex) {
                                Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }
            }
        });

        authorList.addListener(new ListChangeListener<AuthorViewModel>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends AuthorViewModel> change) {
                if (change.next()) {
                    if (change.wasRemoved()) {
                        change.getRemoved().forEach(avm -> {
                            try {
                                RepositoryFactory.getRepository(Author.class).delete(avm.getAuthor());
                            } catch (Exception ex) {
                                Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    } else if (change.wasAdded()) {
                        change.getAddedSubList().forEach(avm -> {
                            try {
                                int idAuthor = RepositoryFactory.getRepository(Author.class).add(avm.getAuthor());
                                avm.getAuthor().setId(idAuthor);
                            } catch (Exception ex) {
                                Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }
            }
        });

        publisherList.addListener(new ListChangeListener<PublisherViewModel>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends PublisherViewModel> change) {
                if (change.next()) {
                    if (change.wasRemoved()) {
                        change.getRemoved().forEach(pvm -> {
                            try {
                                RepositoryFactory.getRepository(Publisher.class).delete(pvm.getPublisher());
                            } catch (Exception ex) {
                                Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    } else if (change.wasAdded()) {
                        change.getAddedSubList().forEach(pvm -> {
                            try {
                                int idPublisher = RepositoryFactory.getRepository(Publisher.class).add(pvm.getPublisher());
                                pvm.getPublisher().setId(idPublisher);
                            } catch (Exception ex) {
                                Logger.getLogger(BookController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    }
                }
            }
        });
    }

    private void initAuthorsComboBox() {
        cbAuthors.setItems(authorList);

        cbAuthors.setConverter(new StringConverter<AuthorViewModel>() {
            @Override
            public String toString(AuthorViewModel wvm) {
                return wvm.toString();
            }

            @Override
            public AuthorViewModel fromString(String name) {
                return cbAuthors.getItems().stream().filter(w -> w.getNameProperty().toString().equals(name)).findFirst().orElse(null);
            }
        });
    }

    private void initPublisherComboBox() {
        cbPublishers.setItems(publisherList);

        cbPublishers.setConverter(new StringConverter<PublisherViewModel>() {
            @Override
            public String toString(PublisherViewModel wvm) {
                return wvm.toString();
            }

            @Override
            public PublisherViewModel fromString(String name) {
                return cbPublishers.getItems().stream().filter(w -> w.getNameProperty().toString().equals(name)).findFirst().orElse(null);
            }
        });
    }

    private void bindBook(BookViewModel bookViewModel) {
        resetForm();

        selectedBookViewModel = bookViewModel != null ? bookViewModel : new BookViewModel(null);
        tfTitle.setText(selectedBookViewModel.getTitleProperty().get());
        tfYearPublished.setText(String.valueOf(selectedBookViewModel.getYearPublishedProperty().get()));
        tfISBN.setText(selectedBookViewModel.getISBNProperty().get());

        cbAuthors.setValue(selectedBookViewModel.getAuthorViewModel());
        cbPublishers.setValue(selectedBookViewModel.getPublisherViewModel());

        ivPerson.setImage(
                selectedBookViewModel.getPictureProperty().get() != null
                ? new Image(new ByteArrayInputStream(selectedBookViewModel.getPictureProperty().get()))
                : new Image(new File("src/assets/no_image.png").toURI().toString())
        );
    }

    private void bindAuthor(AuthorViewModel authorViewModel) {
        resetForm();

        selectedAuthorViewModel = authorViewModel != null ? authorViewModel : new AuthorViewModel(null);
        tfAuthorName.setText(selectedAuthorViewModel.getNameProperty().get());
    }

    private void bindPublisher(PublisherViewModel publisherViewModel) {
        resetForm();

        selectedPublisherViewModel = publisherViewModel != null ? publisherViewModel : new PublisherViewModel(null);
        tfPublisherName.setText(selectedPublisherViewModel.getNameProperty().get());
    }

    private void resetForm() {
        bookValidationMap.values().forEach(l -> l.setVisible(false));
        authorValidationMap.values().forEach(l -> l.setVisible(false));
        publisherValidationMap.values().forEach(l -> l.setVisible(false));
        lbIcon.setVisible(false);
    }

    private boolean formValid() {

        resetForm();
        final AtomicBoolean ok = new AtomicBoolean(true);

        if (tpContent.getSelectionModel().getSelectedItem().equals(tabEditBook)) {
            bookValidationMap.keySet().forEach(tf -> {
                if (tf.getText().trim().isEmpty()) {
                    ok.set(false);
                    bookValidationMap.get(tf).setVisible(true);
                } else {
                    bookValidationMap.get(tf).setVisible(false);
                }
            });
            if (selectedBookViewModel.getPictureProperty().get() == null) {
                lbIcon.setVisible(true);
                ok.set(false);
            } else {
                lbIcon.setVisible(false);
            }
        } else if (tpContent.getSelectionModel().getSelectedItem().equals(tabEditAuthor)) {
            authorValidationMap.keySet().forEach(tf -> {
                if (tf.getText().trim().isEmpty()) {
                    ok.set(false);
                    authorValidationMap.get(tf).setVisible(true);
                } else {
                    authorValidationMap.get(tf).setVisible(false);
                }
            });
        } else {
            publisherValidationMap.keySet().forEach(tf -> {
                if (tf.getText().trim().isEmpty()) {
                    ok.set(false);
                    publisherValidationMap.get(tf).setVisible(true);
                } else {
                    publisherValidationMap.get(tf).setVisible(false);
                }
            });
        }

        return ok.get();
    }
}

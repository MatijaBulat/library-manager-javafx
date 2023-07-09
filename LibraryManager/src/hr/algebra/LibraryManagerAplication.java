/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.models.Author;
import hr.algebra.models.Book;
import hr.algebra.models.Publisher;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Matija
 */
public class LibraryManagerAplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("view/Book.fxml"));

        Scene scene = new Scene(root, 600, 400);

        primaryStage.setTitle("Library manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop(); //To change body of generated methods, choose Tools | Templates.
        RepositoryFactory.getRepository(Book.class).release();
        RepositoryFactory.getRepository(Author.class).release();
        RepositoryFactory.getRepository(Publisher.class).release();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}

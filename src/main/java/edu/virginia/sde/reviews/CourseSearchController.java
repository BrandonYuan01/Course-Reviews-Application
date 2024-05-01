package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseSearchController {
    private Stage stage;
    private DatabaseDriver databaseDriver;
    @FXML
    private TextField subject;
    @FXML
    private TextField courseNumber;
    @FXML
    private TextField title;
    @FXML
    private Button search;
    @FXML
    private MenuItem logOut;
    @FXML
    private MenuItem myReviews;
    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    private void initialize(){
        databaseDriver = DatabaseSingleton.getInstance();
    }

    @FXML
    public void logoff() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(stage);
        stage.setTitle("Course Review");
        stage.setScene(scene);
    }

    @FXML
    public void MyReviews() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("myreview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        myReviewController myReviewController = fxmlLoader.getController();
        myReviewController.setStage(stage);
        stage.setTitle("Course Review");
        stage.setScene(scene);
    }
}

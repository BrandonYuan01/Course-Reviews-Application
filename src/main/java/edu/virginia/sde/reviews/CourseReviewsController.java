package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseReviewsController {
    private DatabaseDriver databaseDriver;
    private Stage stage;
    private Course course;
    public void setStage(Stage stage, Course course) {
        this.stage = stage;
        this.course = course;
    }

    @FXML
    private void initialize() {
        databaseDriver = DatabaseSingleton.getInstance();
    }

    @FXML
    public void back() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("coursesearch.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        CourseSearchController courseSearchController = fxmlLoader.getController();
        courseSearchController.setStage(stage);
        stage.setScene(scene);
    }
}

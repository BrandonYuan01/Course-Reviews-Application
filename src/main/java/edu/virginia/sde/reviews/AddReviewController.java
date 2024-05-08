package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

// todo list
// function for back button
// update header with class info
// function for submit button

public class AddReviewController {
    private Stage stage;
    private String username;
    private Course course;
    private DatabaseDriver databaseDriver;
    @FXML
    private Label courseLabel;

    public void setStage(Stage stage, Course course, String username) {
        this.stage = stage;
        this.username = username;
        this.course = course;

        String courseInfo = String.format("%s %d: %s", course.getSubject(), course.getCourseNumber(), course.getTitle());
        courseLabel.setText(courseInfo);
    }

    @FXML
    private void initialize() {
        databaseDriver = DatabaseSingleton.getInstance();
    }

    @FXML
    public void back() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("coursereviews.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        CourseReviewsController courseReviewsController = fxmlLoader.getController();
        courseReviewsController.setStage(stage, course, username);
        stage.setScene(scene);
    }
}

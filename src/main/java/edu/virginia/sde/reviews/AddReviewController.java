package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AddReviewController {
    private Stage stage;
    private String username;
    private Course course;
    private DatabaseDriver databaseDriver;
    @FXML
    private Label courseLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField ratingField;
    @FXML
    private TextArea commentArea;

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

    @FXML
    public void submit() throws SQLException {
        String rating = ratingField.getText();
        if (rating.isEmpty()){
            errorLabel.setText("Rating cannot be empty.");
            return;
        }
        else if (!rating.matches("[1-5]")){
            errorLabel.setText("Rating must be an integer from 1 to 5.");
            return;
        }

        String comment = commentArea.getText();
        int intRating = Integer.parseInt(rating);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Review review = new Review(intRating, timestamp, comment, username, course);
        try {
            databaseDriver.addReview(review);
            databaseDriver.commit();
        } catch (SQLException e){
            errorLabel.setText("Failed to add review to database.");
            System.out.println(e.getMessage());
            databaseDriver.rollback();
            return;
        }

        try {
            course.addReview(review);
            back();
        } catch (IOException e){
            errorLabel.setText("Failed to go back to Course Review scene.");
        }

    }
}

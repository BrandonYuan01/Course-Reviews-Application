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
import java.util.ArrayList;

public class EditReviewController {
    private DatabaseDriver databaseDriver;
    private Stage stage;
    private String username;
    private Course course;
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

        try {
            int rating = databaseDriver.getReviewRatingByUserAndCourse(username, course);
            String comment = databaseDriver.getReviewCommentByUserAndCourse(username, course);
            databaseDriver.commit();
            ratingField.setText("" + rating);
            commentArea.setText(comment);
        } catch (SQLException e) {
            errorLabel.setText("Error fetching reviews.");
        }
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
        int reviewId = databaseDriver.getReviewIdByUserAndCourse(username, course);
        databaseDriver.commit();

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
            databaseDriver.updateReview(reviewId, intRating, comment, timestamp);
            databaseDriver.commit();
        } catch (SQLException e){
            errorLabel.setText("Failed to edit review in database.");
            System.out.println(e.getMessage());
            databaseDriver.rollback();
            return;
        }

        try {
            ArrayList<Review> newReviews = new ArrayList<>();
            for (Review r : course.getReviews()){
                if (r.getUsername().equals(username)){
                    newReviews.add(review);
                }
                else{
                    newReviews.add(r);
                }
            }
            course.setReviews(newReviews);
            back();
        } catch (IOException e){
            errorLabel.setText("Failed to go back to Course Review scene.");
        }

    }
}

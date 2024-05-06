package edu.virginia.sde.reviews;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

public class MainTest extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("coursereviews.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        int courseNumber = 3140;
        String subject = "CS";
        String title = "Software Development Essentials";
        List<Review> reviews = new ArrayList<>();
        Course sde = new Course(courseNumber, subject, title, reviews);


        Review r1 = new Review(1, 5, new Timestamp(System.currentTimeMillis()), "Good class", "student", sde);
        reviews.add(r1);
        sde.setReviews(reviews);


        CourseReviewsController courseReviewsController = fxmlLoader.getController();
        courseReviewsController.setStage(stage, sde);
        stage.setTitle("Course Review");
        stage.setScene(scene);
        stage.show();
    }
}
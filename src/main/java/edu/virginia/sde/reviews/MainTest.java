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


        ArrayList<Review> reviews = new ArrayList<>();
        Course c1 = new Course(3140, "CS", "Software Development Essentials", reviews);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Review r1 = new Review(5, timestamp, "Great class", "1", c1);
        Review r2 = new Review(3, timestamp, "Too hard", "2", c1);
        Review r3 = new Review(4, timestamp, "Professor is nice", "3", c1);
        reviews.add(r1);
        reviews.add(r2);
        reviews.add(r3);

        c1.setReviews(reviews);

        CourseReviewsController courseReviewsController = fxmlLoader.getController();
        courseReviewsController.setStage(stage, c1, "student");
        stage.setTitle("Course Review");
        stage.setScene(scene);
        stage.show();
    }
}
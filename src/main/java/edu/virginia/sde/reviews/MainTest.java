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

        String username = "student";

        int courseNumber = 3140;
        String subject = "CS";
        String title = "Software Development Essentials";
        List<Review> reviews = new ArrayList<>();
        Course sde = new Course(courseNumber, subject, title, reviews);

        String comment = "I really enjoyed this class. We learned a lot about software development, and so much of the course" +
                " content applies to the real world. This class has prepared me for my career as a software developer. The professor" +
                " was fun and engaging, yet his lectures were informative and insightful. I would highly recommend this class with this professor" +
                " to any prospective computer science students.";
        Review r1 = new Review(1, 5, new Timestamp(System.currentTimeMillis()), comment, username, sde);
        sde.addReview(r1);

        Review r2 = new Review(2, 3, new Timestamp(System.currentTimeMillis()), "This class was hard", "hi", sde);
        sde.addReview(r2);


        CourseReviewsController courseReviewsController = fxmlLoader.getController();
        courseReviewsController.setStage(stage, sde, "joe");
        stage.setTitle("Course Review");
        stage.setScene(scene);
        stage.show();
    }
}
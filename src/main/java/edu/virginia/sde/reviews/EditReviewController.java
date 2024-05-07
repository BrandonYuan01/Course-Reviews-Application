package edu.virginia.sde.reviews;

import javafx.stage.Stage;

public class EditReviewController {
    private Stage stage;
    private String username;
    private Review review;

    public void setStage(Stage stage, String username, Review review) {
        this.stage = stage;
        this.username = username;
        this.review = review;
    }
}

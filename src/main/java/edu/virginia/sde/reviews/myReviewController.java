package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class myReviewController {
    private String username;
    private Stage stage;
    private DatabaseDriver databaseDriver;

    public void setStage(Stage stage, String username){

        this.stage = stage;
        this.username = username;
    }
    @FXML
    private void initialize(){
        databaseDriver = DatabaseSingleton.getInstance();
    }
}

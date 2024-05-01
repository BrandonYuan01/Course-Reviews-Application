package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class myReviewController {
    private Stage stage;
    private DatabaseDriver databaseDriver;

    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    private void initialize(){
        databaseDriver = DatabaseSingleton.getInstance();
    }
}

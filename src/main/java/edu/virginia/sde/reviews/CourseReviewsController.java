package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CourseReviewsController {
    private DatabaseDriver databaseDriver;
    private Stage stage;
    private Course course;
    @FXML
    private Label courseLabel;
    @FXML
    private TableView<Review> reviewTable;
    @FXML
    private TableColumn<Review, String> timeColumn;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    @FXML
    private TableColumn<Review, String> commentColumn;
    public void setStage(Stage stage, Course course) {
        this.stage = stage;
        this.course = course;
        String courseInfo = String.format("%s %d: %s", course.getSubject(), course.getCourseNumber(), course.getTitle());
        courseLabel.setText(courseInfo);

        populateReviewTable();
    }

    private void populateReviewTable(){
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        List<Review> reviews = course.getReviews();
        reviewTable.getItems().addAll(reviews);
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

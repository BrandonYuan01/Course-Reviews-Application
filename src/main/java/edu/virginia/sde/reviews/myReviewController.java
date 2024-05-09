package edu.virginia.sde.reviews;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class myReviewController {
    private Stage stage;
    private String username;
    private DatabaseDriver databaseDriver;


    @FXML
    private TableView<Review> reviewTableView;

    @FXML
    private TableColumn<Review, String> courseMnemonicColumn;

    @FXML
    private TableColumn<Review, Integer> courseNumberColumn;

    @FXML
    private TableColumn<Review, Integer> ratingColumn;

    @FXML
    private TableColumn<Review, String> commentColumn;
    @FXML
    private ListView courseList;

    public void setStage(Stage stage, String username) {
        this.stage = stage;
        this.username = username;
        try {
            loadReviews();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    @FXML
    private void initialize() {
        databaseDriver = DatabaseSingleton.getInstance();

        // Set up columns
        courseMnemonicColumn.setCellValueFactory(data -> {
            Course course = data.getValue().getCourse();
            return new SimpleStringProperty(course.getSubject());
        });

        courseNumberColumn.setCellValueFactory(data -> {
            return new SimpleIntegerProperty(data.getValue().getCourse().getCourseNumber()).asObject();
        });

        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
    }


    private void loadReviews() throws SQLException {
        List<Review> reviews = databaseDriver.getReviewsByUsername(username);
        ObservableList<Review> reviewObservableList = FXCollections.observableArrayList(reviews);
        reviewTableView.setItems(reviewObservableList);
    }

    @FXML
    public void back() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("coursesearch.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        CourseSearchController courseSearchController = fxmlLoader.getController();
        courseSearchController.setStage(stage, username);
        stage.setTitle("Course Search");
        stage.setScene(scene);
    }

    @FXML
    private void handleReviewClicked(MouseEvent event) {
        Review selectedReview = reviewTableView.getSelectionModel().getSelectedItem();
        if (selectedReview != null) {
            Course selectedCourse = selectedReview.getCourse();
            if (selectedCourse != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("coursereviews.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    CourseReviewsController controller = loader.getController();
                    controller.setStage(stage, selectedCourse, username);

                    stage.setScene(scene);
                    stage.setTitle("Course Reviews");
                } catch (IOException e) {
                }
            }
        }
    }
}

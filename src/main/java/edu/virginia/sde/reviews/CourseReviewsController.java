package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CourseReviewsController {
    private DatabaseDriver databaseDriver;
    private Stage stage;
    private Course course;
    private String username;
    @FXML
    private Label courseLabel;
    @FXML
    private Label myReviewLabel;
    @FXML
    private TableView<Review> reviewTable;
    @FXML
    private TableColumn<Review, String> timeColumn;
    @FXML
    private TableColumn<Review, Integer> ratingColumn;
    @FXML
    private TableColumn<Review, String> commentColumn;
    public void setStage(Stage stage, Course course, String username) {
        this.stage = stage;
        this.course = course;
        this.username = username;

        String courseInfo = String.format("%s %d: %s", course.getSubject(), course.getCourseNumber(), course.getTitle());
        courseLabel.setText(courseInfo);

        populateReviewTable();
    }

    private void populateReviewTable(){
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));
        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));

        // I got the following code from URL: https://forums.oracle.com/ords/apexds/post/wrappable-text-in-a-tableview-5312
        // This code allows the text in the "comment" column to wrap rather than just show what it can, followed by ...
        commentColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Review, String> call(TableColumn<Review, String> param) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!isEmpty()) {
                            Text text = new Text(item);
                            text.setWrappingWidth(950);
                            setGraphic(text);
                        }
                    }
                };
            }

        });

        List<Review> reviews = course.getReviews();
        reviewTable.getItems().addAll(reviews);
    }

    private int getUserReviewId(){
        List<Review> reviews = course.getReviews();
        for (Review review : reviews){
            if (review.getUsername().equals(username)){
                return review.getId();
            }
        }
        return -1;
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

    @FXML
    public void addReview() throws IOException{
        if (getUserReviewId() != -1){
            myReviewLabel.setText("You have already written a review for this course.");
        }
        else{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addreview.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            AddReviewController addReviewController = fxmlLoader.getController();
            addReviewController.setStage(stage, username);
            stage.setScene(scene);
        }
    }

    @FXML
    public void deleteReview() throws SQLException{
        if (getUserReviewId() == -1){
            myReviewLabel.setText("You have not written a review for this course yet.");
        }
        else{
            //TODO: Change GUI when review is deleted as well
            try {
                int id = getUserReviewId();
                databaseDriver.deleteReview(id);
                databaseDriver.commit();
                myReviewLabel.setText("Review deleted successfully.");
            } catch (SQLException e){
                myReviewLabel.setText("Error occurred while trying to delete review.");
                databaseDriver.rollback();
                throw e;
            }
        }
    }
}

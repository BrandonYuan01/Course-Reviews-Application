package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CourseSearchController {
    private Stage stage;
    private DatabaseDriver databaseDriver;
    private String username;
    @FXML
    private TextField subject;
    @FXML
    private TextField courseNumber;
    @FXML
    private TextField title;
    @FXML
    private Button search;
    @FXML
    private Button courseAdd;
    @FXML
    private MenuItem logOut;
    @FXML
    private MenuItem myReviews;
    @FXML
    private ListView courseList;
    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    private void initialize() throws SQLException {
        databaseDriver = DatabaseSingleton.getInstance();
        CourseDisplay();
    }

    @FXML
    public void logoff() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(stage);
        stage.setTitle("Course Review");
        stage.setScene(scene);
    }

    @FXML
    public void MyReviews() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("myreview.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        myReviewController myReviewController = fxmlLoader.getController();
        myReviewController.setStage(stage);
        stage.setTitle("Course Review");
        stage.setScene(scene);
    }

    @FXML
    public void CourseDisplay() throws SQLException {
        try {
            List<Course> courses = databaseDriver.allCourses();
            for (var course : courses) {
                course.setReviews(databaseDriver.getReviewsOfCourse(course));
            }
            ObservableList<Course> observableList = FXCollections.observableArrayList(courses);
            courseList.getItems().setAll(observableList);
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    @FXML
    public void CourseReview() throws SQLException, IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("coursereviews.fxml"));
        Course course = (Course) courseList.getSelectionModel().getSelectedItem();
        courseList.getScene().setRoot(fxmlLoader.load());

        CourseReviewsController courseReviewsController = fxmlLoader.getController();
        courseReviewsController.setStage(stage, course, username);
    }

    @FXML
    public void addCourse() throws SQLException, IOException {
        List<Review> reviews = new ArrayList<>();
        Course course = new Course(Integer.parseInt(courseNumber.getText()), subject.getText(), title.getText(), reviews);
        databaseDriver.addCourse(course);
        databaseDriver.commit();
    }
}

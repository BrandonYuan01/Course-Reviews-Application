package edu.virginia.sde.reviews;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

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
        subject.setTextFormatter(formatter("[a-zA-Z]{0,4}"));
        title.setTextFormatter(formatter(".{0,50}"));
        courseNumber.setTextFormatter(formatter("[0-9]{0,4}"));
        CourseDisplay();
    }

    @FXML
    public void search() throws SQLException {
        List<Course> courseMatches = new ArrayList<>();
        if (!courseNumber.getText().equals("") && !subject.getText().equals("") && !title.getText().equals("")) {
            courseMatches = databaseDriver.findAll(Integer.parseInt(courseNumber.getText()), subject.getText().toUpperCase(), title.getText());
        }
        else if (courseNumber.getText().equals("") && !subject.getText().equals("") && !title.getText().equals("")) {
            courseMatches = databaseDriver.findSubjectTitle(subject.getText().toUpperCase(), title.getText());
        }
        else if (!courseNumber.getText().equals("") && subject.getText().equals("") && !title.getText().equals("")) {
            courseMatches = databaseDriver.findCourseNumberTitle(Integer.parseInt(courseNumber.getText()), title.getText());
        }
        else if (!courseNumber.getText().equals("") && !subject.getText().equals("") && title.getText().equals("")) {
            courseMatches = databaseDriver.findCourseNumberSubject(Integer.parseInt(courseNumber.getText()), subject.getText().toUpperCase());
        }
        else if (!courseNumber.getText().equals("") && subject.getText().equals("") && title.getText().equals("")) {
            courseMatches = databaseDriver.findCourseNumber(Integer.parseInt(courseNumber.getText()));
        }
        else if (!subject.getText().equals("") && courseNumber.getText().equals("") && title.getText().equals("")) {
            courseMatches = databaseDriver.findCourseSubject(subject.getText().toUpperCase());
        }
        else if (!title.getText().equals("") && subject.getText().equals("") && courseNumber.getText().equals("")) {
            courseMatches = databaseDriver.findCourseTitle(title.getText());
        } else {
            CourseDisplay();
            return;
        }
        for (var course : courseMatches) {
            course.setReviews(databaseDriver.getReviewsOfCourse(course));
        }
        ObservableList<Course> observableList = FXCollections.observableArrayList(courseMatches);
        courseList.getItems().setAll(observableList);
    }


    public TextFormatter<String> formatter (String pattern) {
        UnaryOperator<TextFormatter.Change> format = change -> {
            String newText = change.getControlNewText();
            if (newText.matches(pattern)) {
                return change;
            } else {
                return null;
            }
        };
        return new TextFormatter<String>(format);
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
        List<Course> courses = databaseDriver.allCourses();
        for (var course : courses) {
            course.setReviews(databaseDriver.getReviewsOfCourse(course));
        }
        ObservableList<Course> observableList = FXCollections.observableArrayList(courses);
        courseList.getItems().setAll(observableList);
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("addcourse.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        AddCourseController addCourseController = fxmlLoader.getController();
        addCourseController.setStage(stage);
        stage.setTitle("Course Review");
        stage.setScene(scene);

//        List<Review> reviews = new ArrayList<>();
//        Course course = new Course(Integer.parseInt(courseNumber.getText()), subject.getText(), title.getText(), reviews);
//        databaseDriver.addCourse(course);
//        databaseDriver.commit();
    }
}

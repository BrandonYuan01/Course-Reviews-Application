package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class AddCourseController {
    private Stage stage;
    String username;
    private DatabaseDriver databaseDriver;
    @FXML
    private TextField subject;
    @FXML
    private TextField courseNumber;
    @FXML
    private TextField title;
    @FXML
    private Label courseAdd;
    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    private void initialize() throws SQLException {
        databaseDriver = DatabaseSingleton.getInstance();
        subject.setTextFormatter(formatter("[a-zA-Z]{0,4}"));
        title.setTextFormatter(formatter(".{0,50}"));
        courseNumber.setTextFormatter(formatter("[0-9]{0,4}"));
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
    public void addCourse() throws SQLException, IOException {
        if (2 <= subject.getText().length() && subject.getText().length() <= 4 && courseNumber.getText().length() == 4 && title.getText().length() >= 1 && databaseDriver.checkCourse(Integer.parseInt(courseNumber.getText()), subject.getText(), title.getText())) {
            List<Review> reviews = new ArrayList<>();

            Course course = new Course(Integer.parseInt(courseNumber.getText()), subject.getText().toUpperCase(), title.getText(), reviews);
            databaseDriver.addCourse(course);
            databaseDriver.commit();
            courseAdd.setText("Sucessfully added course");
            courseAdd.setTextFill(Color.GREEN);
            courseNumber.clear();
            subject.clear();
            title.clear();
        }
        else if (!databaseDriver.checkCourse(Integer.parseInt(courseNumber.getText()), subject.getText(), title.getText())) {
            courseAdd.setText("course already exists");
            courseAdd.setTextFill(Color.RED);
        }
        else if ((2 > subject.getText().length() || subject.getText().length() > 4) && courseNumber.getText().length() != 4 && title.getText().length() < 1) {
            courseAdd.setText("subject must be at least 2 characters and no greater than 4, course number must be 4 digits, and title must be at least 1 character");
            courseAdd.setTextFill(Color.RED);
        }
        else if ((2 > subject.getText().length() || subject.getText().length() > 4) && courseNumber.getText().length() != 4) {
            courseAdd.setText("subject must be at least 2 characters and no greater than 4, and course number must be 4 digits");
            courseAdd.setTextFill(Color.RED);
        }
        else if ((2 > subject.getText().length() || subject.getText().length() > 4) && title.getText().length() < 1) {
            courseAdd.setText("subject must be at least 2 characters and no greater than 4, and title must be at least 1 character");
            courseAdd.setTextFill(Color.RED);
        }
        else if (courseNumber.getText().length() != 4 && title.getText().length() < 1) {
            courseAdd.setText("course number must be 4 digits, and title must be at least 1 character");
            courseAdd.setTextFill(Color.RED);
        }
        else if (courseNumber.getText().length() != 4) {
            courseAdd.setText("course number must be 4 digits");
            courseAdd.setTextFill(Color.RED);
        }
        else if (title.getText().length() < 1) {
            courseAdd.setText("title must be at least 1 character");
            courseAdd.setTextFill(Color.RED);
        }
        else if (2 > subject.getText().length() || subject.getText().length() > 4) {
            courseAdd.setText("subject must be at least 2 characters and no greater than 4");
            courseAdd.setTextFill(Color.RED);
        }
    }
    @FXML
    public void back() throws SQLException, IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("coursesearch.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        CourseSearchController courseSearchController = fxmlLoader.getController();
        courseSearchController.setStage(stage, username);
        stage.setScene(scene);
    }
}

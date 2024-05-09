package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private Label Login;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    private DatabaseDriver databaseDriver;
    private Stage stage;
    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void initialize(){
        databaseDriver = DatabaseSingleton.getInstance();
    }

    @FXML
    public void userLogin() throws SQLException, IOException {
        if (databaseDriver.checkUser(username.getText(), password.getText())) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("coursesearch.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            CourseSearchController courseSearchController = fxmlLoader.getController();
            courseSearchController.setStage(stage, username.getText());
            stage.setTitle("Course Search");
            stage.setScene(scene);
        }
        else if (username.getText().equals("") && password.getText().equals("")) {
            Login.setText("Username and password cannot be empty");
        }
        else if (username.getText().equals("")) {
            Login.setText("Username cannot be empty");
        }
        else if (password.getText().equals("")) {
            Login.setText("Password cannot be empty");
        }
        else {
            Login.setText("Invalid username or password");
        }
    }
    @FXML
    public void signUp() throws SQLException, IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("signup.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        SignUpController signUpController = fxmlLoader.getController();
        signUpController.setStage(stage);
        stage.setTitle("Sign Up");
        stage.setScene(scene);
    }
    @FXML
    public void exit() throws SQLException {
        try {
            databaseDriver.disconnect();
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
        System.exit(0);
    }
}


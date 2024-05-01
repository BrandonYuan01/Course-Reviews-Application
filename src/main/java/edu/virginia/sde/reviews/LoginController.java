package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Button button;
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
            courseSearchController.setStage(stage);
            stage.setScene(scene);
            stage.setTitle("test");

            Login.setText("Login successful");
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
    }
    @FXML
    public void signUp() throws SQLException {
        if (username.getText().equals("") && password.getText().equals("")) {
            Login.setText("Username and password cannot be empty");
        }
        else if (username.getText().equals("")) {
            Login.setText("Username cannot be empty");
        }
        else if (password.getText().equals("")) {
            Login.setText("Password cannot be empty");
        }
        else if (databaseDriver.getPassword(username.getText()) != null) {
            Login.setText("Username already taken");
        }
        else if (password.getText().length() < 8) {
            Login.setText("Your password must be at least 8 characters");
        }
        else if (!username.getText().equals("") && !password.getText().equals("")){
            Login.setText("Signup successful");
            databaseDriver.addUser(username.getText(), password.getText());
            databaseDriver.commit();
        }
    }
    @FXML
    public void exit() throws SQLException {
        System.exit(1);
    }
}


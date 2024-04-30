package edu.virginia.sde.reviews;

import com.sun.tools.javac.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
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
    public void userLogin() throws SQLException {
        if (databaseDriver.checkUser(username.getText(), password.getText())) {
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("test.fxml"));
//            Parent root = fxmlLoader.load();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//            stage.setTitle("test");
//
//            TestController testController = fxmlLoader.getController();
//            testController.setStage(stage);
            Login.setText("Login successful");
        } else {
            Login.setText("Invalid login information");
        }
    }
    @FXML
    public void signUp() throws SQLException {
        System.out.println(username.getText());
        System.out.println(databaseDriver.getPassword(username.getText()));
        if (username.getText().equals("") || password.getText().equals("")){
            Login.setText("Enter a valid username and password");
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


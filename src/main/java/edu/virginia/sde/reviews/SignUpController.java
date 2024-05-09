package edu.virginia.sde.reviews;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SignUpController {
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
    public void signUp() throws SQLException {
        try {
            databaseDriver.addUser(username.getText(), password.getText());
        } catch (SQLException e){
            databaseDriver.createTables();
        }
        if (username.getText().equals("") && password.getText().equals("")) {
            Login.setText("Username and password cannot be empty");
            Login.setTextFill(Color.RED);
        }
        else if (username.getText().equals("")) {
            Login.setText("Username cannot be empty");
            Login.setTextFill(Color.RED);
        }
        else if (password.getText().equals("")) {
            Login.setText("Password cannot be empty");
            Login.setTextFill(Color.RED);
        }
        else if (databaseDriver.getPassword(username.getText()) != null) {
            Login.setText("Username already taken");
            Login.setTextFill(Color.RED);
        }
        else if (password.getText().length() < 8) {
            Login.setText("Your password must be at least 8 characters");
            Login.setTextFill(Color.RED);
        }
        else if (!username.getText().equals("") && !password.getText().equals("")){
            Login.setText("Signup successful");
            Login.setTextFill(Color.GREEN);
            databaseDriver.addUser(username.getText(), password.getText());
            databaseDriver.commit();
            username.clear();
            password.clear();
        }
    }
    @FXML
    public void login() throws SQLException, IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(stage);
        stage.setTitle("Course Review");
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

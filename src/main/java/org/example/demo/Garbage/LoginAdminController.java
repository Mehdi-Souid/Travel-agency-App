package org.example.demo.Garbage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.demo.DataBaseConnection;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginAdminController implements Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label errorMessage;
    @FXML
    private ImageView lock;
    @FXML
    private ImageView person;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label SignIn ;

    public void cancelButtonAction(ActionEvent e) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void loginButtonAction(ActionEvent e) throws SQLException {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        if (isValidCredentials(enteredUsername, enteredPassword)) {
            LoginValid();
        } else {
            errorMessage.setText("Invalid username or password!");
        }
    }

    private void LoginValid() throws SQLException {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();
        DataBaseConnection dbConnection = new DataBaseConnection();
        Connection connection = dbConnection.getConnection();

        String query = "SELECT * FROM Admin WHERE id = ? AND password = ?";

        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, enteredUsername);
                preparedStatement.setString(2, enteredPassword);


                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        errorMessage.setText("Login successful");
                    } else {
                        errorMessage.setText("Invalid username or password");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMessage.setText("Error occurred while processing login");
        }
    }

    private boolean isValidCredentials(String username, String password) {
        return !username.isBlank() && !password.isBlank();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File lockFile = new File("images/lock-removebg-preview.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lock.setImage(lockImage);

        File personFile = new File("images/pic-removebg-preview.png");
        Image personImage = new Image(personFile.toURI().toString());
        person.setImage(personImage);
    }
    }

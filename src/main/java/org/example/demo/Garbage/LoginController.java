package org.example.demo.Garbage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.demo.DataBaseConnection;
import org.example.demo.UserInterface;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
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
    private Label SignIn;

    private UserInterface userInterfaceController;
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

    public void LoginValid() throws SQLException {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();
        DataBaseConnection dbConnection = new DataBaseConnection();
        Connection connection = dbConnection.getConnection();

        String query = "SELECT * FROM travelto WHERE id = ? AND password = ?";

        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, enteredUsername);
                preparedStatement.setString(2, enteredPassword);


                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        errorMessage.setText("Login successful");
                        String userId = resultSet.getString("id");
                        username.setText("");
                        password.setText("");
                        openInformationWindow(userId);
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

        SignIn.setOnMouseClicked(event -> openRegistrationWindow());
    }

    private void openRegistrationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("registre.fxml"));
            Parent root = loader.load();

            Stage registrationStage = new Stage();
            registrationStage.setScene(new Scene(root));
            registrationStage.setTitle("Registration");

            registrationStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void openInformationWindow(String id) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserInterface.fxml"));
                Parent root = loader.load();

                userInterfaceController = loader.getController();
                userInterfaceController.setUserId(id);

                Stage userInterfaceStage = new Stage();
                userInterfaceStage.setScene(new Scene(root));
                userInterfaceStage.setTitle("User Information");

                userInterfaceStage.show();
                userInterfaceController.loadUserInfo();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



}

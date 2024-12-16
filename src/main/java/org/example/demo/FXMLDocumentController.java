/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.example.demo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.demo.MainDashBoardController;
import org.example.demo.UserInterface;
import org.example.demo.DataBaseConnection;

import javax.swing.*;

public class FXMLDocumentController implements Initializable {

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
    private Button Confirm;
    @FXML
    private Button Cancel;

    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField Age;
    @FXML
    private TextField id;
    @FXML
    private PasswordField passwordRegistre;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label notification;
    @FXML
    private ImageView plane;
    @FXML
    private ImageView profileImage;
    @FXML
    private Button chooseImage;
    @FXML
    private File selectedFile=null;



    private UserInterface userInterfaceController;
    @FXML
    private AnchorPane si_loginForm;

    @FXML
    private AnchorPane side_form;
    
    @FXML
    private Button side_CreateBtn;
    
    @FXML
    private Button side_alreadyHave;

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
    @FXML
    private void handleUploadImage(ActionEvent event) throws SQLException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        File selection = fileChooser.showOpenDialog(null);

        if (selection != null) {
            Image image = new Image(selection.toURI().toString());
            profileImage.setImage(image);
            this.selectedFile=selection;
        }
    }
    public void LoginValid() throws SQLException {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();
        DataBaseConnection dbConnection = new DataBaseConnection();
        Connection connection = dbConnection.getConnection();

        String query = "SELECT * FROM users WHERE id = ? AND password = ?";

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
        File planeFile = new File("images/logo.png");
        Image planeImage = new Image(planeFile.toURI().toString());
        plane.setImage(planeImage);
    }
    public void openInformationWindow(String id) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashBoard.fxml"));
            Parent root = loader.load();

            MainDashBoardController main = loader.getController();
            main.setUserId(id);

            Stage userInterfaceStage = new Stage();
            userInterfaceStage.setScene(new Scene(root));
            userInterfaceStage.setTitle("User Information");

            userInterfaceStage.show();
            main.loadUserInfo();
            main.loadUserTrip();
            main.loadData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void register(ActionEvent event) {
        String name = nom.getText();
        String prename = prenom.getText();
        int Ages = Integer.parseInt(Age.getText());
        String ids = id.getText();
        String Mtps = passwordRegistre.getText();
        String confirmMtps = confirmPassword.getText();

        if(Mtps.length()<6 ){
            notification.setText("Password must be more 6 charcteres!");
            return;
        }
        if (!Mtps.equals(confirmMtps)) {
            notification.setText("Passwords do not match!");
            return;
        }
        if (name.isEmpty() || prename.isEmpty() || Ages==0 || ids.isEmpty() || Mtps.isEmpty() || confirmMtps.isEmpty()) {
            notification.setText("Please fill in all fields!");
            return;
        }
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();
            String query = "INSERT INTO users (id,password,nom,prenom,Age) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ids);
            statement.setString(2, Mtps);
            statement.setString(3, name);
            statement.setString(4, prename);
            statement.setInt(5, Ages);
            int rowsInserted = statement.executeUpdate();

            if (selectedFile != null) {
                byte[] imageData = Files.readAllBytes(selectedFile.toPath());
                String userId = ids;
                if (!userId.isEmpty()) {
                    query = "UPDATE users SET ProfileImage = ? WHERE id = ?";
                    statement = connection.prepareStatement(query);
                    statement.setString(2, ids);
                    statement.setBytes(1, imageData);
                    statement.executeUpdate();
                } else {
                    notification.setText("Error: User ID is empty.");
                    return;
                }
            }
                if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Registration Successful");
                nom.setText("");
                prenom.setText("");
                Age.setText("");
                id.setText("");
                passwordRegistre.setText("");
                confirmPassword.setText("");

            } else {
                notification.setText("Registration failed!");
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            notification.setText("Error: " + e.getMessage());
        }
    }
    /**switch to register with slider**/
    public void switchForm(ActionEvent event) {
        
        TranslateTransition slider = new TranslateTransition();
        
        if (event.getSource() == side_CreateBtn) {
            slider.setNode(side_form);
            slider.setToX(300);
            slider.setDuration(Duration.seconds(1));
            
            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(true);
                side_CreateBtn.setVisible(false);

                si_loginForm.setVisible(true);

            });
            
            slider.play();
        } else if (event.getSource() == side_alreadyHave) {
            slider.setNode(side_form);
            slider.setToX(0);
            slider.setDuration(Duration.seconds(1));
            
            slider.setOnFinished((ActionEvent e) -> {
                side_alreadyHave.setVisible(false);
                side_CreateBtn.setVisible(true);
                si_loginForm.setVisible(true);
            });
            
            slider.play();
        }
    }

}

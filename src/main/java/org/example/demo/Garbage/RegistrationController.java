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

import javax.swing.JOptionPane;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    @FXML
    private Button ConfirmButton;
    @FXML
    private Button CancelButton;
    @FXML
    private ImageView WhiteBack;
    @FXML
    private ImageView logo;
    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField Age;
    @FXML
    private TextField id;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label notification;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File lockFile = new File("images/WhiteBack.jpg");
        Image lockImage = new Image(lockFile.toURI().toString());
        WhiteBack.setImage(lockImage);

        File personFile = new File("images/flight-removebg-preview.png");
        Image personImage = new Image(personFile.toURI().toString());
        logo.setImage(personImage);
    }

    public void CancelButtonAction(ActionEvent e) {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

    public void register(ActionEvent event) {
        String name = nom.getText();
        String prename = prenom.getText();
        int Ages = Integer.parseInt(Age.getText());
        String ids = id.getText();
        String Mtps = password.getText();
        String confirmMtps = confirmPassword.getText();

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
            String query = "INSERT INTO travelto (id,password,nom,prenom,Age) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, ids);
            statement.setString(2, Mtps);
            statement.setString(3, name);
            statement.setString(4, prename);
            statement.setInt(5, Ages);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Registration Successful");
                Stage stage = (Stage) ConfirmButton.getScene().getWindow();
                stage.close();

            } else {
                notification.setText("Registration failed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            notification.setText("Error: " + e.getMessage());
        }
    }
}

package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class UserInterface implements Initializable {

    @FXML
    private ImageView face;
    @FXML
    private Label nom;
    @FXML
    private Label prenom;
    @FXML
    private Label age;
    @FXML
    private Label id;
    @FXML
    private ImageView plane;
    @FXML
    private ComboBox<String> destination;
    @FXML
    private ComboBox<String> classe;
    @FXML
    private ComboBox<String> depart;
    @FXML
    private Button cancel;
    @FXML
    private Button buy;
    @FXML
    private Label notification;
    @FXML
    private DatePicker date;



    public String userId;
    public  String ids;


    public void initialize(URL url, ResourceBundle resourceBundle) {
        File faceFile = new File("images/User.jpg");
        Image faceImage = new Image(faceFile.toURI().toString());
        face.setImage(faceImage);

        File planeFile = new File("images/airplane.png");
        Image planeImage = new Image(planeFile.toURI().toString());
        plane.setImage(planeImage);

        loadUserInfo();
        fillDestination();
        fillDepart();

        ObservableList<String> classesList = FXCollections.observableArrayList(
                "First Class",
                "Business Class",
                "Premium Economy Class",
                "Economy Class",
                "Basic Economy Class"
        );
        classe.setItems(classesList);

    }

    public void fillDestination(){
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();

            String query = "SELECT DISTINCT Destination FROM flights";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String destinations = resultSet.getString("Destination");
                destination.getItems().add(destinations);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void fillDepart(){
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();

            String query = "SELECT DISTINCT Depart FROM flights";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String departs = resultSet.getString("Depart");
                depart.getItems().add(departs);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadUserInfo() {
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();

            String query = "SELECT nom, prenom, age FROM travelto WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nom.setText(resultSet.getString("nom"));
                prenom.setText(resultSet.getString("prenom"));
                age.setText(resultSet.getString("age"));
                id.setText(userId);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cancelAction(ActionEvent e) {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void buyAction(ActionEvent e) {
        if (destination.getValue() == null || classe.getValue() == null ||
                depart.getValue() == null || date.getValue() == null) {
            notification.setText("Please fill in all fields!");
            return;
        }

        String destinations = destination.getValue();
        String classes = classe.getValue();
        String departs = depart.getValue();
        Date dates = Date.valueOf(date.getValue());


        if (!isValidChoice(destinations, classes, departs, dates)) {
            notification.setText("Selected options are not available!");
            return;
        }

        if (BuyTicket(destinations,classes,departs,dates,userId)) {
            notification.setText("Ticket purchased successfully!");
        } else {
            notification.setText("Error occurred while purchasing ticket.");
        }
    }

    private boolean isValidChoice(String destination, String classe, String depart, Date date) {
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();

            String query = "SELECT * FROM flights WHERE Destination = ? AND Depart = ? AND Date = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, destination);
            preparedStatement.setString(2, depart);
            preparedStatement.setDate(3, date);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean BuyTicket(String destination, String classe, String depart, Date date, String iduser) {
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();

            String query = "INSERT INTO ticket (idt, iduser, idflight, date, destination, Depart, classe) VALUES (?, ?, (SELECT Numero FROM flights WHERE destination = ? AND depart = ? AND date = ?), ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            String idflight = getFlightId(destination, depart, date);
            String idt = iduser + idflight;

            preparedStatement.setString(1, idt);
            preparedStatement.setString(2, iduser);
            preparedStatement.setString(3, destination);
            preparedStatement.setString(4, depart);
            preparedStatement.setDate(5, date);
            preparedStatement.setDate(6, date);
            preparedStatement.setString(7, destination);
            preparedStatement.setString(8, depart);
            preparedStatement.setString(9, classe);

            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Ticket purchased successfully!");
                return true;
            } else {
                System.out.println("Failed to purchase ticket.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getFlightId(String destination, String depart, Date date) {
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();

            String query = "SELECT Numero FROM flights WHERE destination = ? AND depart = ? AND date = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, destination);
            preparedStatement.setString(2, depart);
            preparedStatement.setDate(3, date);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("Numero");
            } else {
                return "";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }
}

package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class BuyTicketController implements Initializable {

    @FXML
    private ImageView homeImage;
    @FXML
    private ImageView chatImage;
    @FXML
    private ImageView ticketImage;
    @FXML
    private ImageView usersImage;
    @FXML
    private ImageView logoutImage;
    @FXML
    private ImageView planeImage;
    @FXML
    private ImageView avionImage;

    @FXML
    private TableView<Flight> table;


    @FXML
    private ComboBox<String> destination;
    @FXML
    private ComboBox<String> classe;
    @FXML
    private ComboBox<String> depart;
    @FXML
    private DatePicker date;




    @FXML
    private Button logoutButton;
    @FXML
    private Button btn_workbench;
    @FXML
    private Button btn_workbench1;
    @FXML
    private Button btn_workbench11 ;
    @FXML
    private Button btn_workbench111 ;
    @FXML
    private  Button buy;


    private Stage stage;
    private Scene scene;
    @FXML
    private Parent root;




    @FXML
    private TableView<Flight> flightsTable;
    @FXML
    private TableColumn<Flight, Date> dateCol;
    @FXML
    private TableColumn<Flight,String> idCol;
    @FXML
    private TableColumn<Flight,String> departCol;
    @FXML
    private TableColumn<Flight,String> destinationCol;
    @FXML
    private TableColumn<Flight, Double> priceCol;
    String query =null;
    Connection connection =null;
    ResultSet resultSet = null;
    String iduser;
    @FXML
    private Label idLabel;
    @FXML
    private ImageView profileImage;
    @FXML
    private VBox user;

    ObservableList<Flight>  FlightList= FXCollections.observableArrayList();


    public boolean AlreadyBuy(String userId) {
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();
            String query = "SELECT iduser FROM ticket WHERE iduser = ? ORDER BY Date DESC LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void switchToFlights(javafx.event.ActionEvent event) throws IOException {
        if ((AlreadyBuy(iduser))) {
            try {
                Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualFlight.fxml"));
                Parent root = loader.load();

                ActualFlightController main = loader.getController();
                main.setUserId(iduser);

                Stage FlightStage = new Stage();
                FlightStage.setScene(new Scene(root));
                FlightStage.setTitle("Ticket");


                currentStage.close();
                main.loadInformation(iduser);
                FlightStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("You didn't buy a ticket! Go to ticket tab and buy one .");
            alert.showAndWait();
        }
    }

    public void switchToHome(javafx.event.ActionEvent event) throws IOException {
        try {
            Stage currentStage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashBoard.fxml"));
            Parent root = loader.load();

            MainDashBoardController main = loader.getController();
            main.setUserId(iduser);

            Stage userInterfaceStage = new Stage();
            userInterfaceStage.setScene(new Scene(root));
            userInterfaceStage.setTitle("User Information");
            currentStage.close();

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

    public void switchToReview(javafx.event.ActionEvent event) throws IOException {
        try {
            Stage currentStage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Review.fxml"));
            Parent root = loader.load();

            ReviewController main = loader.getController();
            main.setUserId(iduser);

            Stage userInterfaceStage = new Stage();
            userInterfaceStage.setScene(new Scene(root));
            userInterfaceStage.setTitle("User Information");
            currentStage.close();

            userInterfaceStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLogOutClick() {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }

    public void loadImages(ImageView image,File file){
        Image  load= new Image(file.toURI().toString());
        image.setImage(load);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            loadData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        File homeFile = new File("images/home.png");
        loadImages(homeImage,homeFile);

        File usersFile = new File("images/profile.png");
        loadImages(usersImage,usersFile);

        File logoutFile = new File("images/logout.png");
        loadImages(logoutImage,logoutFile);

        File ticketsFile = new File("images/ticket-flight.png");
        loadImages(ticketImage,ticketsFile);

        File chatFile = new File("images/group.png");
        loadImages(chatImage,chatFile);

        File planeFile = new File("images/airplanes.png");
        loadImages(planeImage,planeFile);

        File avionFile = new File("images/airplane.png");
        loadImages(avionImage,avionFile);

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


    private void loadData() throws SQLException {
        DataBaseConnection dbConnection = new DataBaseConnection();
        connection = dbConnection.getConnection();
        refreshTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("Numero"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        departCol.setCellValueFactory(new PropertyValueFactory<>("Destination"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("Depart"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));

        destination.setOnAction(event -> {
            refreshTable();
        });

        depart.setOnAction(event -> {
            refreshTable();
        });

        date.setOnAction(event -> {
            refreshTable();
        });
    }

    private void refreshTable() {
        try {
            FlightList.clear();
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM flights WHERE 1=1");

            if (destination.getValue() != null) {
                queryBuilder.append(" AND Destination = '").append(destination.getValue()).append("'");
            }

            if (depart.getValue() != null) {
                queryBuilder.append(" AND Depart = '").append(depart.getValue()).append("'");
            }

            if (date.getValue() != null) {
                LocalDate selectedDate = date.getValue();
                Date sqlDate = Date.valueOf(selectedDate);
                queryBuilder.append(" AND Date = '").append(sqlDate).append("'");
            }

            PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FlightList.add(new Flight(
                        resultSet.getString("Numero"),
                        resultSet.getDate("Date"),
                        resultSet.getString("Destination"),
                        resultSet.getString("Depart"),
                        resultSet.getDouble("Price")));
            }

            flightsTable.setItems(FlightList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    private void buyTicketAction() {

        buyTicket(iduser);
    }
    private boolean hasUserBoughtTicket(String iduser,String idflight) {
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();

            String query = "SELECT COUNT(*) FROM ticket WHERE iduser = ? AND idflight = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, iduser);
            preparedStatement.setString(2, idflight);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int ticketCount = resultSet.getInt(1);
                return ticketCount > 0;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean buyTicket(String iduser) {
        try {
            if (classe.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please select a class.");
                alert.showAndWait();
                return false;
            }
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();

            Flight selectedFlight = flightsTable.getSelectionModel().getSelectedItem();

            if (selectedFlight != null) {
                String dest = selectedFlight.getDestination();
                String departure = selectedFlight.getDepart();
                java.util.Date dates = selectedFlight.getDate();
                double price=selectedFlight.getPrice();
                String classes = classe.getValue();
                String idflight = selectedFlight.getNumero();
                String idt = iduser + idflight;
                if (hasUserBoughtTicket(iduser,idflight)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("You have already purchased a ticket. You cannot buy another one.");
                    alert.showAndWait();
                    return false;
                }
                String query = "INSERT INTO ticket (idt, iduser, idflight, date, destination, Depart, classe,price) VALUES (?, ?, ?, ?, ?, ?, ?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                preparedStatement.setString(1, idt);
                preparedStatement.setString(2, iduser);
                preparedStatement.setString(3, idflight);
                preparedStatement.setDate(4, (Date) dates);
                preparedStatement.setString(5, dest);
                preparedStatement.setString(6, departure);
                preparedStatement.setString(7, classes);
                preparedStatement.setDouble(8, price);
                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Ticket purchased successfully!");
                    alert.showAndWait();
                    return true;
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to purchase ticket.");
                    alert.showAndWait();
                    return false;
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("No flight selected.");
                alert.showAndWait();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void setUserId(String userId) {
        this.iduser = userId;
        idLabel.setText(iduser);
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();

            String query = "SELECT  ProfileImage FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Blob profileImageBlob = resultSet.getBlob("ProfileImage");
                if (profileImageBlob != null) {
                    InputStream inputStream = profileImageBlob.getBinaryStream();
                    Image image = new Image(inputStream);
                    profileImage.setImage(image);
                    user.setVisible(true);
                } else {
                    user.setVisible(false);
                }
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
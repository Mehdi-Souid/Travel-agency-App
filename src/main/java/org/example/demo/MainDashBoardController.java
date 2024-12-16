package org.example.demo;

import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;



public class MainDashBoardController implements Initializable  {
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
    private Label nom;
    @FXML
    private Label prenom;
    @FXML
    private Label age;
    @FXML
    private Label id;
    @FXML
    private Label tripNumber;
    

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
    private BarChart<String, Number> BarChart;

    
    public String userId;
    private Stage stage;

    @FXML
    private TableView<Flight> flightsTable;
    @FXML
    private TableColumn<Flight, Date> dateCol;
    @FXML
    private TableColumn<Flight,String> departCol;
    @FXML
    private TableColumn<Flight,String> destinationCol;
    Connection connection =null;
    ResultSet resultSet = null;
    @FXML
    private VBox user;
    @FXML
     private ImageView profileImage;
    private Parent root;
    private Scene scene;

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
        if ((AlreadyBuy(userId))) {
            try {
                Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("ActualFlight.fxml"));
                Parent root = loader.load();

                ActualFlightController main = loader.getController();
                main.setUserId(userId);

                Stage FlightStage = new Stage();
                FlightStage.setScene(new Scene(root));
                FlightStage.setTitle("Ticket");


                currentStage.close();
                main.loadInformation(userId);
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

    public void loadData() throws SQLException {
        DataBaseConnection dbConnection = new DataBaseConnection();
         connection = dbConnection.getConnection();
        refreshTable();
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        departCol.setCellValueFactory(new PropertyValueFactory<>("Destination"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("Depart"));
    }

    public void refreshTable() {
        try {
            FlightList.clear();
            StringBuilder queryBuilder = new StringBuilder("SELECT Date, Destination, Depart FROM ticket WHERE iduser = ?");

            PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString());
            preparedStatement.setString(1, userId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                FlightList.add(new Flight(
                        null,
                        resultSet.getDate("Date"),
                        resultSet.getString("Destination"),
                        resultSet.getString("Depart"),
                        0));
            }
            flightsTable.setItems(FlightList);
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
            main.setUserId(userId);

            Stage userInterfaceStage = new Stage();
            userInterfaceStage.setScene(new Scene(root));
            userInterfaceStage.setTitle("User Information");
            currentStage.close();

            userInterfaceStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToTickets(javafx.event.ActionEvent event) throws IOException {
        try {
            Stage currentStage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Ticket.fxml"));
            Parent root = loader.load();
            BuyTicketController main = loader.getController();
            main.setUserId(userId);

            Stage ticketStage = new Stage();
            ticketStage.setScene(new Scene(root));
            ticketStage.setTitle("Ticket");

            currentStage.close();

            ticketStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLogOutClick() {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**declare images**/
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

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Destinations");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of Users");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        fetchDataAndPopulateChart(BarChart);

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
    }
/**fill data in barchar**/
    private void fetchDataAndPopulateChart(BarChart<String, Number> chart) {
        DataBaseConnection dbConnection = new DataBaseConnection();
        try (Connection connection = dbConnection.getConnection()) {
            Statement statement = connection.createStatement();
            String query = "SELECT Depart, COUNT(iduser) AS num_users FROM ticket GROUP BY Depart";
            ResultSet resultSet = statement.executeQuery(query);
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            while (resultSet.next()) {
                String destination = resultSet.getString("Depart");
                int numUsers = resultSet.getInt("num_users");
                series.getData().add(new XYChart.Data<>(destination, numUsers));
            }
            chart.getData().add(series);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadUserTrip() {
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            Connection connection = dbConnection.getConnection();

            String query = "SELECT COUNT(*) FROM ticket WHERE iduser = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                int ticketCount = resultSet.getInt(1);
                tripNumber.setText(String.valueOf(ticketCount));
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

            String query = "SELECT nom, prenom, age, ProfileImage FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                nom.setText(resultSet.getString("nom"));
                prenom.setText(resultSet.getString("prenom"));
                age.setText(resultSet.getString("age"));
                id.setText(userId);
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

    public void setUserId(String userId) {
        this.userId = userId;
    }
}



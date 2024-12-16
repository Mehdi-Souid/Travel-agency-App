package org.example.demo;
import com.almasb.fxgl.net.Connection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ActualFlightController implements Initializable {

    @FXML
    private Label N_flight;

    @FXML
    private Button btn_workbench;

    @FXML
    private Button btn_workbench1;

    @FXML
    private Button btn_workbench11;

    @FXML
    private Button btn_workbench111;

    @FXML
    private ImageView chatImage;

    @FXML
    private Label date3;

    @FXML
    private Label date2;

    @FXML
    private Label date4;

    @FXML
    private Label datefilght;

    @FXML
    private Label depart_ticket2;

    @FXML
    private Label depart_ticket;

    @FXML
    private Label depart_ticket3;

    @FXML
    private Label depart_ticket4;

    @FXML
    private Label destination_ticket;

    @FXML
    private Label destination_ticket2;

    @FXML
    private Label destination_ticket3;

    @FXML
    private Label destination_ticket4;

    @FXML
    private Label flighid4;

    @FXML
    private Label flightid2;

    @FXML
    private Label flightid3;

    @FXML
    private ImageView homeImage;

    @FXML
    private Label id;

    @FXML
    private Label id1;

    @FXML
    private Label id_ticket;

    @FXML
    private Button logoutButton;

    @FXML
    private ImageView logoutImage;

    @FXML
    private ImageView worldImage;

    @FXML
    private ImageView world2Image;

    @FXML
    private Label name;

    @FXML
    private Label name2;

    @FXML
    private Label name3;

    @FXML
    private Label name4;

    @FXML
    private ImageView planeImage;

    @FXML
    private ImageView ticketImage;

    @FXML
    private Label ticketid2;

    @FXML
    private Label ticketid3;

    @FXML
    private Label ticketid4;
    @FXML
    private Label classe;
    @FXML
    private Label gate;
    @FXML
    private Label gate2;

    @FXML
    private ImageView usersImage;
    @FXML
    private Label idLabel;
    private Stage stage;
    private Scene scene;
    @FXML
    private Parent root;
    @FXML
    private ImageView profileImage;
    @FXML
    private VBox user;


    String query =null;
    Connection connection =null;
    ResultSet resultSet = null;
    String iduser;


    public void switchToTickets(javafx.event.ActionEvent event) throws IOException {
        try {
            Stage currentStage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Ticket.fxml"));
            Parent root = loader.load();
            BuyTicketController main = loader.getController();
            main.setUserId(iduser);

            Stage ticketStage = new Stage();
            ticketStage.setScene(new Scene(root));
            ticketStage.setTitle("Ticket");

            currentStage.close();

            ticketStage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

        File worldFile = new File("images/World.png");
        loadImages(worldImage,worldFile);

        File worldFile2 = new File("images/World.png");
        loadImages(world2Image,worldFile2);
    }

    public void loadInformation(String iduser) {

        try {
            String Fullname=getFullname(iduser);
            idLabel.setText(iduser);
            DataBaseConnection dbConnection = new DataBaseConnection();
            java.sql.Connection connection = dbConnection.getConnection();
            String query = "SELECT idt, idflight, Date, Depart, Destination, classe " +
                    "FROM ticket " +
                    "WHERE iduser = ? " +
                    "ORDER BY Date DESC " +
                    "LIMIT 1";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, iduser);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String idTicket = resultSet.getString("idt");
                String idFlight = resultSet.getString("idflight");
                String date = resultSet.getString("Date");
                String depart = resultSet.getString("Depart");
                String destination = resultSet.getString("Destination");
                String flightClass = resultSet.getString("classe");
                String GatNumber=gateNumber(idFlight);
                gate.setText(GatNumber);
                name.setText(Fullname);
                N_flight.setText(idFlight);
                datefilght.setText(date);
                destination_ticket.setText(destination);
                depart_ticket.setText(depart);
                id_ticket.setText(idTicket);
                classe.setText(flightClass);

                gate2.setText(GatNumber);

                flighid4.setText(idFlight);
                flightid2.setText(idFlight);
                flightid3.setText(idFlight);

                ticketid2.setText(idTicket);
                ticketid3.setText(idTicket);
                ticketid4.setText(idTicket);

                depart_ticket2.setText(depart);
                depart_ticket3.setText(depart);
                depart_ticket4.setText(depart);

                destination_ticket2.setText(destination);
                destination_ticket3.setText(destination);
                destination_ticket4.setText(destination);

                date2.setText(date);
                date3.setText(date);
                date4.setText(date);

                name2.setText(Fullname);
                name3.setText(Fullname);
                name4.setText(Fullname);

                flightid2.setText(idFlight);
                flightid3.setText(idFlight);
                flighid4.setText(idFlight);

                resultSet.close();
                preparedStatement.close();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getFullname(String iduser) {
        String fullname = "";

        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            java.sql.Connection connection = dbConnection.getConnection();
            String query = "SELECT CONCAT(nom, ' ', prenom) AS fullname FROM users WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, iduser);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                fullname = resultSet.getString("fullname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
return fullname;
    }


    public String gateNumber(String idflight) {
        if (idflight.startsWith("FL")) {
            String flightNumber = idflight.substring(2);
            if (flightNumber.startsWith("0")) {
                while (flightNumber.startsWith("0")) {
                    flightNumber = flightNumber.substring(1);
                }
            }
            return flightNumber;
        }
        return idflight;
    }

    public void setUserId(String userId) {
        this.iduser = userId;
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            java.sql.Connection connection = dbConnection.getConnection();

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

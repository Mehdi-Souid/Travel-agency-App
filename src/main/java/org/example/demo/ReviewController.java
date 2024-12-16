package org.example.demo;
import com.almasb.fxgl.net.Connection;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReviewController implements Initializable {

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
    private Button logoutButton;

    @FXML
    private ImageView logoutImage;

    @FXML
    private ImageView planeImage;
    @FXML
    private ImageView homeImage;

    @FXML
    private ImageView ticketImage;
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

    @FXML
    private VBox chatContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField messageField;

    @FXML
    private Button sendButton;

    private DataOutputStream dout;

    String query =null;
    Connection connection =null;
    ResultSet resultSet = null;
    private Client client;
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
    public boolean AlreadyBuy(String userId) {
        try {
            DataBaseConnection dbConnection = new DataBaseConnection();
            java.sql.Connection connection = dbConnection.getConnection();
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
/**socket**/
        try{
            client = new Client(new Socket("localhost", 6005));
            System.out.println("Connected to Server");
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Error creating Client ... ");
        }

        chatContainer.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                scrollPane.setVvalue((Double) newValue);
            }
        });

        client.receiveMessageFromServer(chatContainer);

        sendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend = messageField.getText();
                if (!(messageToSend.isEmpty())) {
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_RIGHT);

                    hBox.setPadding(new Insets(5, 5, 5, 10));
                    Text text = new Text(messageToSend);
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle(
                            "-fx-color: rgb(239, 242, 255);" +
                                    "-fx-background-color: rgb(15, 125, 242);" +
                                    "-fx-background-radius: 20px;");

                    textFlow.setPadding(new Insets(5, 10, 5, 10));
                    text.setFill(Color.color(0.934, 0.925, 0.996));

                    hBox.getChildren().add(textFlow);
                    chatContainer.getChildren().add(hBox);

                    client.sendMessageToServer(messageToSend,iduser);
                    messageField.clear();
                }
            }
        });
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

    public static void addLabel(String messageFromServer, VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5, 5, 5, 10));

        Text text = new Text(messageFromServer);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle(
                "-fx-background-color: rgb(233, 233, 235);" +
                        "-fx-background-radius: 20px;");

        textFlow.setPadding(new Insets(5, 10, 5, 10));
        hBox.getChildren().add(textFlow);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }
}

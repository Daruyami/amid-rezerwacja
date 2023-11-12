package uk.adamcz.patryk.rezerwacja;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

import static uk.adamcz.patryk.rezerwacja.IdentityHandler.SubmitCredentials;

public class RezerwacjaApplication extends Application {

    private static Stage stage;

    private static LocationManager locationManager;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        Group root = getLoginView();

        Scene scene = new Scene(root);
        stage.setTitle("Login to continue...");
        stage.setScene(scene);
        stage.show();
    }

    private static Group getLoginView(){
        Label loginLabel = new Label("Login: ");
        TextField loginField = new TextField();
        Label passwordLabel = new Label("Password: ");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");
        HBox buttonsContainer = new HBox(loginButton, registerButton);

        loginButton.setOnMouseClicked(e -> submitCredentials(loginField, passwordField, false));
        registerButton.setOnMouseClicked(e -> submitCredentials(loginField, passwordField, true));

        VBox container = new VBox(loginLabel, loginField, passwordLabel, passwordField, buttonsContainer);

        return new Group(container);
    }

    private static void submitCredentials(TextField loginField, TextField passwordField, boolean register){
        String login = loginField.getText();
        String password = passwordField.getText();
        boolean result = SubmitCredentials(login, password, register);

        System.out.println(IdentityHandler.loggedIn);
        if (result){
            showMainView();
        }
    }

    private static void showMainView(){
        locationManager = new LocationManager();

        Group root = getMainView();

        Scene scene = new Scene(root);

        stage.setTitle("Welcome to room reservator 2000!");
        stage.setScene(scene);
        stage.show();
    }

    private static Group getMainView(){
        Button listRooms = new Button("List rooms...");
        Button reserveRoomButton = new Button("Reserve a room...");
        Button clearButton = new Button("Clear a reservation...");

        listRooms.setOnMouseClicked(e -> showLocationList());

        HBox container = new HBox(listRooms, reserveRoomButton, clearButton);

        return new Group(container);
    }

    private static void showLocationList(){
        VBox container = new VBox();

        if (locationManager.Locations != null && !locationManager.Locations.isEmpty())
            for (String key : locationManager.Locations.keySet()) {
                Label label = new Label(key);
                container.getChildren().add(label);
            }
        else {
            container.getChildren().add(new Label("No rooms"));
        }

        Group group = new Group(container);
        Scene scene = new Scene(group);
        Stage listStage = new Stage();

        listStage.setScene(scene);
        listStage.setTitle("List of rooms");
        listStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
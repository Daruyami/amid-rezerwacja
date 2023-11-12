package uk.adamcz.patryk.rezerwacja;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

        System.out.println(IdentityHandler.LoggedIn);
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
        Label usernameLabel = new Label("Username: "+IdentityHandler.Name);
        Label roleLabel = new Label("Role: "+IdentityHandler.Role.toString());
        HBox statusContainer = new HBox(usernameLabel, roleLabel);

        Button listRooms = new Button("List rooms...");

        listRooms.setOnMouseClicked(e -> showLocationListView());

        VBox container = new VBox(statusContainer, listRooms);

        if (IdentityHandler.Role.ordinal() >= UserRole.Teacher.ordinal()){
            Button reserveRoomButton = new Button("Reserve a room...");

            reserveRoomButton.setOnMouseClicked(e -> showLocationReservationView());

            container.getChildren().add(reserveRoomButton);
        }

        if (IdentityHandler.Role == UserRole.Administrator){
            Button manageRooms = new Button("Manage rooms...");
            manageRooms.setOnMouseClicked(e -> showManageLocationsView());

            Button manageUsers = new Button("Manage users...");
            manageUsers.setOnMouseClicked(e -> showManageUsersView());

            container.getChildren().add(manageRooms);
            container.getChildren().add(manageUsers);
        }

        return new Group(container);
    }

    private static void showLocationListView(){
        VBox container = new VBox();

        if (locationManager.Locations != null && !locationManager.Locations.isEmpty()) {
            ListView<String> list = new ListView<String>();
            ObservableList<String> items = FXCollections.observableList(locationManager.Locations.keySet().stream().toList());
            list.setItems(items);
            list.setPrefHeight(400);
            list.setPrefWidth(300);

            container.getChildren().add(list);
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

    private static void showLocationReservationView(){
        Label locationLabel = new Label("Room: ");
        TextField locationField = new TextField();

        Label startTimeLabel = new Label("Start time: ");
        TextField startTimeField = new TextField();
        startTimeField.setPromptText("8:00");
        Label endTimeLabel = new Label("End time: ");
        TextField endTimeField = new TextField();
        endTimeField.setPromptText("12:45");

        Button reserveButton = new Button("Reserve");

        reserveButton.setOnMouseClicked(e -> locationManager.ReserveLocation(locationField.getText(), startTimeField.getText(), endTimeField.getText()));

        VBox container = new VBox(locationLabel, locationField, startTimeLabel, startTimeField, endTimeLabel, endTimeField, reserveButton);

        if (IdentityHandler.Role.ordinal() >= UserRole.Teacher.ordinal()) {
            Button clearReservationsButton = new Button("Clear reservations");

            //clearReservationsButton.setOnMouseClicked(e -> locationManager.ClearReservationsLocation(locationField.getText()));

            container.getChildren().add(clearReservationsButton);
        }

        Group group = new Group(container);
        Scene scene = new Scene(group);
        Stage manageStage = new Stage();

        manageStage.setScene(scene);
        manageStage.setTitle("Reserve a room");
        manageStage.show();
    }

    private static void showManageLocationsView(){
        Label locationLabel = new Label("Room: ");
        TextField locationField = new TextField();
        Button addButton = new Button("Add");
        Button removeButton = new Button("Remove");

        addButton.setOnMouseClicked(e -> locationManager.AddLocation(locationField.getText()));
        removeButton.setOnMouseClicked(e -> locationManager.RemoveLocation(locationField.getText()));

        VBox container = new VBox(locationLabel, locationField, addButton, removeButton);

        Group group = new Group(container);
        Scene scene = new Scene(group);
        Stage manageStage = new Stage();

        manageStage.setScene(scene);
        manageStage.setTitle("Manage rooms");
        manageStage.show();

    }

    private static void showManageUsersView(){
        Label loginLabel = new Label("Login: ");
        TextField loginField = new TextField();
        Label usernameLabel = new Label("Name: ");
        TextField usernameField = new TextField();

        ListView<String> list = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList (
                UserRole.Guest.toString(), UserRole.Student.toString(),
                UserRole.Teacher.toString(), UserRole.Administrator.toString());
        list.setItems(items);
        list.setPrefHeight(70);
        list.getSelectionModel().select(0);

        Button modifyButton = new Button("Modify");

        modifyButton.setOnMouseClicked(e -> StorageManager.TryUpdateUser(loginField.getText(), usernameField.getText(),
                UserRole.values()[list.getSelectionModel().getSelectedIndex()]));

        VBox container = new VBox(loginLabel, loginField, usernameLabel, usernameField, list, modifyButton);

        Group group = new Group(container);
        Scene scene = new Scene(group);
        Stage manageStage = new Stage();

        manageStage.setScene(scene);
        manageStage.setTitle("Manage users");
        manageStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
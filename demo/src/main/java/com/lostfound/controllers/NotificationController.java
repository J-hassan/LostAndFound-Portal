package com.lostfound.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.lostfound.models.Notification;
import com.lostfound.storage.DatabaseManager;
import com.lostfound.storage.FileManager;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import com.lostfound.utils.SceneManager;
import com.lostfound.utils.SessionManager;

public class NotificationController {
    @FXML
    private TableView<Notification> notificationTable;
    @FXML
    private TableColumn<Notification, String> nameCol, locationCol, emailCol, categoryCol;
    @FXML
    private TableColumn<Notification, LocalDate> dateCol;

    @FXML
    public void initialize() {
        setupTitleBar();
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("reportedByEmail"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));

        List<Notification> notifications = FileManager.loadNotifications();
        List<Notification> userNotifications = notifications.stream()
                .filter(n -> n.getReceiverEmail().equals(SessionManager.getCurrentUserEmail()))
                .collect(Collectors.toList());

        if (userNotifications == null || userNotifications.isEmpty()) {
            notificationTable.setPlaceholder(new Label("No new matches. Check back later!"));

        } else {
            notificationTable.setItems(FXCollections.observableArrayList(userNotifications));
        }
    }

    @FXML
    private HBox customTitleBar;
    private double xOffset = 0;
    private double yOffset = 0;

    // initialize() method mein ye call karein
    private void setupTitleBar() {
        // Window ko pakar kar hilaane (drag) ke liye
        customTitleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        customTitleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) customTitleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    @FXML
    private void handleClose() {
        System.exit(0);
    }

    @FXML
    private void handleMinimize() {
        Stage stage = (Stage) customTitleBar.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void handleClearAll() {
        // 1. Check karein ke table khali to nahi
        if (notificationTable.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No notifications to clear.");
            alert.showAndWait();
            return;
        }

        // 2. Confirmation lein
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to clear all your notifications?",
                ButtonType.YES, ButtonType.NO);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                String currentUser = SessionManager.getCurrentUserEmail();

                // 3. Database se delete karein
                boolean success = DatabaseManager.clearUserNotifications(currentUser);

                if (success) {
                    // 4. UI Table ko khali karein
                    notificationTable.getItems().clear();

                    // 5. Dashboard badge ko zero karne ke liye refresh
                    DashboardController.refreshBadge();

                    System.out.println("All notifications cleared for: " + currentUser);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Failed to clear notifications from database.").show();
                }
            }
        });
    }

    @FXML
    private void backToDashboard() {
        SceneManager.switchTo("Dashboard");
        DashboardController.refreshBadge();
    }
}

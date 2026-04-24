package com.lostfound.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.lostfound.models.Notification;
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
        List<Notification> allNotifications = FileManager.loadNotifications();

        if (allNotifications == null || allNotifications.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No notifications to clear.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to clear all notifications?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                String currentUser = SessionManager.getCurrentUserEmail();

                allNotifications.removeIf(n -> n.getReceiverEmail().equals(currentUser));

                FileManager.saveNotifications(allNotifications);

                notificationTable.setItems(FXCollections.observableArrayList());

                System.out.println("Notifications cleared for user: " + currentUser);
            }
        });
    }

    @FXML
    private void backToDashboard() {
        SceneManager.switchTo("Dashboard");
        DashboardController.refreshBadge();
    }
}

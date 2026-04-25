package com.lostfound.controllers;

import java.time.LocalDate;
import java.util.List;

import com.lostfound.models.Item;
import com.lostfound.models.Notification;
import com.lostfound.storage.DatabaseManager;
import com.lostfound.storage.FileManager;
import com.lostfound.utils.SceneManager;
import com.lostfound.utils.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, String> nameCol;
    @FXML
    private TableColumn<Item, String> categoryCol;
    @FXML
    private TableColumn<Item, String> locationCol;
    @FXML
    private TableColumn<Item, LocalDate> dateCol;
    @FXML
    private TableColumn<Item, String> typeCol;
    @FXML
    private TableColumn<Item, String> desCol;
    @FXML
    private StackPane notificationBadge;
    @FXML
    private Label badgeCount;
    @FXML
    private HBox customTitleBar;

    private double xOffset = 0;
    private double yOffset = 0;
    private static DashboardController instance;

    @FXML
    public void initialize() {
        setupDraggableBar();
        String userName = SessionManager.getCurrentUserName();
        welcomeLabel.setText("Welcome, " + userName);
        updateNotificationBadge();
        setupTable();
        applyRowStyles();
        loadData();
    }

    private void setupDraggableBar() {
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

    public DashboardController() {
        instance = this;
    }

    public static void refreshBadge() {
        if (instance != null) {
            instance.updateNotificationBadge();
        }
    }

    private void setupTable() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        desCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void loadData() {
        List<Item> allitems = DatabaseManager.getAllItems();
        if (allitems != null) {
            ObservableList<Item> observableList = FXCollections.observableArrayList(allitems);
            itemTable.setItems(observableList);
        }
    }

    @FXML
    private void handleMyItems() {
        System.out.println("My Items Clicked");
        SceneManager.switchTo("MyItems");
    }

    @FXML
    private void handleViewAll() {
        System.out.println("View All Clicked");
        SceneManager.switchTo("Dashboard");
    }

    @FXML
    private void handleReportFound() {
        System.out.println("Report Found Clicked");
        SceneManager.switchTo("ReportForm");
        ReportController reportController = (ReportController) SceneManager.getController();
        if (reportController != null)
            reportController.setMode(false);
    }

    @FXML
    private void handleReportLost() {
        System.out.println("Report Lost Clicked");
        SceneManager.switchTo("ReportForm");
        ReportController reportController = (ReportController) SceneManager.getController();
        if (reportController != null)
            reportController.setMode(true);
    }

    @FXML
    private void handleNotifications() {
        System.out.println("Notifications Clicked");
        SceneManager.switchTo("Notifications");
    }

    private void updateNotificationBadge() {
        String currentUser = SessionManager.getCurrentUserEmail();
        List<Notification> allNotifications = FileManager.loadNotifications();

        // Sirf current user ki notifications count karein
        long count = allNotifications.stream()
                .filter(n -> n.getReceiverEmail().equals(currentUser))
                .count();

        if (count > 0) {
            badgeCount.setText(String.valueOf(count));
            notificationBadge.setVisible(true);
        } else {
            notificationBadge.setVisible(false);
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.setCurrentUserEmail(null); // Session cleared
        SceneManager.switchTo("Login");
    }

    private void applyRowStyles() {
        itemTable.setRowFactory(tv -> new TableRow<Item>() {
            @Override
            protected void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("found-row", "lost-row");

                if (item == null || empty) {
                    setStyle("");
                } else {
                    if (item.getType().equalsIgnoreCase("found")) {
                        getStyleClass().add("found-row");
                    } else {
                        getStyleClass().add("lost-row");
                    }
                }
            }
        });
    }

}

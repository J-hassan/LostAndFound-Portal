package com.lostfound.controllers;

import com.lostfound.models.Item;
import com.lostfound.models.Notification;
import com.lostfound.storage.DatabaseManager;
import com.lostfound.utils.Editing;
import com.lostfound.utils.SceneManager;
import com.lostfound.utils.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MyItemsController {

    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, String> nameCol, categoryCol, typeCol, locationCol, desCol;
    @FXML
    private TableColumn<Item, LocalDate> dateCol;
    @FXML
    private TableColumn<Item, Void> actionCol;
    @FXML
    private Label welcomeLabel;
    @FXML
    private StackPane notificationBadge;
    @FXML
    private Label badgeCount;
    @FXML
    private HBox customTitleBar;

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public void initialize() {
        setupDraggableBar();
        if (SessionManager.getCurrentUserName() != null) {
            welcomeLabel.setText("Welcome, " + SessionManager.getCurrentUserName());
        }
        updateNotificationBadge();
        setupTable();
        applyRowStyles();
        loadMyItems();
    }

    private void setupTable() {

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        desCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        actionCol.setCellFactory(params -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(10, editBtn, deleteBtn);
            {
                editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
                deleteBtn.setStyle("-fx-background-color: tomato; -fx-text-fill: white;");

                editBtn.setOnAction(e -> {
                    Item item = getTableView().getItems().get(getIndex());
                    handleEdit(item);
                });

                deleteBtn.setOnAction(e -> {
                    Item item = getTableView().getItems().get(getIndex());
                    handleDelete(item);
                });

            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
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

    private void loadMyItems() {
        String currentUserEmail = SessionManager.getCurrentUserEmail();
        List<Item> allItems = DatabaseManager.getAllItems();

        List<Item> myItems = allItems.stream().filter(item -> item.getReportedByEmail().equals(currentUserEmail))
                .collect(Collectors.toList());

        itemTable.setItems(FXCollections.observableArrayList(myItems));
    }

    private void handleDelete(Item item) {
        // Confirmation Dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete this item?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                System.out.println("Deleting: " + item.getName());
                DatabaseManager.deleteItem(item.getId());
                loadMyItems();
                updateNotificationBadge();
            }
        });
    }

    private void updateNotificationBadge() {
        String currentUser = SessionManager.getCurrentUserEmail();
        List<Notification> allNotifications = DatabaseManager.getAllNotifications();

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

    private void handleEdit(Item item) {
        System.out.println("Editing: " + item.getName());
        Editing.setEditingItems(item.getId(), item.getName(), item.getCategory(), item.getType(),
                item.getLocation(), item.getDescription(), item.getDate());
        Editing.setEditingMode(true);
        SceneManager.switchTo("ReportForm");
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

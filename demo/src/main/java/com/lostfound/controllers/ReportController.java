package com.lostfound.controllers;

import java.util.List;

import com.lostfound.engine.MatchEngine;
import com.lostfound.models.*;
import com.lostfound.models.Item;
import com.lostfound.storage.FileManager;
import com.lostfound.utils.*;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ReportController {

    // --- FXML Fields ---
    @FXML
    private Text formTitle;
    @FXML
    private TextField nameField;
    @FXML
    private TextField locationField;
    @FXML
    private ComboBox<String> categoryBox;
    @FXML
    private TextArea descriptionField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button submitBtn;

    // --- State Variable ---
    private boolean isLostMode = true;

    @FXML
    public void initialize() {
        setupTitleBar();
        // Initialize category options
        categoryBox.getItems().addAll("ID CARD", "WALLET", "KEYS", "ELECTRONICS", "CLOTHING", "OTHER");
        if (Editing.isEditingMode()) {
            formTitle.setText("Edit Item");
            editingForm();
            submitBtn.setText("Update Item");
        }
    }

    /**
     * Dashboard se mode set karne ke liye (Lost vs Found)
     */
    public void setMode(boolean lostMode) {
        this.isLostMode = lostMode;
        if (lostMode) {
            formTitle.setText("Report Lost Item");
        } else {
            formTitle.setText("Report Found Item");
        }
    }

    /**
     * Submit button ka action
     */
    @FXML
    private void handleSubmit() {
        String name = nameField.getText();
        String location = locationField.getText();
        String description = descriptionField.getText();
        String category = categoryBox.getValue();

        if (!validateFields()) {
            statusLabel.setStyle("-fx-text-fill: #ff4d4d");
            statusLabel.setText("Please fill all the fields!");
            return;
        }

        Item finalItem;
        String email = SessionManager.getCurrentUserEmail();

        if (Editing.isEditingMode()) {
            if (Editing.getInstance().getType().equalsIgnoreCase("lost")) {
                finalItem = new LostItem(name, category, description, location, email);
            } else {
                finalItem = new FoundItem(name, category, description, location, email);
            }
            FileManager.updateItem(Editing.getInstance().getId(), finalItem);
            statusLabel.setText("Updating item... Please wait.");
        } else {
            if (isLostMode) {
                finalItem = new LostItem(name, category, description, location, email);
            } else {
                finalItem = new FoundItem(name, category, description, location, email);
            }
            FileManager.addItem(finalItem);
            statusLabel.setText("Reporting item... Please wait.");
        }

        statusLabel.setStyle("-fx-text-fill: #00ff00;");

        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(e -> {
            Platform.runLater(() -> {
                List<Item> matches = MatchEngine.findMatches(finalItem);
                if (matches != null && !matches.isEmpty()) {
                    List<Notification> notifications = FileManager.loadNotifications();

                    for (Item matchedItem : matches) {
                        if (!matchedItem.getReportedByEmail().equalsIgnoreCase(email)) {

                            // --- DUPLICATE CHECK LOGIC ---
                            boolean currentExists = false;
                            boolean matchedExists = false;

                            for (Notification n : notifications) {
                                // Check for Current User
                                if (n.getItemId().equals(finalItem.getId()) && n.getReceiverEmail().equals(email)) {
                                    currentExists = true;
                                }
                                // Check for Matched User
                                if (n.getItemId().equals(matchedItem.getId())
                                        && n.getReceiverEmail().equals(matchedItem.getReportedByEmail())) {
                                    matchedExists = true;
                                }
                            }

                            // 1. Notification for CURRENT USER
                            if (!currentExists) {
                                notifications.add(new Notification(
                                        finalItem.getId(), matchedItem.getName(), matchedItem.getLocation(),
                                        matchedItem.getCategory(), matchedItem.getReportedByEmail(),
                                        matchedItem.getDate(), email));
                            }

                            // 2. Notification for MATCHED USER
                            if (!matchedExists) {
                                notifications.add(new Notification(
                                        matchedItem.getId(), finalItem.getName(), finalItem.getLocation(),
                                        finalItem.getCategory(), email, finalItem.getDate(),
                                        matchedItem.getReportedByEmail()));
                            }
                        }
                    }

                    FileManager.saveNotifications(notifications);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Matches Found!");
                    alert.setHeaderText(null);
                    alert.setContentText("We found " + matches.size() + " potential matches!");

                    ButtonType checkNow = new ButtonType("Check Now");
                    ButtonType later = new ButtonType("Later", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(checkNow, later);

                    alert.showAndWait().ifPresent(response -> {
                        if (response == checkNow)
                            SceneManager.switchTo("Notifications");
                        else
                            goBackAfterSubmit();
                    });
                } else {
                    goBackAfterSubmit();
                }
            });
        });

        delay.play();
        Editing.clear();
        Editing.setEditingMode(false);
    }

    @FXML
    private HBox customTitleBar;
    private double xOffset = 0;
    private double yOffset = 0;

    private void setupTitleBar() {
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

    private void goBackAfterSubmit() {
        if (Editing.isEditingMode()) {
            SceneManager.switchTo("MyItems");
        } else {
            SceneManager.switchTo("Dashboard");
        }
    }

    /**
     * Validation ka helper function
     */
    private boolean validateFields() {
        // Return true if all inputs are correct
        String name = nameField.getText();
        String location = locationField.getText();
        String description = descriptionField.getText();
        String category = categoryBox.getValue();

        if (name.isEmpty() || location.isEmpty() || description.isEmpty() || category == null) {
            return false;
        }
        return true;
    }

    /**
     * Cancel ya Back button ka action
     */
    @FXML
    private void handleCancel() {
        if (Editing.isEditingMode()) {
            SceneManager.switchTo("MyItems");
            return;
        }
        SceneManager.switchTo("Dashboard");
    }

    private void editingForm() {
        Editing editing = Editing.getInstance();
        nameField.setText(editing.getName());
        locationField.setText(editing.getLocation());
        descriptionField.setText(editing.getDescription());
        categoryBox.setValue(editing.getCategory());
    }
}

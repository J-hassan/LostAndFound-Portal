package com.lostfound.controllers;

import java.util.List;

import com.lostfound.models.Student;
import com.lostfound.storage.FileManager;
import com.lostfound.utils.SceneManager;
import com.lostfound.utils.SessionManager;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private VBox loginBox;

    @FXML
    private void initialize() {
        setupTitleBar();
        String tempEmail = SessionManager.getTempUserEmail();
        if (tempEmail != null && !tempEmail.isEmpty()) {
            emailField.setText(tempEmail);
            passwordField.setText(SessionManager.getTempUserPassword());

            // Use karne ke baad clear kar dein
            SessionManager.clearCredentials();
        }
        TranslateTransition slide = new TranslateTransition(Duration.seconds(0.8), loginBox);
        slide.setFromX(-600);
        slide.setToX(0);
        slide.play();
    }

    @FXML
    private HBox customTitleBar;
    private double xOffset = 0;
    private double yOffset = 0;

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
    private void handleLogin() {
        System.out.println("Logging in..."); // for testing purposes

        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill all the fields!");
            return;
        }

        List<Student> students = FileManager.getAllStudents();
        boolean found = false;
        if (students != null) {
            for (Student student : students) {
                if (student.getEmail().equals(email) && student.getPassword().equals(password)) {
                    // creating a session for the logged in user
                    SessionManager.setCurrentUserEmail(student.getEmail());
                    SessionManager.setCurrentUserName(student.getName());
                    found = true;
                    break;
                }
            }
        }

        if (found) {
            errorLabel.setStyle("-fx-text-fill: #00ff00;");
            errorLabel.setText("Logging in... Please wait.");

            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished((e) -> {
                SceneManager.switchTo("Dashboard");
            });

            delay.play();
        } else {
            errorLabel.setStyle("-fx-text-fillL: #ff4d4d");
            errorLabel.setText("Invalid Email or Password. Please try again!");
        }
    }

    @FXML
    private void goToRegister() {
        SceneManager.switchTo("Signup");
    }
}

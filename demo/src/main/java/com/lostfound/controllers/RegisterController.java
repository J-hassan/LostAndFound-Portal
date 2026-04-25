package com.lostfound.controllers;

import java.util.List;

import com.lostfound.models.Student;
import com.lostfound.storage.DatabaseManager;
import com.lostfound.storage.FileManager;
import com.lostfound.utils.SceneManager;
import com.lostfound.utils.SessionManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class RegisterController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private VBox RegisterVBox;

    @FXML
    public void initialize() {
        setupTitleBar();
        TranslateTransition slide = new TranslateTransition(Duration.seconds(0.8), RegisterVBox);
        slide.setFromX(-600);
        slide.setToX(0);
        slide.play();
    }

    @FXML
    private void handleRegister() {

        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            errorLabel.setStyle("-fx-text-fillL: #ff4d4d");
            errorLabel.setText("Please fill all the fields!");
            return;
        }
        if (password.length() < 4) {
            errorLabel.setStyle("-fx-text-fillL: #ff4d4d");
            errorLabel.setText("Password must contain atleast 4 characters!");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            errorLabel.setStyle("-fx-text-fillL: #ff4d4d");
            errorLabel.setText("Please enter a valid email address!");
            return;
        }

        // List<Student> students = FileManager.getAllStudents();
        // if (students != null) {
        // for (Student student : students) {
        // if (student.getEmail().equals(email)) {
        // errorLabel.setStyle("-fx-text-fillL: #ff4d4d");
        // errorLabel.setText("Email is already registered! Please login.");
        // return;
        // }
        // }
        // }
        // errorLabel.setStyle("-fx-text-fill: #00ff00;");
        // errorLabel.setText("Registering Student... Please wait.");

        // Student newStudent = new Student(nameField.getText(), emailField.getText(),
        // passwordField.getText());
        // FileManager.registerStudent(newStudent);
        // SessionManager.setCredentials(email, password);

        // RegisterController's handleRegister method mein ye change karein:
        if (DatabaseManager.registerStudent(name, email, password)) {
            // Successful
            SessionManager.setCredentials(email, password);
            errorLabel.setStyle("-fx-text-fill: #00ff00;");
            errorLabel.setText("Registration Successful! Redirecting...");

            PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
            delay.setOnFinished(e -> SceneManager.switchTo("Login"));
            delay.play();
        } else {
            errorLabel.setText("Registration failed! Email might already exist.");
        }

        // PauseTransition delay = new PauseTransition(Duration.seconds(2));
        // delay.setOnFinished(event -> {
        // SceneManager.switchTo("Login");
        // });
        // delay.play();
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

    @FXML
    private void goToLogin() {
        SceneManager.switchTo("Login");
    }
}

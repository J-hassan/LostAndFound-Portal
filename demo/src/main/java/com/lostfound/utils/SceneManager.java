package com.lostfound.utils;

import java.net.URL;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class SceneManager {
    private static Stage primaryStage;
    private static FXMLLoader lastLoader;
    public static HashMap<String, Parent> screens = new HashMap<>();

    public static void setStage(Stage primStage) {
        primaryStage = primStage;
    }

    public static void switchTo(String fxmlName) {
        try {
            URL url = SceneManager.class.getResource("/views/" + fxmlName + ".fxml");
            lastLoader = new FXMLLoader(url);
            Parent root = lastLoader.load();

            // --- ANIMATION START ---
            applyFadeIn(root);
            // -----------------------

            primaryStage.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void applyFadeIn(Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(600), node);
        fade.setFromValue(0.0); // Bilkul gayab
        fade.setToValue(1.0); // Mukammal zahir
        fade.play();
    }

    public static Object getController() {
        if (lastLoader != null) {
            return lastLoader.getController();
        }
        return null;
    }
}

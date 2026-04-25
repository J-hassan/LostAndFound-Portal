package com.lostfound;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import com.lostfound.utils.SceneManager;

public class App extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.setStage(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/views/Login.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 1100, 700);
        stage.setScene(scene);

        try {
            Image icon = new Image(getClass().getResourceAsStream("/images/logo.png"));
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Failed to load application icon, using default icon instead.");
        }
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Lost & Found Portal - MCS");
        stage.setWidth(1400);
        stage.setHeight(750);
        stage.centerOnScreen();
        stage.show();
    }

}

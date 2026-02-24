package com.example.restuarantms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * HelloApplication - main JavaFX application class
 */
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Set application icon (shown in title bar and taskbar)
        // Place your icon file in src/main/resources/Images/ (or adjust path)
        stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("/Images/gdk.png")));

        stage.setTitle("GDK MS");
        stage.setMinHeight(435);
        stage.setMinWidth(612);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
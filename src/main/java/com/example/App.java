package com.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Metadata tool");
        stage.setResizable(false);
        scene = new Scene(loadFXML("primary", stage), 1232, 774);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml, Stage stage) throws IOException {
        scene.setRoot(loadFXML(fxml, stage));
    }

    private static Parent loadFXML(String fxml, Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Object ob = fxmlLoader.load();
        PrimaryController pc = fxmlLoader.getController();
        pc.setStage(stage);
        return (Parent)ob;
    }

    public static void main(String[] args) {
        launch();
    }

}
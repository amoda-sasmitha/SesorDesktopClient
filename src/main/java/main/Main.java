package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/pages/index.fxml"));
        primaryStage.setTitle("Hello World");
        Scene dashboard = new Scene(root, 1500, 800);
        dashboard.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        primaryStage.setScene(dashboard);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
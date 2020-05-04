package main;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;

public class Main extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void start(final Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/pages/login.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - xOffset);
                primaryStage.setY(event.getScreenY() - yOffset);
            }
        });

        Scene dashboard = new Scene(root, 800, 515);
        dashboard.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");
        primaryStage.setScene(dashboard);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

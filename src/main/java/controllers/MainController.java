package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private BorderPane borderpane;
    @FXML
    private BorderPane mainpanel;

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("from dashboard controller");
        loadPage("dashboard" , mainpanel );


    }

    @FXML
    void onAdminClicked(MouseEvent event) {
        System.out.println("Admin Clicked");
        loadPage("admins" , mainpanel );
    }

    @FXML
    void onDashboardClicked(MouseEvent event) {
        System.out.println("Dashboard Clicked");
        loadPage("dashboard" , mainpanel );
    }

    @FXML
    void onSensorClicked(MouseEvent event) {
        System.out.println("Sensor Clicked");
        loadPage("sensors" , mainpanel );
    }

    public void loadPage(String page, BorderPane bp) {
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/pages/" + page + ".fxml"));
            bp.setCenter(root);
        } catch (IOException ex) {
            System.out.println("Exception : " +ex);
        }

    }

}

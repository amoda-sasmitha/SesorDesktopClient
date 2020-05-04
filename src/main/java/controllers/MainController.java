package controllers;

import com.model.User;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javax.xml.soap.Text;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

public class MainController implements Initializable {

    Timer timer = new Timer();
    @FXML
    private BorderPane borderpane;
    @FXML
    private BorderPane mainpanel;

    @FXML
    private Label emailadmin;

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("from dashboard controller");
        loadPage("dashboard" , mainpanel );

        emailadmin.setText(User.getInstance().getEmail());


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
            timer.cancel();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pages/" + page + ".fxml"));
            Parent root = (Parent)fxmlLoader.load();

            if(page == "dashboard"){
                timer = new Timer();
                DashboardController dashboardController = fxmlLoader.<DashboardController>getController();
                dashboardController.init(timer);
            }
            if(page == "sensors"){
                timer = new Timer();
                SensorsController sensorsController = fxmlLoader.<SensorsController>getController();
                sensorsController.init(timer);
            }

            bp.setCenter(root);
        } catch (IOException ex) {
            System.out.println("Exception : " +ex);
        }

    }

    public void onManageSensorClicked(MouseEvent mouseEvent) {
        System.out.println("Manage Sensors Clicked");
        loadPage("manage_sensors" , mainpanel );
    }
}

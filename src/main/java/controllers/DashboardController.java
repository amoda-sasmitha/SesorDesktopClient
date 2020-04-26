package controllers;

import com.service.ISensorService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.model.Sensor;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardController implements Initializable {

    @FXML
    private TableView<Sensor> sensortable;

    @FXML
    private TableColumn<Sensor,Integer> id;

    @FXML
    private TableColumn<Sensor,String> floor_no;

    @FXML
    private TableColumn<Sensor, String> room_no;

    @FXML
    private TableColumn<Sensor, Integer> co2_level;

    @FXML
    private TableColumn<Sensor,Integer> smoke_level;

    @FXML
    private TableColumn<Sensor,String> status;

    ArrayList<Sensor> sensorsData = new ArrayList<Sensor>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setTableData(){
        sensortable.getItems().removeAll();

        id.setCellValueFactory( new PropertyValueFactory<Sensor, Integer>("id"));
        floor_no.setCellValueFactory( new PropertyValueFactory<Sensor, String>("floor_id"));
        room_no.setCellValueFactory( new PropertyValueFactory<Sensor, String>("room_id"));
        co2_level.setCellValueFactory( new PropertyValueFactory<Sensor, Integer>("co2_level"));
        smoke_level.setCellValueFactory( new PropertyValueFactory<Sensor, Integer>("smoke_level"));
        status.setCellValueFactory( new PropertyValueFactory<Sensor, String>("status"));

        sensortable.setItems(FXCollections.observableArrayList(sensorsData));

        id.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        floor_no.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        room_no.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        co2_level.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        smoke_level.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
    }

    public void init(Timer timer){
        ISensorService service = null;
        System.setProperty("java.security.policy", "file:allowall.policy");

        try {

            if(System.getSecurityManager() == null ){
                System.setSecurityManager( new RMISecurityManager() );
            }

            service = (ISensorService) Naming.lookup("//localhost:4500/SensorService");

            final ISensorService finalService = service;


            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        sensorsData =  finalService.getAllSensorsCurrentData();
                        setTableData();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }, 0, 5000);

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}

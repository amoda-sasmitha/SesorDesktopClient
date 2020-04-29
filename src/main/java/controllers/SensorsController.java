package controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.*;

import com.model.Sensor;
import com.model.SensorLog;
import com.service.ISensorService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class SensorsController {

    @FXML
    private Label currentco2;

    @FXML
    private Label sensor_id_label;

    @FXML
    private Label currentsmoke;

    @FXML
    private ChoiceBox<String> sensor_id_selector;

    @FXML
    private Label flood_id;

    @FXML
    private Label room_id;

    @FXML
    private CategoryAxis smoke_x;

    @FXML
    private NumberAxis smoke_y;

    @FXML
    private CategoryAxis co2_x;

    @FXML
    private NumberAxis co2_y;

    @FXML
    private TableView<SensorLog> sensor_log;

    @FXML
    private TableColumn<SensorLog, Integer> co2_level;

    @FXML
    private TableColumn<SensorLog, Integer> smoke_level;

    @FXML
    private TableColumn<SensorLog, String> updated_at;

    @FXML
    private AreaChart<String,Number> smoke_chart;

    @FXML
    private AreaChart<String,Number> co2_chart;

    private int current_id;
    private ArrayList<Sensor> listofsensors = new ArrayList<Sensor>();
    private Sensor current_sensor = new Sensor();
    private LinkedList<SensorLog> current_sensor_log = new LinkedList<SensorLog>();
    ISensorService service = null;
    @FXML
    void initialize() {

    }

    public void init(Timer timer){

        System.setProperty("java.security.policy", "file:allowall.policy");

        try {

            if(System.getSecurityManager() == null ){
                System.setSecurityManager( new RMISecurityManager() );
            }

            service = (ISensorService) Naming.lookup("//localhost:4500/SensorService");
            listofsensors = service.getAllSensorsCurrentData();
            current_id = listofsensors.get(0).getId();
            initselector();

            final ISensorService finalService = service;

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        current_sensor = finalService.getSensorCurrentData(current_id);
                        current_sensor_log = finalService.getSensorLog(current_id);

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                setTableData();
                                setLivechart();
                                setCurrentInfo();
                            }
                        });

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

    public void setTableData(){
        sensor_log.getItems().removeAll();

        updated_at.setCellValueFactory( new PropertyValueFactory<SensorLog, String>("datetime"));
        co2_level.setCellValueFactory( new PropertyValueFactory<SensorLog, Integer>("co2_level"));
        smoke_level.setCellValueFactory( new PropertyValueFactory<SensorLog, Integer>("smoke_level"));

        sensor_log.setItems(FXCollections.observableArrayList(current_sensor_log));

        updated_at.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        co2_level.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        smoke_level.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
    }

    public void setLivechart(){
        XYChart.Series smoke = new XYChart.Series();
        XYChart.Series co2 = new XYChart.Series();
        co2.setName("co2");
        smoke.setName("smoke");

        for (int i = 0; i < current_sensor_log.size(); i++) {
            co2.getData().add(new XYChart.Data( current_sensor_log.get(i).getDatetime() , current_sensor_log.get(i).getCo2_level() ));
            smoke.getData().add(new XYChart.Data( current_sensor_log.get(i).getDatetime(), current_sensor_log.get(i).getSmoke_level() ));
        }
        smoke_chart.getData().clear();
        co2_chart.getData().clear();

        co2_chart.layout();
        smoke_chart.layout();

        smoke_chart.getData().add(smoke);
        co2_chart.getData().add(co2);

        smoke_chart.setLegendVisible(false);
        co2_chart.setLegendVisible(false);
    }

    public void setCurrentInfo(){
        currentco2.setText( current_sensor.getCo2_level() + ".00");
        currentsmoke.setText( current_sensor.getSmoke_level() + ".00");
        flood_id.setText(( current_sensor.getFloor_id() ));
        room_id.setText(( current_sensor.getRoom_id() ));
    }

    public void initselector(){

        ArrayList<String> list = new ArrayList<String>();
        for (Sensor listofsensor : listofsensors) {
            list.add( listofsensor.getId()+"");
        }
        ObservableList<String> options = FXCollections.observableArrayList(list);

        sensor_id_selector.setValue(list.get(0)); // this statement shows default value
        sensor_id_label.setText(list.get(0));

        sensor_id_selector.setItems(options); // this statement adds all values in choiceBox

        sensor_id_selector.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                System.out.println(sensor_id_selector.getItems().get((Integer) number2));
                current_id = Integer.parseInt(sensor_id_selector.getItems().get((Integer) number2));
                sensor_id_label.setText("" + current_id );
                try {
                    current_sensor = service.getSensorCurrentData(current_id);
                    current_sensor_log = service.getSensorLog(current_id);
                    setTableData();
                    setLivechart();
                    setCurrentInfo();

                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }
        });
    }


}

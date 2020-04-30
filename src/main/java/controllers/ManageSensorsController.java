package controllers;

import com.model.Admin;
import com.model.Sensor;
import com.service.IAdminService;
import com.service.ISensorService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.StageStyle;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ManageSensorsController implements Initializable {

    @FXML
    private Button mode_btn;

    @FXML
    private TextField id_input;

    @FXML
    private TextField floor_id_input;

    @FXML
    private TextField room_id_input;

    @FXML
    private Button insert;

    @FXML
    private Button edit;

    @FXML
    private Button delete;

    @FXML
    private TableView<Sensor> sensor_table;

    @FXML
    private TableColumn<Sensor, Integer> id;

    @FXML
    private TableColumn<Sensor, String> floor_id;

    @FXML
    private TableColumn<Sensor, String> room_id;

    @FXML
    private TableColumn<Sensor, String> updated_time;

    @FXML
    private TableColumn<Sensor, String> updated_time_s;

    private String mode = "Add";
    private ISensorService service = null;
    private ArrayList<Sensor> sensors_array = new ArrayList<Sensor>();
    private Sensor activeItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeMode("Add");
        onChangeId();

        System.setProperty("java.security.policy", "file:allowall.policy");

        try {

            if(System.getSecurityManager() == null ){
                System.setSecurityManager( new RMISecurityManager() );
            }
            service  = (ISensorService) Naming.lookup("//localhost:4500/SensorService");
            sensors_array = service.getAllSensorsCurrentData();
            setTableData();

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void setTableData() {
        DateFormat time = new SimpleDateFormat("HH:mm:ss");
        DateFormat date = new SimpleDateFormat("EEEEE dd MMMMM yyyy");
        for (Sensor sensor : sensors_array) {
            Calendar cal = javax.xml.bind.DatatypeConverter.parseDateTime(sensor.getUpdated_at());
            sensor.setStatus(time.format( cal.getTime() ));
            sensor.setUpdated_at( date.format(cal.getTime()));
        }
        sensor_table.getItems().removeAll();

        id.setCellValueFactory( new PropertyValueFactory<Sensor, Integer>("id"));
        floor_id.setCellValueFactory( new PropertyValueFactory<Sensor, String>("floor_id"));
        room_id.setCellValueFactory( new PropertyValueFactory<Sensor, String>("room_id"));
        updated_time.setCellValueFactory( new PropertyValueFactory<Sensor, String>("updated_at"));
        updated_time_s.setCellValueFactory( new PropertyValueFactory<Sensor, String>("status"));

        sensor_table.setItems(FXCollections.observableArrayList(sensors_array));

        id.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        floor_id.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        room_id.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        updated_time.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        updated_time_s.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
    }

    public void onChangeMode(ActionEvent actionEvent) {
        if(this.mode == "Add"){
            changeMode("Edit");
        }else{
            changeMode("Add");
        }
    }

    public void onClickInsert(ActionEvent actionEvent) throws RemoteException {
        if( floor_id_input.getText().isEmpty() || room_id_input.getText().isEmpty() ){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Warning");
            alert.setContentText("All Fields Required");
            alert.showAndWait();
        }else{

            Sensor new_sensor = new Sensor();
            new_sensor.setFloor_id( floor_id_input.getText().trim());
            new_sensor.setRoom_id( room_id_input.getText().trim());
            service.insertSensor(new_sensor);
            sensors_array = service.getAllSensorsCurrentData();
            setTableData();
            floor_id_input.setText("");
            room_id_input.setText("");
        }
    }

    public void onClickEdit(ActionEvent actionEvent) throws RemoteException {
        if(!id_input.getText().isEmpty()) {
            if (floor_id_input.getText().isEmpty() || room_id_input.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Warning");
                alert.setContentText("All Fields Required");
                alert.showAndWait();
            } else {
                Sensor new_sensor = new Sensor();
                new_sensor.setFloor_id(floor_id_input.getText().trim());
                new_sensor.setRoom_id(room_id_input.getText().trim());
                new_sensor.setId( Integer.parseInt( id_input.getText().trim()));
                service.editSensor(new_sensor);
                sensors_array = service.getAllSensorsCurrentData();
                setTableData();

            }
        }

    }

    public void onClickDelete(ActionEvent actionEvent) throws RemoteException {
        if(!id_input.getText().isEmpty()){

            service.deleteSensor(Integer.parseInt(id_input.getText().trim()));
            sensors_array = service.getAllSensorsCurrentData();
            setTableData();
        }
    }

    public void changeMode(String mode){
        this.mode = mode;
        if(mode == "Add"){
            floor_id_input.setText("");
            room_id_input.setText("");
            id_input.setText("");

            mode_btn.setText("Change to Edit");

            insert.setVisible(true);
            edit.setVisible(false);
            delete.setVisible(false);

            id_input.setPromptText("Id Will be Auto Genarated");
            id_input.setDisable(true);

        }else if( mode == "Edit"){
            mode_btn.setText("Change to Register");

            insert.setVisible(false);
            edit.setVisible(true);
            delete.setVisible(true);

            id_input.setPromptText("Enter ID to Edit/Delete");
            id_input.setDisable(false);

        }
    }

    public void onChangeId(){
        id_input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if(newValue.length() > 0 ){
                    if( isNumeric(newValue) ){
                        for (Sensor sensor : sensors_array) {
                            if(sensor.getId() == Integer.parseInt(newValue)){
                                activeItem = sensor;
                                setActiveItem(sensor);
                            }
                        }
                    }
                }else{
                    floor_id_input.setText("");
                    room_id_input.setText("");

                }
            }
        });
    }

    private void setActiveItem(Sensor sensor) {
        if(mode == "Add"){
            floor_id_input.setText("");
            room_id_input.setText("");

        }else if( mode == "Edit"){
            floor_id_input.setText(sensor.getFloor_id());
           room_id_input.setText(sensor.getRoom_id());
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }


}

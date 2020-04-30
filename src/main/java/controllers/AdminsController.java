package controllers;

import com.model.Admin;
import com.model.SensorLog;
import com.service.IAdminService;
import com.service.ISensorService;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TimerTask;

public class AdminsController implements Initializable {

    @FXML
    private Button mode_btn;

    @FXML
    private TextField id_input;

    @FXML
    private TextField password_input;

    @FXML
    private TextField username_input;

    @FXML
    private TextField email_input;

    @FXML
    private TextField phone_input;

    @FXML
    private Button insert;

    @FXML
    private Button edit;

    @FXML
    private Button delete;

    @FXML
    private TableView<Admin> admin_table;

    @FXML
    private TableColumn<Admin,Integer> id;

    @FXML
    private TableColumn<Admin,String> username;

    @FXML
    private TableColumn<Admin,String> email;

    @FXML
    private TableColumn<Admin,String> phone;

    private String mode = "Register";
    private IAdminService service = null;
    private ArrayList<Admin> admins_array = new ArrayList<Admin>();
    private Admin activeItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        changeMode("Register");
        onChangeId();
        System.setProperty("java.security.policy", "file:allowall.policy");

        try {

            if(System.getSecurityManager() == null ){
                System.setSecurityManager( new RMISecurityManager() );
            }
            service = (IAdminService) Naming.lookup("//localhost:4500/AdminService");
            admins_array = service.getAllAdmins();
            setTableData();

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onClickInsert(javafx.event.ActionEvent actionEvent) throws RemoteException {
       if( email_input.getText().isEmpty() || phone_input.getText().isEmpty() || username_input.getText().isEmpty()){
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.initStyle(StageStyle.UTILITY);
           alert.setTitle("Warning");
           alert.setContentText("All Fields Required");
           alert.showAndWait();
       }else{
           Admin admin = new Admin();
           admin.setName(username_input.getText().trim());
           admin.setPhone( phone_input.getText().trim());
           admin.setEmail( email_input.getText() );
           service.insertAdmin(admin);
           admins_array = service.getAllAdmins();
           setTableData();
       }

    }

  

    public void onClickDelete(javafx.event.ActionEvent actionEvent) throws RemoteException {
        if(!id_input.getText().isEmpty()){
            if(password_input.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Warning");
                alert.setContentText("Current password is required to remove an admin");
                alert.showAndWait();
            }else{
                service.deleteAdmin( Integer.parseInt(id_input.getText().trim() ) , password_input.getText().trim() );
                admins_array = service.getAllAdmins();
                setTableData();
            }
        }
    }

    public void onChangeMode(ActionEvent actionEvent) {
        if(this.mode == "Register"){
            changeMode("Edit");
        }else{
            changeMode("Register");
        }
    }

    public void changeMode(String mode){
        this.mode = mode;
       if(mode == "Register"){
           email_input.setText("");
           phone_input.setText("");
           username_input.setText("");
           password_input.setText("");
           id_input.setText("");
           mode_btn.setText("Change to Edit");
           insert.setVisible(true);
           edit.setVisible(false);
           delete.setVisible(false);
           id_input.setPromptText("Id Will be Auto Genarated");
           id_input.setDisable(true);
           password_input.setDisable(true);
           email_input.setDisable(false);
           username_input.setDisable(false);
           phone_input.setDisable(false);
       }else if( mode == "Edit"){
           mode_btn.setText("Change to Register");
           insert.setVisible(false);
           edit.setVisible(false);
           delete.setVisible(true);
           id_input.setPromptText("Enter ID to Edit/Delete");
           id_input.setDisable(false);
           password_input.setDisable(false);
           email_input.setDisable(true);
           username_input.setDisable(true);
           phone_input.setDisable(true);

       }
    }

    public void onChangeId(){
        id_input.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable,
                                String oldValue, String newValue) {

                if(newValue.length() > 0 ){
                    if( isNumeric(newValue) ){
                        for (Admin admin : admins_array) {
                            if(admin.getId() == Integer.parseInt(newValue)){
                                activeItem = admin;
                                setActiveItem(admin);
                            }
                        }
                    }
                }else{
                    email_input.setText("");
                    phone_input.setText("");
                    username_input.setText("");
                    password_input.setText("");
                }
            }
        });
    }



    public void setTableData(){
        admin_table.getItems().removeAll();

        id.setCellValueFactory( new PropertyValueFactory<Admin, Integer>("id"));
        username.setCellValueFactory( new PropertyValueFactory<Admin, String>("name"));
        email.setCellValueFactory( new PropertyValueFactory<Admin, String>("email"));
        phone.setCellValueFactory( new PropertyValueFactory<Admin, String>("phone"));

        admin_table.setItems(FXCollections.observableArrayList(admins_array));

        id.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        username.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        email.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
        phone.setStyle("-fx-size: 35.0px;-fx-min-height: 45.0px;");
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    public void setActiveItem(Admin admin){
        if(mode == "Register"){
            email_input.setText("");
            phone_input.setText("");
            username_input.setText("");
            password_input.setText("");

        }else if( mode == "Edit"){
            email_input.setText(admin.getEmail());
            phone_input.setText(admin.getPhone());
            username_input.setText(admin.getName());
            password_input.setText("");
        }
    }

    public void onClickEdit(ActionEvent actionEvent) {
    }
}

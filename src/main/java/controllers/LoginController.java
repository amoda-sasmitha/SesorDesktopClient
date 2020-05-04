package controllers;

import com.model.User;
import com.service.IAdminService;
import com.service.ISensorService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.TimerTask;

public class LoginController  implements Initializable {

    @FXML
    private Label lblUssername;

    @FXML
    private Label lblPassword;

    @FXML
    private Label error;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onClickSubmit(javafx.event.ActionEvent actionEvent) {

        if(txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() ) {

        }else{
            IAdminService service = null;
            System.setProperty("java.security.policy", "file:allowall.policy");
            try {

                if(System.getSecurityManager() == null ){
                    System.setSecurityManager( new RMISecurityManager() );
                }

                service = (IAdminService) Naming.lookup("//localhost:4500/AdminService");
                boolean result = service.loginUser(txtUsername.getText() , txtPassword.getText());
                System.out.println(result);

                if(result){
                    error.setText("");
                    User user = User.getInstance();
                    user.setEmail(txtUsername.getText());
                    Node node = (Node) actionEvent.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();

                    Scene scene = new Scene((Parent) FXMLLoader.load(getClass().getResource("/pages/index.fxml")));
                    stage.setScene(scene);
                    stage.show();
                }else{
                    error.setText("Login Failed !");
                }
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }catch (IOException e) {
            e.printStackTrace();
            }
        }
    }
}

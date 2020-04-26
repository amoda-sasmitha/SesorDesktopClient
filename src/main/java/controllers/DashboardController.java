package controllers;

import com.service.ISensorService;
import javafx.fxml.Initializable;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ISensorService service = null;
        System.setProperty("java.security.policy", "file:allowall.policy");

        try {

            if(System.getSecurityManager() == null ){
                System.setSecurityManager( new RMISecurityManager() );
            }

            service = (ISensorService) Naming.lookup("//localhost:4500/SensorService");
            System.out.println( service.test("testing") );

        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}

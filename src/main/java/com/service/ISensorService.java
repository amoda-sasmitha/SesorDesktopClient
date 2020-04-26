package com.service;



import models.Sensor;
import models.SensorLog;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ISensorService extends Remote {

    public ArrayList<Sensor> getAllSensorsCurrentData() throws RemoteException;
    public ArrayList<SensorLog> getAllSensorsLog() throws RemoteException;
    public Sensor getSensorCurrentData(int id) throws RemoteException;
    public Sensor getSensorLog(int id) throws RemoteException;
    public String test(String msg) throws RemoteException;
}

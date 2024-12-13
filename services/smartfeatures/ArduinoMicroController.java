package services.smartfeatures;

import exceptions.PMVPhisicalException;
import exceptions.ProceduralException;

import java.net.ConnectException;

public interface ArduinoMicroController {
    void setBTconnection() throws ConnectException;
    
    void startDriving() throws PMVPhisicalException, ConnectException, ProceduralException;
    
    void stopDriving() throws PMVPhisicalException, ConnectException, ProceduralException;
    
    void undoBTconnection();
}
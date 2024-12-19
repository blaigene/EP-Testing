package services.smartfeatures;

// FER EL PAQUET D'EXCEPCIONS DE SERVICES I CANVIAR PER EL DE MICROMOBILITY
import micromobility.exceptions.*;

import java.net.ConnectException;

public interface ArduinoMicroController {
    void setBTconnection() throws ConnectException;
    
    void startDriving() throws PMVPhisicalException, ConnectException, ProceduralException;
    
    void stopDriving() throws PMVPhisicalException, ConnectException, ProceduralException;
    
    void undoBTconnection();
}
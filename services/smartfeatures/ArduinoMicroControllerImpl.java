package services.smartfeatures;

import micromobility.exceptions.PMVPhisicalException;
import micromobility.exceptions.ProceduralException;

import java.net.ConnectException;

public class ArduinoMicroControllerImpl implements ArduinoMicroController {
    @Override
    public void setBTconnection() throws ConnectException {

    }

    @Override
    public void startDriving() throws PMVPhisicalException, ConnectException, ProceduralException {

    }

    @Override
    public void stopDriving() throws PMVPhisicalException, ConnectException, ProceduralException {

    }

    @Override
    public void undoBTconnection() {

    }
}

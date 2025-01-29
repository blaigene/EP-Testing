package services.smartfeatures;

import services.exceptions.*;

import java.net.ConnectException;

public class ArduinoMicroControllerImpl implements ArduinoMicroController {

    private boolean isBTConnected;
    private boolean isDriving;
    private boolean vehicleHasIssues;

    public ArduinoMicroControllerImpl() {
        isBTConnected = false;
        isDriving = false;
        vehicleHasIssues = false;
    }

    @Override
    public void setBTconnection() throws ConnectException {
        // Simula el emparejamiento BT con el smartphone
        if (!isBTConnected) {
            isBTConnected = true;
        } else {
            throw new ConnectException("La conexión Bluetooth ya está establecida.");
        }
    }

    @Override
    public void startDriving() throws PMVPhisicalException, ConnectException, ProceduralException {
        // Simula la acción de iniciar el desplazamiento del vehículo
        if (!isBTConnected) {
            throw new ConnectException("No se ha establecido la conexión Bluetooth.");
        }
        if (isDriving) {
            throw new ProceduralException("El vehículo ya está en movimiento.");
        }
        // Simula la comprobación de problemas físicos en el vehículo
        if (vehicleHasIssues) {
            throw new PMVPhisicalException("Problema técnico en el vehículo: No se puede iniciar el trayecto.");
        }
        isDriving = true;
    }

    @Override
    public void stopDriving() throws PMVPhisicalException, ConnectException, ProceduralException {
        // Simula la acción de detener el desplazamiento del vehículo
        if (!isBTConnected) {
            throw new ConnectException("No se ha establecido la conexión Bluetooth.");
        }
        if (!isDriving) {
            throw new ProceduralException("El vehículo no está en movimiento.");
        }
        // Simula la comprobación de problemas físicos en los frenos del vehículo
        if (vehicleHasIssues) {
            throw new PMVPhisicalException("Problema técnico en los frenos: No se puede detener el vehículo.");
        }
        isDriving = false;
    }

    @Override
    public void undoBTconnection() {
        // Simula la acción de deshacer la conexión Bluetooth
        if (isBTConnected) {
            isBTConnected = false;
        }
    }

    // Métodos para configurar el estado de los problemas en el vehículo o los frenos
    public void setVehicleHasIssues(boolean hasIssues) {
        this.vehicleHasIssues = hasIssues;
    }

    // Getters per a les variables booleans
    public boolean isBTConnected() {
        return isBTConnected;
    }

    public boolean isDriving() {
        return isDriving;
    }

    public boolean isVehicleHasIssues() {
        return vehicleHasIssues;
    }
}

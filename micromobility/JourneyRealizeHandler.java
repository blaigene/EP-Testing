package micromobility;

import data.GeographicPoint;
import data.StationID;
import micromobility.exceptions.*;
import java.net.ConnectException;
import java.time.LocalDateTime;

/**
 * Classe controladora per gestionar els esdeveniments del cas d'ús de realitzar trajectes.
 */
public class JourneyRealizeHandler {
    private PMVehicle vehicle;
    private JourneyService service;

    public JourneyRealizeHandler(PMVehicle vehicle, JourneyService service) {
        this.vehicle = vehicle;
        this.service = service;
    }

    public void scanQR() throws ConnectException, InvalidPairingArgsException,
            CorruptedImgException, PMVNotAvailException, ProceduralException {
        if (!vehicle.getState().equals(PMVState.Available)) {
            throw new PMVNotAvailException("Vehicle not available");
        }
        try {
            service.bindVehicle(vehicle.getId());  // Método ficticio, actualiza con el correcto
            vehicle.setNotAvailble();  // Cambiar estado del vehículo
            service.setServiceInit();  // Inicia el servicio
        } catch (Exception e) {
            throw new ProceduralException("Error during QR scan process", e);
        }
    }

    public void unPairVehicle() throws ConnectException, InvalidPairingArgsException,
            PairingNotFoundException, ProceduralException {
        if (!service.isInProgress()) {
            throw new PairingNotFoundException("No active pairing found");
        }
        try {
            service.setServiceFinish();  // Finaliza el servicio
            vehicle.setAvailble();  // Cambiar estado del vehículo a disponible
        } catch (Exception e) {
            throw new ProceduralException("Error during unpairing process", e);
        }
    }

    public void broadcastStationID(StationID stID) throws ConnectException {
        try {
            System.out.println("Station ID received: " + stID);
            service.updateStationID(stID); // Método ficticio, actualiza con el correcto
        } catch (Exception e) {
            throw new ConnectException("Failed to broadcast station ID");
        }
    }

    public void startDriving() throws ConnectException, ProceduralException {
        if (!service.isInProgress() || !vehicle.getState().equals(PMVState.NotAvailable)) {
            throw new ProceduralException("Vehicle is not ready to start driving");
        }
        try {
            vehicle.setUnderWay();  // Cambiar estado del vehículo a UnderWay
            service.startJourney();  // Método ficticio, actualiza con el correcto
        } catch (Exception e) {
            throw new ConnectException("Error starting the drive");
        }
    }

    public void stopDriving() throws ConnectException, ProceduralException {
        if (!service.isInProgress() || !vehicle.getState().equals(PMVState.UnderWay)) {
            throw new ProceduralException("No journey is in progress or vehicle is not in the correct state");
        }
        try {
            vehicle.setTemporaryParking();  // Cambiar estado a TemporaryParking
            service.finishJourney();  // Método ficticio, actualiza con el correcto
        } catch (Exception e) {
            throw new ConnectException("Error stopping the drive");
        }
    }

    private void calculateValues(GeographicPoint gP, LocalDateTime date) {
        // Cálculo de duración, distància i velocitat mitjana
    }

    private void calculateImport(float dis, int dur, float avSp, LocalDateTime date) {
        // Cálculo del importe del servicio
    }
}

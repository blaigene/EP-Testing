package micromobility;

import data.*;
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
        // Implementació de la lògica de decodificació del QR
    }

    public void unPairVehicle() throws ConnectException, InvalidPairingArgsException,
            PairingNotFoundException, ProceduralException {
        // Lògica per desconnectar el vehicle
    }

    public void broadcastStationID(StationID stID) throws ConnectException {
        // Lògica per rebre l'ID de l'estació
    }

    public void startDriving() throws ConnectException, ProceduralException {
        // Lògica per iniciar el desplaçament
    }

    public void stopDriving() throws ConnectException, ProceduralException {
        // Lògica per aturar el desplaçament
    }

    private void calculateValues(GeographicPoint gP, LocalDateTime date) {
        // Càlcul de durada, distància i velocitat mitjana
    }

    private void calculateImport(float dis, int dur, float avSp, LocalDateTime date) {
        // Càlcul de l'import del servei
    }

    public static class InvalidPairingArgsException extends Exception {
        public InvalidPairingArgsException(String message) {
            super(message);
        }
    }

    public static class CorruptedImgException extends Exception {
        public CorruptedImgException(String message) {
            super(message);
        }
    }

    public static class PMVNotAvailException extends Exception {
        public PMVNotAvailException(String message) {
            super(message);
        }
    }

    public static class ProceduralException extends Exception {
        public ProceduralException(String message) {
            super(message);
        }
    }

    public static class PairingNotFoundException extends Exception {
        public PairingNotFoundException(String message) {
            super(message);
        }
    }
}
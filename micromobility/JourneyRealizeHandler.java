package micromobility;

import data.GeographicPoint;
import data.StationID;
import micromobility.exceptions.*;
import services.Server;
import services.smartfeatures.QRDecoder;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Classe controladora per gestionar els esdeveniments del cas d'ús de realitzar trajectes.
 */
public class JourneyRealizeHandler {
    private PMVehicle vehicle;
    private JourneyService service;
    private QRDecoder qrDecoder;  // Suposant que QRDecoder és una classe que ja existeix
    private Server server;

    public JourneyRealizeHandler(PMVehicle vehicle, JourneyService service, QRDecoder qrDecoder, Server server) {
        this.vehicle = vehicle;
        this.service = service;
        this.qrDecoder = qrDecoder;
        this.server = server;
    }

    public void scanQR(String qrCode) throws ConnectException, InvalidPairingArgsException,
            CorruptedImgException, PMVNotAvailException, ProceduralException {

        if (!server.isConnected()) {
            throw new ConnectException("No es pot connectar al servidor");
        }
        String vehicleID = qrDecoder.decodeQR(qrCode);
        if (vehicleID == null || !vehicleID.equals(vehicle.getId())) {
            throw new InvalidPairingArgsException("L'ID del vehicle no coincideix amb el QR proporcionat");
        }

        server.checkPMVAvail(vehicle.getId());

        vehicle.setUnderWay();
        service.setServiceInit();
        server.registerPairing(service.getUserAccount(), vehicle.getId(), new StationID(), service.getOriginPoint(), LocalDateTime.now());
    }

    public void unPairVehicle() throws ConnectException, InvalidPairingArgsException,
            PairingNotFoundException, ProceduralException {
        if (!server.isConnected()) {
            throw new ConnectException("No es pot connectar al servidor");
        }
        server.unPairRegisterService(service);
        vehicle.setAvailble();
        service.setServiceFinish();
    }


    public static void broadcastStationID(StationID stID) throws ConnectException {
        // Lògica per rebre l'ID de l'estació
    }

    public void startDriving() throws ConnectException, ProceduralException {
        if (!vehicle.getState().equals(PMVState.UnderWay)) {
            throw new ProceduralException("El vehicle no està en estat per iniciar el desplaçament");
        }
        // Logica adicional si es necesario
    }



    public void stopDriving() throws ConnectException, ProceduralException {
        if (!server.isConnected()) {
            throw new ConnectException("No es pot connectar al servidor");
        }
        // Assume que el vehicle ya ha llegado a su destino
        calculateValues(service.getEndPoint(), LocalDateTime.now());
        float distance = (float) service.getDistance();
        int duration = service.getDuration();
        float avgSpeed = (float) service.getAvgSpeed();
        calculateImport(distance, duration, avgSpeed, LocalDateTime.now());
        server.stopPairing(service.getUserAccount(), vehicle.getId(), new StationID(), service.getEndPoint(), LocalDateTime.now(), avgSpeed, distance, duration, BigDecimal.valueOf(service.getImportCost()));
        vehicle.setAvailble();
        service.setServiceFinish();
    }


    private void calculateValues(GeographicPoint endPoint, LocalDateTime endDate) {
        LocalDateTime startDate = LocalDateTime.of(service.getInitDate(), service.getInitHour());
        long minutesBetween = ChronoUnit.MINUTES.between(startDate, endDate);
        double distance = calculateValues(service.getOriginPoint(), endPoint);
        service.setDistance(distance);
        service.setDuration((int) minutesBetween);
        service.setAvgSpeed(distance / (minutesBetween / 60.0));
    }

    private void calculateImport(float dis, int dur, float avSp, LocalDateTime date) {
        // Ejemplo simple basado en tarifa fija más costo por kilómetro
        double baseRate = 2.50; // Tarifa base
        double costPerKm = 1.20; // Costo por kilómetro
        double importCost = baseRate + (dis * costPerKm);
        service.setImportCost(importCost);
    }

}

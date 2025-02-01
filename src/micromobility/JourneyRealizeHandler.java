package micromobility;

import data.*;
import micromobility.exceptions.ProceduralException;
import services.*;
import services.exceptions.*;
import services.smartfeatures.*;
import micromobility.exceptions.*;
import micromobility.payment.*;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class JourneyRealizeHandler {

    private PMVehicle vehicle;
    private JourneyService service;
    private QRDecoder qrDecoder;
    private Server server;
    private ArduinoMicroController amc;

    public JourneyRealizeHandler(PMVehicle vehicle, JourneyService service) {
        this.vehicle = vehicle;
        this.service = service;
    }

    public void scanQR() throws ConnectException, InvalidPairingArgsException,
            CorruptedImgException, PMVNotAvailException, ProceduralException {
        VehicleID vehicleID = qrDecoder.getVehicleID(vehicle.getQrCode());
        System.out.println("Ok escaneo QR.");
        server.checkPMVAvail(vehicleID);
        amc.setBTconnection();
        vehicle.setNotAvailable();
        server.registerPairing(vehicle.getUser(), vehicle.getVehicleID(), vehicle.getStationID(),
                vehicle.getLocation(), service.getInitDateTime());
        System.out.println("Ok emparejamiento, se puede iniciar trayecto.");
    }

    public void unPairVehicle() throws ConnectException, ProceduralException {
        calculateValues(vehicle.getLocation(), service.getInitDateTime());
        calculateImport(service.getDistance(), service.getDuration(), service.getAvgSpeed(), service.getInitDateTime());
        server.stopPairing(vehicle.getUser(), vehicle.getVehicleID(), vehicle.getStationID(), vehicle.getLocation(),
                service.getInitDateTime(), service.getAvgSpeed(), service.getDistance(), service.getDuration(),
                new BigDecimal(service.getImportCost()));
        vehicle.setAvailable();
        System.out.println("Ok desemparejamiento, importe, selecciona método pago.");
    }


    public void broadcastStationID(StationID stationID) throws ConnectException {
        try {
            System.out.println("Estación identificada.");
        } catch (Exception e) {
            throw new ConnectException("No se pudo encontrar la estación.");
        }
    }


    public void startDriving() throws ConnectException, ProceduralException {
        if (!vehicle.getState().equals(PMVState.NotAvailable)) {
            throw new ProceduralException("El vehiculo tiene un estado incorrecto.");
        }

        vehicle.setUnderWay();
        service.setServiceInit();
        System.out.println("Pantalla de trayecto en curso.");
    }


    public void stopDriving() throws ConnectException, ProceduralException {
        if (!service.isInProgress()) {
            throw new ProceduralException("El viaje no está en progreso.");
        }

        if (!vehicle.getState().equals(PMVState.UnderWay)) {
            throw new ProceduralException("El vehiculo no está siendo utilizado");
        }

        try {
            System.out.println("Pantalla de vehículo detenido.");
        } catch (Exception e) {
            throw new ConnectException("No se pudo conectar con el vehículo.");
        }
    }

    public void selectPaymentMethod(char opt) throws ProceduralException, NotEnoughWalletException, ConnectException {
        if (opt == 'W') {
            BigDecimal importCost = new BigDecimal(service.getImportCost());
            try {
                realizePayment(importCost);
            } catch (Exception e) {
                throw new ConnectException("Error de conexión.");
            }
        } else {
            throw new ProceduralException("Método de pago no soportado.");
        }
        System.out.println("Pantalla credenciales.");
    }

    // Operacions internes

    public void calculateValues(GeographicPoint gP, LocalDateTime date) {
        service.setEndPoint(gP);
        service.setEndDateTime(date);
        service.setDuration(60);
        service.setDistance(calculateDistance());
        service.setAvgSpeed();
    }

    private float calculateDistance() {
        GeographicPoint start = service.getOriginPoint();
        GeographicPoint end  = service.getEndPoint();

        return (float) start.haversineDistance(end);
    }

    public void calculateImport(float dis, int dur, float avSp, LocalDateTime date) {
        float baseFare = 1.00f;
        float farePerMinute = 0.20f;
        float farePerKm = 0.50f;
        float minimumFare = 2.00f;

        // Extra per horari nocturn
        float nightSurchargeRate = 1.20f;
        boolean isNightTime = isNightTime(date);

        // Cálcul del import
        float timeFare = dur * farePerMinute;
        float distanceFare = dis * farePerKm;
        float totalFare = baseFare + timeFare + distanceFare;

        // Extra per horari nocturn
        if (isNightTime) {
            totalFare *= nightSurchargeRate;
        }

        if (totalFare < minimumFare) {
            totalFare = minimumFare;
        }

        service.setImportCost((long) totalFare);
    }

    private boolean isNightTime(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        LocalTime nightStart = LocalTime.of(22, 0);
        LocalTime nightEnd = LocalTime.of(6, 0);

        // Comprovar si és de nit
        return time.isAfter(nightStart) || time.isBefore(nightEnd);
    }

    private void realizePayment(BigDecimal imp) throws NotEnoughWalletException {
        UserAccount user = vehicle.getUser();
        ServiceID id = service.getServiceID();
        WalletPayment payment = new WalletPayment(id, user, imp, user.getWallet());
        payment.processPayment();
    }

    // Setters i Getters
    public void setServer(Server server){
        this.server = server;
    }

    public void setDecoder(QRDecoder qrDecoder){
        this.qrDecoder = qrDecoder;
    }

    public void setAmc(ArduinoMicroController amc){
        this.amc = amc;
    }

    public void setVehicle(PMVehicle vehicle) {
        this.vehicle = vehicle;
    }

    public PMVehicle getVehicle(){
        return this.vehicle;
    }

    public JourneyService getService(){
        return this.service;
    }

    public PMVState getVehicleState() {
        return vehicle.getState();
    }
}
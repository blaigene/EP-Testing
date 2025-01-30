package micromobility;

import data.*;
import micromobility.exceptions.*;
import micromobility.payment.Wallet;
import micromobility.payment.WalletPayment;
import services.Server;
import services.smartfeatures.*;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class JourneyRealizeHandler {

    private PMVehicle vehicle;
    private JourneyService service;
    private QRDecoder qrDecoder;
    private Server server;
    private UnbondedBTSignal btSignal;
    private ArduinoMicroController amc;

    public JourneyRealizeHandler() {
        /**this.vehicle = new PMVehicle();
        this.service = new JourneyService();
        this.qrDecoder = new QRDecoderImpl();
        this.server = new ServerImpl();
        this.btSignal = new UnbondedBTSignalImpl();
        this.amc = new ArduinoMicroControllerImpl();**/
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
        service.setDuration();
        service.setDistance(calculateDistance());
        service.setAvgSpeed();
    }

    private float calculateDistance() {
        GeographicPoint start = service.getOriginPoint();
        GeographicPoint end  = service.getEndPoint();

        return (float) start.haversineDistance(end);
    }

    public void calculateImport(float dis, int dur, float avSp, LocalDateTime date) {
        //Tarifes
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

        // Asegurarse de que el importe sea al menos la tarifa mínima
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
        UserAccount user = service.getUserAccount();
        ServiceID id = service.getServiceID();
        WalletPayment payment = new WalletPayment(id, user, imp, user.getWallet());
        payment.processPayment();
    }
}
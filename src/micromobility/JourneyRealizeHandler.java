package micromobility;

import data.GeographicPoint;
import data.StationID;
import micromobility.exceptions.*;
import micromobility.payment.Wallet;
import micromobility.payment.WalletPayment;
import services.Server;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.UnbondedBTSignal;


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
    private QRDecoder qrDecoder;
    private static Server server;
    private UnbondedBTSignal btSignal;
    private StationID stationID;

    public JourneyRealizeHandler(Server serverMock, PMVehicle vehicle, JourneyService service) {
        this.vehicle = vehicle;
        this.service = service;
        this.qrDecoder = qrDecoder;
        this.server = server;
        this.btSignal = btSignal;
        this.stationID = stationID;
    }

    /**
     * Escanea un código QR para iniciar un proceso de emparejamiento de vehículo.
     *
     * @param qrCode el código QR a escanear
     * @throws ConnectException si no se puede conectar al servidor
     * @throws InvalidPairingArgsException si el ID del vehículo obtenido no coincide
     * @throws CorruptedImgException si el código QR está corrupto
     * @throws PMVNotAvailException si el vehículo no está disponible
     * @throws ProceduralException si ocurre un error de procedimiento no especificado
     */

    public void scanQR(String qrCode) throws ConnectException, InvalidPairingArgsException,
            CorruptedImgException, PMVNotAvailException {
        /**if (!server.isConnected()) {
            throw new ConnectException("No se puede conectar al servidor.");
        }

        String vehicleID;
        try {
            vehicleID = qrDecoder.decodeQR(qrCode);
        } catch (Exception e) {
            throw new CorruptedImgException("Error al decodificar el código QR.", e);
        }

        if (vehicleID == null || !vehicleID.equals(vehicle.getId())) {
            throw new InvalidPairingArgsException("El ID del vehículo obtenido del QR no coincide con el ID del vehículo registrado.");
        }

        boolean isAvailable = server.checkPMVAvail(vehicle.getVehicleID());  // Supongamos que retorna un booleano
        if (!isAvailable) {
            throw new PMVNotAvailException("El vehículo con ID " + vehicle.getVehicleID() + " no está disponible.");
        }

        vehicle.setNotAvailble();  // Marca el vehículo como no disponible
        server.registerPairing(service.getUserAccount(), vehicle.getVehicleID(), new StationID("ST123456"), service.getOriginPoint(), LocalDateTime.now());*/
    }

    public void unPairVehicle() throws ConnectException, ProceduralException {
        /**if (!server.isConnected()) {
            throw new ConnectException("No se puede conectar al servidor.");
        }

        try {
            // Marcar el servicio como finalizado y el vehículo como disponible
            vehicle.setAvailble();
            service.setServiceFinish();

            // Registrar la finalización del emparejamiento y del servicio en el servidor
            server.stopPairing(service.getUserAccount(), vehicle.getVehicleID(), service.getStationID(stationID),
                    service.getEndPoint(), service.getEndDate(), service.getAvgSpeed(),
                    service.getDistance(), service.getDuration(), BigDecimal.valueOf(service.getImportCost()));
        } catch (Exception e) {
            throw new ProceduralException("Error durante la finalización del servicio: " + e.getMessage());
        }*/
    }

    /**
     * Simula la recepción del ID de la estación por Bluetooth y maneja la conexión.
     *
     * @param stationID El ID de la estación que se recibe.
     * @throws ConnectException Si hay un fallo en la conexión que impide la recepción del ID.
     */
    public static void broadcastStationID(StationID stationID) throws ConnectException {
        /**if (!server.isConnected()) {
            throw new ConnectException("No se puede conectar al servidor para recibir el ID de la estación.");
        }

        // Emula la recepción del ID de la estación a través del canal Bluetooth
        server.broadcastStationID(stationID);
        System.out.println("ID de estación recibido correctamente: " + stationID.getId()); */
    }

    /**
     * Inicia el desplazamiento del usuario, actualizando el estado y registrando el inicio.
     *
     * @throws ConnectException si hay un problema de conexión con el servidor.
     * @throws ProceduralException si ocurre un error durante la configuración del desplazamiento.
     */
    public void startDriving() throws ConnectException, ProceduralException {
        /**if (!server.isConnected()) {
            throw new ConnectException("No se puede conectar al servidor para iniciar el desplazamiento.");
        }

        try {
            vehicle.setUnderWay();  // Marca el vehículo como en movimiento
            service.setServiceInit();  // Registra el inicio del servicio en el sistema
            server.startJourney(vehicle.getVehicleID(), service.getServiceID());  // Envía datos al servidor
            System.out.println("Desplazamiento iniciado correctamente.");
        } catch (Exception e) {
            throw new ProceduralException("Error al iniciar el desplazamiento: " + e.getMessage(), e);
        }*/
    }

    /**
     * Detiene el desplazamiento del usuario, actualizando el estado y registrando la finalización.
     *
     * @throws ConnectException si hay un problema de conexión con el servidor.
     * @throws ProceduralException si ocurre un error durante la finalización del desplazamiento.
     */
    public void stopDriving() throws ConnectException, ProceduralException {
        /**if (!server.isConnected()) {
            throw new ConnectException("No se puede conectar al servidor para detener el desplazamiento.");
        }

        try {
            vehicle.setAvailble();  // Marca el vehículo como disponible
            service.setServiceFinish();  // Registra la finalización del servicio en el sistema
            server.endJourney(vehicle.getVehicleID(), service.getServiceID(), service.getEndPoint(), service.getEndDate(), (float) service.getAvgSpeed(), (float) service.getDistance(), service.getDuration(), BigDecimal.valueOf(service.getImportCost()));  // Envía datos al servidor
            System.out.println("Desplazamiento detenido correctamente.");
        } catch (Exception e) {
            throw new ProceduralException("Error al detener el desplazamiento: " + e.getMessage(), e);
        }*/
    }

    /**
     * Calcula los valores relativos al trayecto: duración, distancia y velocidad promedio.
     *
     * @param gP El punto geográfico final del trayecto.
     * @param date La fecha y hora de finalización del trayecto.
     */
    public void calculateValues(GeographicPoint gP, LocalDateTime date) {
        // Calcular la duración en minutos entre la fecha de inicio y la fecha final
        long duration = ChronoUnit.MINUTES.between(service.getInitDate(), date);

        // Calcular la distancia entre el punto de inicio y el punto final
        double distance = service.getDistance();

        // Calcular la velocidad promedio (km/h)
        double avgSpeed = (distance / (duration / 60.0));

        // Actualizar el servicio con los valores calculados
        service.setDuration((int) duration);
        service.setDistance(distance);
        service.setAvgSpeed(avgSpeed);
    }

    public void calculateImport(float dis, int dur, float avSp, LocalDateTime date) {
        double baseRate = 2.0; // Tarifa base, por ejemplo 2 euros
        double ratePerKm = 0.50; // Tarifa por kilómetro, por ejemplo 0.50 euros
        double ratePerMinute = 0.10; // Tarifa por minuto, por ejemplo 0.10 euros

        // Calcular costos adicionales basados en distancia y duración
        double distanceCost = dis * ratePerKm;
        double timeCost = dur * ratePerMinute;

        // Considerar recargos o descuentos por velocidad o hora del día
        double speedDiscount = avSp > 50 ? -1.0 : 0; // Descuento de 1 euro si la velocidad promedio supera 50 km/h

        // Calcular el importe total
        double totalCost = baseRate + distanceCost + timeCost + speedDiscount;

        // Actualizar el servicio con el importe calculado
        service.setImportCost(totalCost);
    }

    public void selectPaymentMethod(char opt) throws ProceduralException, NotEnoughWalletException, ConnectException {
        if (opt == 'w') {
            BigDecimal journeyCost = new BigDecimal(this.service.getImportCost());

            try {
                this.realizePayment(journeyCost);
            } catch (NotEnoughWalletException var4) {
                throw new NotEnoughWalletException("Saldo insuficient per realitzar el pagament.");
            }
        } else {
            throw new ProceduralException("Método de pago no soportado.");
        }
    }

    private void realizePayment(BigDecimal imp) {
        Wallet fakeWallet = new Wallet(new BigDecimal(200));
        WalletPayment payment = new WalletPayment( 'W',this.service.getUserAccount(), imp, fakeWallet);
        payment.processPayment();
    }
}
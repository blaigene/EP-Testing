package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.VehicleID;
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
    private boolean isConnected;

    public JourneyRealizeHandler(Server server, PMVehicle vehicle, JourneyService service, QRDecoder qrDecoder, UnbondedBTSignal btSignal, StationID stationID) {
        this.vehicle = vehicle;
        this.service = service;
        this.qrDecoder = qrDecoder;
        this.server = server;
        this.btSignal = btSignal;
        this.stationID = stationID;
    }

    public void scanQR(String qrCode) throws InvalidPairingArgsException, CorruptedImgException, PMVNotAvailException, ConnectException {
        String vehicleID;
        try {
            vehicleID = qrDecoder.decodeQR(qrCode);
        } catch (Exception  e) {
            throw new CorruptedImgException("Error al decodificar el código QR.", e);
        }

        if (vehicleID == null || !vehicleID.equals(vehicle.getId())) {
            throw new InvalidPairingArgsException("El ID del vehículo obtenido del QR no coincide con el ID del vehículo registrado.");
        }

        VehicleID id = new VehicleID(vehicleID);  // Suponiendo que VehicleID es una clase que encapsula el ID del vehículo.
        boolean isAvailable = false;  // Asumiendo que el método espera una instancia de VehicleID
        try {
            server.checkPMVAvail(id);
        } catch (services.exceptions.PMVNotAvailException e) {
            throw new RuntimeException(e);
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        }
        if (!isAvailable) {
            throw new PMVNotAvailException("El vehículo con ID " + vehicleID + " no está disponible.");
        }

        vehicle.setNotAvailble();  // Marca el vehículo como no disponible
    }

    public void unPairVehicle() throws ConnectException, ProceduralException, InvalidPairingArgsException, PairingNotFoundException {
        if (!service.isInProgress() || !vehicle.getState().equals(PMVState.UnderWay)) {
            throw new ProceduralException("Precondiciones no cumplidas para desvincular el vehículo.");
        }

        // Propaga estas excepciones específicas si ocurrieron
        try {
            // Establecer la hora y fecha de finalización del viaje
            LocalDateTime now = LocalDateTime.now();
            service.setEndDate();
            service.setEndHour();

            // Calcular y actualizar los valores de distancia, duración y velocidad promedio
            calculateValues(service.getEndPoint(), now);
            calculateImport(service.getDistance(), service.getDuration(), service.getAvgSpeed(), now);

            // Actualizar el estado del vehículo y concluir el servicio en el servidor
            vehicle.setAvailble();  // Asegúrate de que el método se llame correctamente
            vehicle.setLocation(service.getEndPoint());

            // Registrar la finalización del emparejamiento y del servicio en el servidor
            server.stopPairing(service.getUserAccount(), vehicle.getVehicleID(), vehicle.getStationID(),
                    service.getEndPoint(), LocalDateTime.from(service.getEndDate()), service.getAvgSpeed(),
                    service.getDistance(), service.getDuration(), BigDecimal.valueOf(service.getImportCost()));

            // Desvincular el vehículo en el sistema y en el servidor
            server.unPairRegisterService(service);

        } catch (ConnectException e) {
            throw new ProceduralException("Error durante la finalización del servicio: " + e.getMessage(), e);
        } catch (services.exceptions.InvalidPairingArgsException e) {
            throw new ProceduralException("Error durante la finalización del servicio: " + e.getMessage(), e);
        } catch (services.exceptions.PairingNotFoundException e) {
            throw new ProceduralException("Error durante la finalización del servicio: " + e.getMessage(), e);
        } finally {
            service.setServiceFinish(); // Asegura que el estado se marca como no en progreso
        }
    }


    // Método que gestiona la recepción del ID de estación vía Bluetooth
    public static void broadcastStationID(StationID stID) throws ConnectException {
        if (stID == null) {
            throw new ConnectException("Error: No se recibió ningún ID de estación.");
        }
        System.out.println("ID de estación recibido correctamente: " + stID.getId());
    }

    /**
     * Inicia el desplazamiento del usuario, actualizando el estado y registrando el inicio.
     *
     * @throws ConnectException si hay un problema de conexión con el servidor.
     * @throws ProceduralException si ocurre un error durante la configuración del desplazamiento.
     */
    public void startDriving() throws ConnectException, ProceduralException {

        isConnected = true; //se conecta correctamente

        // Verificar que el vehículo está correctamente vinculado y preparado
        if (!vehicle.getState().equals(PMVState.NotAvailable) || service == null) {
            throw new ProceduralException("Precondiciones no cumplidas para iniciar el desplazamiento.");
        }

        try {
            vehicle.setUnderWay();  // Cambia el estado del vehículo a 'En movimiento'
            service.setServiceInit();  // Indica que el servicio está en progreso

            System.out.println("Desplazamiento iniciado correctamente.");
        } catch (Exception e) {
            throw new ProceduralException("Error al iniciar el desplazamiento: " + e.getMessage(), e);
        }
    }

    /**
     * Detiene el desplazamiento del usuario, actualizando el estado y registrando la finalización.
     *
     * @throws ConnectException si hay un problema de conexión con el servidor.
     * @throws ProceduralException si ocurre un error durante la finalización del desplazamiento.
     */
    public void stopDriving() throws ConnectException, ProceduralException {
        // Verificación de la conexión servidor
        if (!isConnected) {
            throw new ConnectException("No es pot connectar al servidor");
        }

        // Asegurar que el vehículo está en movimiento y que el servicio está activo
        if (!vehicle.getState().equals(PMVState.UnderWay) || !service.isInProgress()) {
            throw new ProceduralException("El vehículo no está en movimiento o el servicio no está activo.");
        }

        try {
            // Solo marca el servicio como terminado, no cambia el estado del vehículo aún
            service.setServiceFinish();  // Registra la finalización del servicio en el sistema

            System.out.println("Desplazamiento detenido correctamente.");
        } catch (Exception e) {
            throw new ProceduralException("Error al detener el desplazamiento: " + e.getMessage(), e);
        }
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

    public void calculateImport(double dis, int dur, double avSp, LocalDateTime date) {
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
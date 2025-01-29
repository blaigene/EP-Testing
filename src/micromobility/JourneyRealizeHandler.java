package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.VehicleID;
import micromobility.exceptions.*;
import micromobility.payment.Wallet;
import micromobility.payment.WalletPayment;
import services.Server;
import services.ServerImpl;
import services.smartfeatures.*;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;


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
        server.checkPMVAvail(vehicleID);
        amc.setBTconnection();
        vehicle.setNotAvailable();
        server.registerPairing(vehicle.getUser(), vehicle.getVehicleID(), vehicle.getStationID(),
                vehicle.getLocation(), service.getInitDateTime());
        System.out.println("Ok emparejamiento, se puede iniciar trayecto.");
    }

    public void unPairVehicle() throws ConnectException, ProceduralException {

    }


    public static void broadcastStationID(StationID stationID) throws ConnectException {

    }


    public void startDriving() throws ConnectException, ProceduralException {

    }


    public void stopDriving() throws ConnectException, ProceduralException {

    }


    public void calculateValues(GeographicPoint gP, LocalDateTime date) {

    }

    public void calculateImport(float dis, int dur, float avSp, LocalDateTime date) {

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
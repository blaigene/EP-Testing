package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.VehicleID;
import micromobility.exceptions.*;
import micromobility.payment.Wallet;
import micromobility.payment.WalletPayment;
import services.Server;
import services.smartfeatures.*;


import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class JourneyRealizeHandler {

    private PMVehicle vehicle;
    private JourneyService service;
    private QRDecoder qrDecoder;
    private static Server server;
    private UnbondedBTSignal btSignal;
    private ArduinoMicroController amc;
    private StationID stationID;
    private VehicleID vehicleID;

    public JourneyRealizeHandler() {
        this.vehicle = new PMVehicle();
        this.service = service;
        this.qrDecoder = new QRDecoderImpl();
        this.server = server;
        this.btSignal = new UnbondedBTSignalImpl();
        this.stationID = stationID;
    }



    public void scanQR() throws ConnectException, InvalidPairingArgsException,
            CorruptedImgException, PMVNotAvailException {
        vehicleID = qrDecoder.getVehicleID(new BufferedImage());
        server.checkPMVAvail(vehicleID);
        amc.setBTconnection();
        vehicle.setNotAvailble();
        server.registerPairing();
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
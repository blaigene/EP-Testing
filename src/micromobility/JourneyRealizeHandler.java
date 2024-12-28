package micromobility;

import data.GeographicPoint;
import data.StationID;
import micromobility.exceptions.*;
import micromobility.payment.Wallet;
import micromobility.payment.WalletPayment;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;

/**
 * Classe controladora per gestionar els esdeveniments del cas d'ús de realitzar trajectes.
 */
public class JourneyRealizeHandler {
    // DSS  el sistema és JOURNEYREALIZEHANDLER
    // 3 CANALS D'ENTRADA
    // CAL PENDRE ALGUNA DECISIÓ, NO ENS HO CHIVA
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

    public static void broadcastStationID(StationID stID) throws ConnectException {
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

    public void selectPaymentMethod(char opt) throws ProceduralException, NotEnoughWalletException, ConnectException {
        if (opt == 'W') { // Wallet
            BigDecimal journeyCost = new BigDecimal(service.getImportCost());
            try {
                realizePayment(journeyCost);
            } catch (NotEnoughWalletException e) {
                throw new NotEnoughWalletException("Saldo insuficient per realitzar el pagament.");
            }
        } else {
            throw new ProceduralException("Método de pago no soportado.");
        }
    }

    private void realizePayment (BigDecimal imp) {
        Wallet fakeWallet = new Wallet(new BigDecimal(200));
        WalletPayment payment = new WalletPayment('W', service.getUserAccount(), imp, fakeWallet);
        payment.processPayment();
    }
}
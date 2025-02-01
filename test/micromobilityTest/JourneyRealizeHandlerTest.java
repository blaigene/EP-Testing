package micromobilityTest;

import data.*;
import micromobility.*;
import micromobility.exceptions.*;
import micromobility.exceptions.ProceduralException;
import micromobility.payment.Wallet;
import org.junit.jupiter.api.*;
import services.*;
import services.exceptions.*;
import services.smartfeatures.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.awt.image.BufferedImage;

public class JourneyRealizeHandlerTest {

    private JourneyRealizeHandler handler;
    private Server server;
    private ArduinoMicroController amc;
    private QRDecoder qrDecoder;
    private PMVehicle vehicle;
    private JourneyService service;

    @BeforeAll
    static void initAll() {
        System.out.println("INICIALIZANDO JOURNEYREALIZEHANDLER TESTS");
    }

    @BeforeEach
    void setUp() {
        service = new JourneyService(new ServiceID("SE000000"));
        service.setOriginPoint(new GeographicPoint(45.3851f, 3.1734f));
        service.setVehicleID(new VehicleID("VH000000"));
        vehicle = new PMVehicle(new VehicleID("VH000000"), new GeographicPoint(41.3851f, 2.1734f), "Data",
                80, new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB),
                new UserAccount("OriolVines12345"), new StationID("ST000000"));
        handler = new JourneyRealizeHandler(vehicle, service);
        server = new ServerImpl();
        amc = new ArduinoMicroControllerImpl();
        qrDecoder = new QRDecoderImpl();

        handler.setServer(server);
        handler.setAmc(amc);
        handler.setDecoder(qrDecoder);
    }

    private void prepareStartDrivingUseCase() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());
    }

    private void prepareStopDrivingUseCase() {
        prepareStartDrivingUseCase();
        Assertions.assertDoesNotThrow(() -> handler.startDriving());
        Assertions.assertEquals(PMVState.UnderWay, handler.getVehicleState());
        Assertions.assertTrue(service.isInProgress());
    }

    private void prepareUnpairVehicleUseCase() {
        prepareStopDrivingUseCase();
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.stopDriving());

    }

    private void prepareSelectPaymentMethodUseCase() {
        prepareUnpairVehicleUseCase();
        Assertions.assertDoesNotThrow(() -> handler.unPairVehicle());
        Assertions.assertEquals(PMVState.Available, handler.getVehicleState());

    }

    @Test
    void testSuccessfulScanQR() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());
    }

    @Test
    void testScanQRThrowsConnectException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        amc.undoBTconnection(); // Injectem valor erroni.
        Assertions.assertThrows(ConnectException.class, () -> handler.scanQR());
        Assertions.assertNotEquals(PMVState.NotAvailable, handler.getVehicleState());
    }

    @Test
    void testScanQRThrowsInvalidPairingArgsException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        vehicle.setLocation(new GeographicPoint(2000, 2000));   // Injectem valor erroni.
        Assertions.assertThrows(InvalidPairingArgsException.class, () -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());
    }

    @Test
    void testScanQRThrowsCorruptedImgException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        qrDecoder.setCorrupted(true); // Injectem valor erroni.
        handler.setDecoder(qrDecoder);
        Assertions.assertThrows(CorruptedImgException.class, () -> handler.scanQR());
    }

    @Test
    void testScanQRThrowsPMVNotAvailException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        server.setPairing(new UserAccount("LionelMessi1234"), new VehicleID("VH000000"),
                new StationID("ST000000"), new GeographicPoint(1,1)); // Injectem valor erroni.
        Assertions.assertThrows(PMVNotAvailException.class, () -> handler.scanQR());
        Assertions.assertNotEquals(PMVState.NotAvailable, handler.getVehicleState());
    }

    @Test
    void testScanQRThrowsProceduralException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> vehicle.setNotAvailable()); // Injectem valor erroni.
        Assertions.assertThrows(micromobility.exceptions.ProceduralException.class, () -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());
    }

    @Test
    void testSuccessfulStartDriving() {
        prepareStartDrivingUseCase();
        Assertions.assertDoesNotThrow(() -> handler.startDriving());
        Assertions.assertEquals(PMVState.UnderWay, handler.getVehicleState());
        Assertions.assertTrue(service.isInProgress());
    }

    @Test
    void testStartDrivingThrowsConnectException() {
        prepareStartDrivingUseCase();
        Assertions.assertDoesNotThrow(() -> service.setServiceInit()); // Injectem valor erroni.
        Assertions.assertThrows(ConnectException.class, () -> handler.startDriving());
        Assertions.assertNotEquals(PMVState.UnderWay, handler.getVehicleState());
        Assertions.assertTrue(service.isInProgress());
    }

    @Test
    void testStartDrivingThrowsProceduralException() {
        prepareStartDrivingUseCase();
        Assertions.assertDoesNotThrow(() -> vehicle.setTemporaryParking()); // Injectem valor erroni.
        Assertions.assertThrows(ProceduralException.class, () -> handler.startDriving());
        Assertions.assertNotEquals(PMVState.UnderWay, handler.getVehicleState());
        Assertions.assertFalse(service.isInProgress());
    }

    @Test
    void testSuccessfulStopDriving() {
        prepareStopDrivingUseCase();
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.stopDriving());
    }

    @Test
    void testStopDrivingThrowsConnectException() {
        prepareStopDrivingUseCase();
        Assertions.assertDoesNotThrow(() -> service.setServiceFinish()); //Injectem valor erroni.
        Assertions.assertThrows(ConnectException.class, () -> handler.stopDriving());
    }

    @Test
    void testStopDrivingThrowsProceduralException() {
        prepareStopDrivingUseCase();
        Assertions.assertDoesNotThrow(() -> vehicle.setTemporaryParking()); //Injectem valor erroni.
        Assertions.assertThrows(ProceduralException.class, () -> handler.stopDriving());
    }

    @Test
    void testSuccessfulUnpairVehicle() {
        prepareUnpairVehicleUseCase();
        Assertions.assertDoesNotThrow(() -> handler.unPairVehicle());
        Assertions.assertEquals(PMVState.Available, handler.getVehicleState());
    }

    @Test
    void testUnpairVehicleThrowsConnectException() {
        prepareUnpairVehicleUseCase();
        vehicle.setVehicleID(new VehicleID("VH654321")); // Injectem valor erroni.
        Assertions.assertThrows(ConnectException.class, () -> handler.unPairVehicle());
        Assertions.assertNotEquals(PMVState.Available, handler.getVehicleState());
    }

    @Test
    void testUnpairVehicleThrowsProceduralException() {
        prepareUnpairVehicleUseCase();
        Assertions.assertDoesNotThrow(() -> vehicle.setTemporaryParking()); //Injectem valor erroni.
        Assertions.assertThrows(ProceduralException.class, () -> handler.unPairVehicle());
        Assertions.assertNotEquals(PMVState.Available, handler.getVehicleState());
    }

    @Test
    void testSuccessfulSelectPaymentMethod() {
        prepareSelectPaymentMethodUseCase();
        Assertions.assertDoesNotThrow(() -> handler.selectPaymentMethod('W'));
    }

    @Test
    void testSelectPaymentMethodThrowsConnectException() {
        prepareSelectPaymentMethodUseCase();
        service.setVehicleID(new VehicleID("VH654321"));    //Injectem valor erroni.
        Assertions.assertThrows(ConnectException.class, () -> handler.selectPaymentMethod('W'));
    }

    @Test
    void testSelectPaymentMethodThrowsProceduralException() {
        prepareSelectPaymentMethodUseCase();
        Assertions.assertThrows(ProceduralException.class, () -> handler.selectPaymentMethod('F')); //Injectem valor erroni
    }

    @Test
    void testSelectPaymentMethodThrowsNotEnoughWalletException() {
        prepareSelectPaymentMethodUseCase();
        UserAccount poorUser = vehicle.getUser();
        Wallet emptyWallet = poorUser.getWallet();
        emptyWallet.setBalance(new BigDecimal(0)); //Injectem valor erroni.
        Assertions.assertThrows(NotEnoughWalletException.class, () -> handler.selectPaymentMethod('W'));
    }

    @AfterEach
    void tearDown() {
        handler = null;
        server = null;
        qrDecoder = null;
        amc = null;
        service = null;
        vehicle = null;
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("FINALIZANDO JOURNEYREALIZEHANDLER TESTS");
        System.out.println();
    }
}

package micromobilityTest;

import data.*;
import micromobility.*;
import micromobility.exceptions.*;
import org.junit.jupiter.api.*;
import services.*;
import services.exceptions.*;
import services.exceptions.PMVPhisicalException;
import services.smartfeatures.*;

import java.net.ConnectException;

import java.awt.image.BufferedImage;

public class JourneyRealizeHandlerTest {

    private JourneyRealizeHandler handler;
    private Server server;
    private ArduinoMicroController amc;
    private QRDecoder qrDecoder;
    private PMVehicle vehicle;
    private JourneyService service;

    @BeforeEach
    void setUp() {
        service = new JourneyService(new ServiceID("SE000000"));
        service.setOriginPoint(new GeographicPoint(45.3851f, 3.1734f));
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

    @Test
    void testSuccessfulScanQR() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());
    }

    @Test
    void testScanQRThrowsConnectException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        amc.undoBTconnection();
        Assertions.assertThrows(ConnectException.class, () -> handler.scanQR());
        Assertions.assertNotEquals(PMVState.NotAvailable, handler.getVehicleState());
    }

    @Test
    void testScanQRThrowsInvalidPairingArgsException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        vehicle.setLocation(new GeographicPoint(2000, 2000));
        Assertions.assertThrows(InvalidPairingArgsException.class, () -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());
    }

    @Test
    void testScanQRThrowsCorruptedImgException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        qrDecoder.setCorrupted(true);
        handler.setDecoder(qrDecoder);
        Assertions.assertThrows(CorruptedImgException.class, () -> handler.scanQR());
    }

    @Test
    void testScanQRThrowsPMVNotAvailException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        server.setPairing(new UserAccount("LionelMessi1234"), new VehicleID("VH000000"),
                new StationID("ST000000"), new GeographicPoint(1,1));
        Assertions.assertThrows(PMVNotAvailException.class, () -> handler.scanQR());
        Assertions.assertNotEquals(PMVState.NotAvailable, handler.getVehicleState());
    }

    @Test
    void testScanQRThrowsProceduralException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));

    }

    @Test
    void testSuccessfulStartDriving() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());
        Assertions.assertDoesNotThrow(() -> handler.startDriving());
        Assertions.assertEquals(PMVState.UnderWay, handler.getVehicleState());
        Assertions.assertTrue(service.isInProgress());
    }

    @Test
    void testStartDrivingThrowsConnectException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());

    }

    @Test
    void testStartDrivingThrowsProceduralException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());

    }

    @Test
    void testSuccessfulStopDriving() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());
        Assertions.assertDoesNotThrow(() -> handler.startDriving());
        Assertions.assertEquals(PMVState.UnderWay, handler.getVehicleState());
        Assertions.assertTrue(service.isInProgress());
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.stopDriving());
    }

    @Test
    void testStopDrivingThrowsConnectException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());

    }

    @Test
    void testStopDrivingThrowsProceduralException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());

    }

    @Test
    void testSuccessfulUnpairVehicle() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());
        Assertions.assertDoesNotThrow(() -> handler.startDriving());
        Assertions.assertEquals(PMVState.UnderWay, handler.getVehicleState());
        Assertions.assertTrue(service.isInProgress());
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.stopDriving());
        Assertions.assertDoesNotThrow(() -> handler.unPairVehicle());
        Assertions.assertEquals(PMVState.Available, handler.getVehicleState());
    }

    @Test
    void testUnpairVehicleThrowsConnectException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());

    }

    @Test
    void testUnpairVehicleThrowsProceduralException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());

    }

    @Test
    void testSuccessfulSelectPaymentMethod() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());
        Assertions.assertDoesNotThrow(() -> handler.startDriving());
        Assertions.assertEquals(PMVState.UnderWay, handler.getVehicleState());
        Assertions.assertTrue(service.isInProgress());
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.stopDriving());
        Assertions.assertDoesNotThrow(() -> handler.unPairVehicle());
        Assertions.assertEquals(PMVState.Available, handler.getVehicleState());
        Assertions.assertDoesNotThrow(() -> handler.selectPaymentMethod('W'));
    }

    @Test
    void testSelectPaymentMethodThrowsConnectException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());

    }

    @Test
    void testSelectPaymentMethodThrowsProceduralException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());

    }

    @Test
    void testSelectPaymentMethodThrowsNotEnoughWalletException() {
        Assertions.assertDoesNotThrow(() -> handler.broadcastStationID(vehicle.getStationID()));
        Assertions.assertDoesNotThrow(() -> handler.scanQR());
        Assertions.assertEquals(PMVState.NotAvailable, handler.getVehicleState());

    }
}

package test.servicesTest;

import data.StationID;
import micromobility.JourneyRealizeHandler;
import org.junit.jupiter.api.*;
import services.smartfeatures.UnbondedBTSignalImpl;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UnbondedBTSignalTest {

    private UnbondedBTSignalImpl unbondedBTSignal;
    private JourneyRealizeHandler journeyRealizeHandlerMock;

    @BeforeAll
    static void setupAll() {
        System.out.println("Iniciant proves de UnbondedBTSignalImpl...");
    }

    @BeforeEach
    void setup() {
        unbondedBTSignal = new UnbondedBTSignalImpl();
        journeyRealizeHandlerMock = mock(JourneyRealizeHandler.class);
    }

    @Test
    void testBTbroadcastSuccessfulTransmission() throws ConnectException, InterruptedException {
        StationID fakeStationID = new StationID("ST123456");

        doNothing().when(journeyRealizeHandlerMock).broadcastStationID(fakeStationID);

        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    void testBTbroadcastWithBluetoothFailure() throws ConnectException {
        StationID fakeStationID = new StationID("S123456");

        doThrow(new ConnectException("Error de connexió Bluetooth")).when(journeyRealizeHandlerMock).broadcastStationID(fakeStationID);

        assertThrows(ConnectException.class, () -> {
            unbondedBTSignal.BTbroadcast();
        });
    }

    @AfterEach
    void tearDown() {
        unbondedBTSignal = null;
        journeyRealizeHandlerMock = null;
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Totes les proves finalitzades.");
    }
}


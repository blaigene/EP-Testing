package micromobilityTest;

import micromobility.*;
import micromobility.exceptions.*;
import data.*;
import services.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import services.smartfeatures.QRDecoder;
import services.smartfeatures.UnbondedBTSignal;

import java.net.ConnectException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JourneyRealizeHandlerTest {

    @Mock private Server server;
    @Mock private QRDecoder qrDecoder;
    @Mock private UnbondedBTSignal btSignal;

    private PMVehicle vehicle;
    private JourneyService service;
    private JourneyRealizeHandler handler;

    @BeforeEach
    void setup() {
        vehicle = new PMVehicle("PMV123456", new GeographicPoint(40.712776F, (float) -74.005974), "VehicleModel", 100, null, "VehicleColor", "VehiclePlate");
        service = new JourneyService("J123456", "S012345");
        handler = new JourneyRealizeHandler(server, vehicle, service, qrDecoder, btSignal, new StationID("ST123456"));
    }


    @Test
    void testBroadcastStationIDWithValidID() throws ConnectException {
        StationID stationID = new StationID("ST123456");

        assertDoesNotThrow(() -> JourneyRealizeHandler.broadcastStationID(stationID));
    }

    @Test
    void testBroadcastStationIDWithNull() {
        assertThrows(ConnectException.class, () -> JourneyRealizeHandler.broadcastStationID(null));
    }

    @AfterEach
    void tearDown() {
        handler = null; // Clean up resources
    }
}

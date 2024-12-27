package test.servicesTest;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import micromobility.JourneyService;
import org.junit.jupiter.api.*;
import services.ServerImpl;
import services.exceptions.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private ServerImpl server;
    private VehicleID vehicleID;
    private UserAccount userAccount;
    private StationID stationID;
    private GeographicPoint stationLocation;
    private GeographicPoint wrongLocation;

    @BeforeAll
    static void setupAll() {
        System.out.println("Iniciant tests de ServerImpl...");
    }

    @BeforeEach
    void setup() {
        // Configuració inicial abans de cada test
        server = new ServerImpl();
        vehicleID = new VehicleID("VH123456");
        userAccount = new UserAccount("fakeuser_123456");
        stationID = new StationID("ST123456");
        stationLocation = new GeographicPoint(41.40338f, 2.17403f); // Latitud i longitud de la ubicació de l'estació
        wrongLocation = new GeographicPoint(40.71278f, -74.0060f);  // Latitud i longitud incorrecta
        server.registerLocation(vehicleID, stationID); // Registra el vehicle a l'estació
    }

    @Test
    void testCheckPMVAvail_vehicleAvailable() throws ConnectException, PMVNotAvailException {
        // Afegeix el vehicle com a disponible
        Map<VehicleID, Boolean> vehicleAvailability = new HashMap<>();
        vehicleAvailability.put(vehicleID, true);

        assertDoesNotThrow(() -> server.checkPMVAvail(vehicleID));
    }

    @Test
    void testCheckPMVAvail_vehicleNotAvailable() {
        // Test per comprovar que es llença l'excepció si el vehicle no està disponible
        assertThrows(PMVNotAvailException.class, () -> {
            server.checkPMVAvail(vehicleID);
        });
    }

    @Test
    void testRegisterPairing_validPairing() throws InvalidPairingArgsException, ConnectException {
        // Test per verificar que es pot registrar un emparellament vàlid
        server.registerLocation(vehicleID, stationID); // Registra la ubicació del vehicle a l'estació
        assertDoesNotThrow(() -> server.registerPairing(userAccount, vehicleID, stationID, stationLocation, LocalDateTime.now()));
    }

    @Test
    void testRegisterPairing_invalidLocation() {
        // Test per verificar que es llença excepció si la ubicació no és correcta
        assertThrows(InvalidPairingArgsException.class, () -> {
            server.registerPairing(userAccount, vehicleID, stationID, wrongLocation, LocalDateTime.now());
        });
    }

    @Test
    void testStopPairing_validPairing() throws InvalidPairingArgsException, PairingNotFoundException, ConnectException {

        // Registra la ubicació del vehicle
        server.registerLocation(vehicleID, stationID);

        // Registra l'emparellament
        server.registerPairing(userAccount, vehicleID, stationID, stationLocation, LocalDateTime.now());

        // Verifica que es pot aturar l'emparellament sense excepcions
        assertDoesNotThrow(() -> {
            server.stopPairing(userAccount, vehicleID, stationID, stationLocation, LocalDateTime.now(), 20.0f, 5.0f, 30, new BigDecimal("12.50"));
        });
    }

    @Test
    void testStopPairing_noPairingFound() {
        // Test per comprovar que es llença l'excepció si no es troba l'emparellament
        assertThrows(PairingNotFoundException.class, () -> {
            server.stopPairing(userAccount, vehicleID, stationID, stationLocation, LocalDateTime.now(), 20.0f, 5.0f, 30, new BigDecimal("12.50"));
        });
    }

    @AfterEach
    void tearDown() {
        // Neteja després de cada test
        server = null;
        vehicleID = null;
        userAccount = null;
        stationID = null;
        stationLocation = null;
        wrongLocation = null;
    }

    @AfterAll
    static void tearDownAll() {
        // Executat després de tots els tests
        System.out.println("Finalitzats els tests de ServerImpl.");
    }
}

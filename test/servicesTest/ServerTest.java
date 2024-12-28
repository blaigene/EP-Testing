package servicesTest;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import data.ServiceID;
import micromobility.JourneyService;
import micromobility.payment.*;
import org.junit.jupiter.api.*;
import services.ServerImpl;
import services.exceptions.*;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    private ServerImpl server;
    private VehicleID vehicleID;
    private UserAccount userAccount;
    private StationID stationID;
    private GeographicPoint stationLocation;

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

        // Inicialitzar les col·leccions necessàries abans de les proves
        server.registerLocation(vehicleID, stationID); // Registra el vehicle a l'estació
        server.stationGPs.put(stationID, stationLocation); // Afegir la ubicació de l'estació
        server.vehicleAvailability.put(vehicleID, true); // El vehicle està disponible
    }

    @Test
    void testCheckPMVAvail_vehicleNotAvailable() {
        // El vehicle no està disponible, esperem l'excepció ConnectException
        VehicleID invalidVehicleID = new VehicleID("VH999999");

        assertThrows(ConnectException.class, () -> server.checkPMVAvail(invalidVehicleID));
    }

    @Test
    void testCheckPMVAvail_vehiclePaired() throws ConnectException, PMVNotAvailException, InvalidPairingArgsException {
        // Pairem un vehicle i després verifiquem la seva disponibilitat
        server.setPairing(userAccount, vehicleID, stationID, stationLocation, LocalDateTime.now());

        assertThrows(PMVNotAvailException.class, () -> server.checkPMVAvail(vehicleID));
    }

    @Test
    void testRegisterPairing_success() throws InvalidPairingArgsException, ConnectException {
        // Prova un registre d'emparellament correcte
        server.registerPairing(userAccount, vehicleID, stationID, stationLocation, LocalDateTime.now());

        // Comprovem que el vehicle s'ha emparellat correctament
        assertEquals(userAccount, server.activePairings.get(vehicleID));
        assertFalse(server.vehicleAvailability.get(vehicleID)); // Vehicle no disponible
    }

    @Test
    void testRegisterPairing_invalidStationLocation() {
        // Prova un emparellament amb una ubicació incorrecta
        GeographicPoint wrongLocation = new GeographicPoint(40.0f, 3.0f);

        assertThrows(InvalidPairingArgsException.class, () ->
                server.registerPairing(userAccount, vehicleID, stationID, wrongLocation, LocalDateTime.now())
        );
    }

    @Test
    void testStopPairing_success() throws InvalidPairingArgsException, ConnectException, PairingNotFoundException {
        server.registerPairing(userAccount, vehicleID, stationID, stationLocation, LocalDateTime.now());

        // Prova d'aturar l'emparellament
        server.stopPairing(userAccount, vehicleID, stationID, stationLocation, LocalDateTime.now(), 15f, 100f, 30, new BigDecimal("5.00"));

        // Comprovem que l'emparellament s'ha eliminat
        assertNull(server.activePairings.get(vehicleID));
        assertTrue(server.vehicleAvailability.get(vehicleID)); // Vehicle torna a estar disponible
    }

    @Test
    void testStopPairing_invalidPairing() {
        assertThrows(InvalidPairingArgsException.class, () ->
                server.stopPairing(userAccount, vehicleID, stationID, stationLocation, LocalDateTime.now(), 15f, 100f, 30, new BigDecimal("5.00"))
        );
    }

    @Test
    void testUnpairRegisterService_success() throws PairingNotFoundException, InvalidPairingArgsException, ConnectException {
        server.registerPairing(userAccount, vehicleID, stationID, stationLocation, LocalDateTime.now());

        // Creem un JourneyService fictici
        JourneyService journeyService = new JourneyService("J000001", "S000001");
        journeyService.setUserAccount(userAccount);
        journeyService.setVehicleID(vehicleID);

        // Prova d'unpair i registre del servei
        server.unPairRegisterService(journeyService);

        // Comprovem que el vehicle ja no està emparellat i que està disponible
        assertNull(server.activePairings.get(vehicleID));
        assertTrue(server.vehicleAvailability.get(vehicleID));
    }

    @Test
    void testRegisterPayment_successfulWalletPayment() throws Exception {
        BigDecimal imp = new BigDecimal("50.00");
        char payMeth = 'W';
        server.registerPayment(new ServiceID("SE000001"), userAccount, imp, payMeth);
        assertTrue(server.paymentsDB.containsKey(new ServiceID("SE000001")));
        assertTrue(server.paymentsDB.get(new ServiceID("SE000001")) instanceof WalletPayment);
    }

    @Test
    void testRegisterPayment_serviceIDIsNull() {
        BigDecimal imp = new BigDecimal("50.00");
        char payMeth = 'W';
        assertThrows(ConnectException.class, () -> server.registerPayment(null, userAccount, imp, payMeth));
    }

    @Test
    void testRegisterPayment_userAccountIsNull() {
        BigDecimal imp = new BigDecimal("50.00");
        char payMeth = 'W';
        assertThrows(ConnectException.class, () -> server.registerPayment(new ServiceID("SE000001"), null, imp, payMeth));
    }

    @Test
    void testRegisterPayment_methodNotSupported() {
        BigDecimal imp = new BigDecimal("50.00");
        char payMeth = 'C';
        assertThrows(MethodNotSupportedException.class, () -> server.registerPayment(new ServiceID("SE000001"), userAccount, imp, payMeth));
    }

    @AfterEach
    void tearDown() {
        // Netejar l'estat després de cada test
        server = null;
        vehicleID = null;
        userAccount = null;
        stationID = null;
        stationLocation = null;
    }

    @AfterAll
    static void tearDownAll() {
        // Executat després de tots els tests
        System.out.println("Finalitzats els tests de Server.");
    }
}


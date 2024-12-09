package test;
import data.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests per verificar les classes del paquet `data` seguint la estructura JUnit 5.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DataClassesTest {

    @BeforeAll
    static void initAll() {
        System.out.println("Iniciant els tests per a les classes del paquet `data`.");
    }

    @BeforeEach
    void setUp() {
        System.out.println("Preparant el context per al següent test...");
    }

    @Test
    void testGeographicPointExceptions() {
        System.out.println("Executant test: testGeographicPointExceptions");
        // Tests d'excepcions per GeographicPoint
        assertThrows(IllegalArgumentException.class, () -> new GeographicPoint(-91, 0));
        assertThrows(IllegalArgumentException.class, () -> new GeographicPoint(0, -181));
        assertDoesNotThrow(() -> new GeographicPoint(45.123f, 12.456f));
    }

    @Test
    void testStationIDExceptions() {
        System.out.println("Executant test: testStationIDExceptions");
        // Tests d'excepcions per StationID
        assertThrows(IllegalArgumentException.class, () -> new StationID(null));
        assertThrows(IllegalArgumentException.class, () -> new StationID("ST123"));
        assertDoesNotThrow(() -> new StationID("ST12345"));
    }

    @Test
    void testVehicleIDExceptions() {
        System.out.println("Executant test: testVehicleIDExceptions");
        // Tests d'excepcions per VehicleID
        assertThrows(IllegalArgumentException.class, () -> new VehicleID(""));
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("VE123456"));
        assertDoesNotThrow(() -> new VehicleID("VH123456"));
    }

    @Test
    void testUserAccountExceptions() {
        System.out.println("Executant test: testUserAccountExceptions");
        // Tests d'excepcions per UserAccount
        assertThrows(IllegalArgumentException.class, () -> new UserAccount(""));
        assertThrows(IllegalArgumentException.class, () -> new UserAccount("ab"));
        assertDoesNotThrow(() -> new UserAccount("user_123"));
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test finalitzat. Netejant el context...");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Tots els tests han finalitzat.");
    }
}


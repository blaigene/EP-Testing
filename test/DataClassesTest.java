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
        GeographicPoint p1 = new GeographicPoint(45.123f, 12.456f);
        GeographicPoint p2 = new GeographicPoint(45.123f, 12.456f);
        GeographicPoint p3 = new GeographicPoint(-45.123f, 12.456f);

        // Test del constructor
        assertThrows(IllegalArgumentException.class, () -> new GeographicPoint(-91, 0));
        assertThrows(IllegalArgumentException.class, () -> new GeographicPoint(0, -181));
        assertThrows(IllegalArgumentException.class, () -> new GeographicPoint(91, 0));
        assertThrows(IllegalArgumentException.class, () -> new GeographicPoint(0, 181));
        assertDoesNotThrow(() -> new GeographicPoint(45.123f, 12.456f));
        assertDoesNotThrow(() -> new GeographicPoint(90, 180));
        assertDoesNotThrow(() -> new GeographicPoint(-90, -180));

        // Test equals
        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));

        // Test hashCode
        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());

        // Testa toString
        String expected = "Geographic point {latitude='45.123',longitude='12.456'}";
        assertEquals(expected, p1.toString());
    }

    @Test
    void testStationIDExceptions() {
        System.out.println("Executant test: testStationIDExceptions");
        // Tests d'excepcions per StationID

        // Test del constructor
        assertThrows(IllegalArgumentException.class, () -> new StationID(null));
        assertThrows(IllegalArgumentException.class, () -> new StationID("ST123"));
        assertThrows(IllegalArgumentException.class, () -> new StationID("1234567"));
        assertThrows(IllegalArgumentException.class, () -> new StationID("SS12345"));
        assertDoesNotThrow(() -> new StationID("ST12345"));

        StationID id1 = new StationID("ST12345");
        StationID id2 = new StationID("ST12345");
        StationID id3 = new StationID("ST54321");

        // Test getId
        assertEquals("ST12345", id1.getId());

        // Test equals
        assertTrue(id1.equals(id2));
        assertFalse(id1.equals(id3));
        assertFalse(id1.equals(null));
        assertFalse(id1.equals("ST12345"));

        // Test hashCode
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());

        // Test toString
        String expected = "StationID{id='ST12345'}";
        assertEquals(expected, id1.toString());
    }

    @Test
    void testVehicleIDExceptions() {
        System.out.println("Executant test: testVehicleIDExceptions");
        // Tests d'excepcions per VehicleID

        // Test del constructor
        assertThrows(IllegalArgumentException.class, () -> new VehicleID(""));
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("VE123456"));
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("VH123456"));
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("90123456"));
        assertDoesNotThrow(() -> new VehicleID("VH12345"));

        VehicleID id1 = new VehicleID("VH12345");
        VehicleID id2 = new VehicleID("VH12345");
        VehicleID id3 = new VehicleID("VH54321");

        // Test getId
        assertEquals("VH12345", id1.getId());

        // Test equals
        assertTrue(id1.equals(id2));
        assertFalse(id1.equals(id3));
        assertFalse(id1.equals(null));
        assertFalse(id1.equals("VH12345"));

        // Test hashCode
        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());

        // Test toString
        String expected = "VehicleID{id='VH12345'}";
        assertEquals(expected, id1.toString());
    }

    @Test
    void testUserAccountExceptions() {
        System.out.println("Executant test: testUserAccountExceptions");
        // Tests d'excepcions per UserAccount

        // Test del constructor
        assertThrows(IllegalArgumentException.class, () -> new UserAccount(""));
        assertThrows(IllegalArgumentException.class, () -> new UserAccount(null));
        assertThrows(IllegalArgumentException.class, () -> new UserAccount("ab"));
        assertThrows(IllegalArgumentException.class, () -> new UserAccount("0123456789abcdef"));
        assertThrows(IllegalArgumentException.class, () -> new UserAccount("user@name"));
        assertDoesNotThrow(() -> new UserAccount("BlaiGene"));

        UserAccount account1 = new UserAccount("user.name");
        UserAccount account2 = new UserAccount("user.name");
        UserAccount account3 = new UserAccount("another_user");

        // Test getUsername
        assertEquals("user.name", account1.getUsername());

        // Test equals
        assertTrue(account1.equals(account2));
        assertFalse(account1.equals(account3));
        assertFalse(account1.equals(null));
        assertFalse(account1.equals("user.name"));

        // Test hashCode
        assertEquals(account1.hashCode(), account2.hashCode());
        assertNotEquals(account1.hashCode(), account3.hashCode());

        // Test toString
        String expected = "UserAccount{username='user.name'}";
        assertEquals(expected, account1.toString());
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


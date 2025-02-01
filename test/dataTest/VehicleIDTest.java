package dataTest;

import data.VehicleID;
import data.exceptions.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests per verificar la classe VehicleID seguint l'estructura JUnit 5.
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehicleIDTest {

    @BeforeAll
    static void initAll() {
        System.out.println("INICIALIZANDO VEHICLEID TESTS");
    }

    @BeforeEach
    void setUp() {
        System.out.println("Preparant el context per al següent test...");
    }

    @Test
    void testConstructor() {
        assertThrows(NullArgumentException.class, () -> new VehicleID(""));
        assertThrows(NullArgumentException.class, () -> new VehicleID(null));
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("VE1234567"));
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("VH1234567"));
        assertThrows(IllegalArgumentException.class, () -> new VehicleID("901234567"));
        assertDoesNotThrow(() -> new VehicleID("VH123456"));
    }

    @Test
    void testGetters() {
        VehicleID id1 = new VehicleID("VH123456");
        assertEquals("VH123456", id1.getId());
    }

    @Test
    void testEquals() {
        VehicleID id1 = new VehicleID("VH123456");
        VehicleID id2 = new VehicleID("VH123456");
        VehicleID id3 = new VehicleID("VH543216");

        assertTrue(id1.equals(id2));
        assertFalse(id1.equals(id3));
        assertFalse(id1.equals(null));
        assertFalse(id1.equals("VH123456"));
    }

    @Test
    void testHashCode() {
        VehicleID id1 = new VehicleID("VH123456");
        VehicleID id2 = new VehicleID("VH123456");
        VehicleID id3 = new VehicleID("VH543216");

        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void testToString() {
        VehicleID id1 = new VehicleID("VH123456");
        String expected = "VehicleID{id='VH123456'}";
        assertEquals(expected, id1.toString());
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test finalitzat. Netejant el context...");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("FINALIZANDO VEHICLEID TESTS");
        System.out.println();
    }

}

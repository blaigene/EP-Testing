package test.dataTest;
import data.exceptions.NullArgumentException;
import data.mocks.StationID;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests per verificar la classe StationID seguint la estructura JUnit 5.
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StationIDTest {

    @BeforeAll
    static void initAll() {
        System.out.println("Iniciant els tests per a la classe StationID.");
    }

    @BeforeEach
    void setUp() {
        System.out.println("Preparant el context per al següent test...");
    }

    @Test
    void testConstructor() {
        assertThrows(NullArgumentException.class, () -> new StationID(""));
        assertThrows(NullArgumentException.class, () -> new StationID(null));
        assertThrows(IllegalArgumentException.class, () -> new StationID("ST123"));
        assertThrows(IllegalArgumentException.class, () -> new StationID("1234567"));
        assertThrows(IllegalArgumentException.class, () -> new StationID("SS123456"));
        assertDoesNotThrow(() -> new StationID("ST123456"));
    }

    @Test
    void testGetters() {
        StationID id1 = new StationID("ST123456");
        assertEquals("ST123456", id1.getId());
    }

    @Test
    void testEquals() {
        StationID id1 = new StationID("ST123456");
        StationID id2 = new StationID("ST123456");
        StationID id3 = new StationID("ST543216");

        assertTrue(id1.equals(id2));
        assertFalse(id1.equals(id3));
        assertFalse(id1.equals(null));
        assertFalse(id1.equals("ST12345"));
    }

    @Test
    void testHashCode() {
        StationID id1 = new StationID("ST123456");
        StationID id2 = new StationID("ST123456");
        StationID id3 = new StationID("ST543216");

        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void testToString() {
        StationID id1 = new StationID("ST123456");
        String expected = "StationID{id='ST123456'}";
        assertEquals(expected, id1.toString());
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

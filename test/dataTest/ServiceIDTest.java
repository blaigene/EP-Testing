package dataTest;

import data.ServiceID;
import data.exceptions.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests per verificar la classe ServiceID.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServiceIDTest {

    @BeforeAll
    static void initAll() {
        System.out.println("Iniciant els tests per a la classe ServiceID.");
    }

    @BeforeEach
    void setUp() {
        System.out.println("Preparant el context per al següent test...");
    }

    @Test
    void testConstructor() {
        assertThrows(NullArgumentException.class, () -> new ServiceID(""));
        assertThrows(NullArgumentException.class, () -> new ServiceID(null));
        assertThrows(IllegalArgumentException.class, () -> new ServiceID("SE123"));
        assertThrows(IllegalArgumentException.class, () -> new ServiceID("1234567"));
        assertThrows(IllegalArgumentException.class, () -> new ServiceID("SS123456"));
        assertDoesNotThrow(() -> new ServiceID("SE123456"));
    }

    @Test
    void testGetters() {
        ServiceID id1 = new ServiceID("SE123456");
        assertEquals("SE123456", id1.getId());
    }

    @Test
    void testEquals() {
        ServiceID id1 = new ServiceID("SE123456");
        ServiceID id2 = new ServiceID("SE123456");
        ServiceID id3 = new ServiceID("SE543216");

        assertTrue(id1.equals(id2));
        assertFalse(id1.equals(id3));
        assertFalse(id1.equals(null));
        assertFalse(id1.equals("SE12345"));
    }

    @Test
    void testHashCode() {
        ServiceID id1 = new ServiceID("SE123456");
        ServiceID id2 = new ServiceID("SE123456");
        ServiceID id3 = new ServiceID("SE543216");

        assertEquals(id1.hashCode(), id2.hashCode());
        assertNotEquals(id1.hashCode(), id3.hashCode());
    }

    @Test
    void testToString() {
        ServiceID id1 = new ServiceID("SE123456");
        String expected = "ServiceID{id='SE123456'}";
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
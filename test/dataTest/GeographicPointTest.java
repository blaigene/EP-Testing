package dataTest;

import data.GeographicPoint;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests per verificar la classe GeographicPoint seguint la estructura JUnit 5.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GeographicPointTest {

    @BeforeAll
    static void initAll() {
        System.out.println("INICIALIZANDO GEOGRAPHICPOINT TESTS");
    }

    @BeforeEach
    void setUp() {
        System.out.println("Preparant el context per al següent test...");
    }

    @Test
    void testConstructor() {
        assertDoesNotThrow(() -> new GeographicPoint(45.123f, 12.456f));
        assertDoesNotThrow(() -> new GeographicPoint(90, 180));
        assertDoesNotThrow(() -> new GeographicPoint(-90, -180));
    }

    @Test
    void testEquals() {
        GeographicPoint p1 = new GeographicPoint(45.123f, 12.456f);
        GeographicPoint p2 = new GeographicPoint(45.123f, 12.456f);
        GeographicPoint p3 = new GeographicPoint(-45.123f, 12.456f);

        assertTrue(p1.equals(p2));
        assertFalse(p1.equals(p3));
    }

    @Test
    void testHashCode() {
        GeographicPoint p1 = new GeographicPoint(45.123f, 12.456f);
        GeographicPoint p2 = new GeographicPoint(45.123f, 12.456f);
        GeographicPoint p3 = new GeographicPoint(-45.123f, 12.456f);

        assertEquals(p1.hashCode(), p2.hashCode());
        assertNotEquals(p1.hashCode(), p3.hashCode());
    }

    @Test
    void testToString() {
        GeographicPoint p1 = new GeographicPoint(45.123f, 12.456f);
        String expected = "Geographic point {latitude='45.123',longitude='12.456'}";
        assertEquals(expected, p1.toString());
    }

    @AfterEach
    void tearDown() {
    System.out.println("Test finalitzat. Netejant el context...");
}

    @AfterAll
    static void tearDownAll() {
        System.out.println("FINALIZANDO GEOGRAPHICPOINT TESTS");
        System.out.println();
    }

}



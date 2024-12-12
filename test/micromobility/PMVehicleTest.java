package test.micromobility;

import data.mocks.*;
import micromobility.mocks.*;
import org.junit.jupiter.api.*;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Classe de tests per a PMVehicle.
 */
public class PMVehicleTest {
    private static BufferedImage sampleQrCode;
    private PMVehicle PMVehicle;
    private GeographicPoint sampleLocation;

    @BeforeAll
    static void setupBeforeAll() {
        System.out.println("Iniciant proves per a la classe PMVehicle...");
        // Crear un QR Code simulador per utilitzar a les proves
        sampleQrCode = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    @BeforeEach
    void setupBeforeEach() {
        System.out.println("Preparant PMVehicle abans de cada prova...");
        // Exemple de punt geogràfic
        sampleLocation = new GeographicPoint(41.3851f, 2.1734f);
        PMVehicle = new PMVehicle(
                "PMV012345",
                sampleLocation,
                "Temperatura: 22°C",
                75.0,
                sampleQrCode,
                "BlaiGene0123456",
                "ST012345"
        );
    }

    @Test
    void testConstructor() {
        assertNotNull(PMVehicle);
        assertEquals("PMV012345", PMVehicle.getId());
        assertEquals(PMVState.Available, PMVehicle.getState());
        assertEquals(sampleLocation, PMVehicle.getLocation());
        assertEquals("Temperatura: 22°C", PMVehicle.getSensorsData());
        assertEquals(75.0, PMVehicle.getChargeLevel());
        assertEquals(sampleQrCode, PMVehicle.getQrCode());
        assertEquals("BlaiGene0123456", PMVehicle.getDriverUsername());
        assertEquals("ST012345", PMVehicle.getStationID());
    }

    @Test
    void testGetters() {
        assertEquals("PMV012345", PMVehicle.getId());
        assertEquals(PMVState.Available, PMVehicle.getState());
        assertEquals("BlaiGene0123456", PMVehicle.getDriverUsername());
        assertEquals("ST012345", PMVehicle.getStationID());
    }

    @Test
    void testStateTransitions() {
        PMVehicle.setNotAvailble();
        assertEquals(PMVState.NotAvailable, PMVehicle.getState());

        PMVehicle.setUnderWay();
        assertEquals(PMVState.UnderWay, PMVehicle.getState());

        PMVehicle.setAvailble();
        assertEquals(PMVState.Available, PMVehicle.getState());
    }

    @Test
    void testSetLocation() {
        GeographicPoint newLocation = new GeographicPoint(40.7128f, -74.0060f);
        PMVehicle.setLocation(newLocation);
        assertEquals(newLocation, PMVehicle.getLocation());
    }

    @Test
    void testToString() {
        String expected = "PMVehicle{id='PMV012345', state=Available, location=" + sampleLocation +
                ", sensorsData='Temperatura: 22°C', chargeLevel=75.0, qrCode=present, username='BlaiGene0123456', stationID='ST012345'}";
        assertEquals(expected, PMVehicle.toString());
    }

    @AfterEach
    void cleanupAfterEach() {
        System.out.println("Netejant després de cada prova...");
        PMVehicle = null;
        sampleLocation = null;
    }

    @AfterAll
    static void cleanupAfterAll() {
        System.out.println("Finalitzant proves per a PMVehicle...");
        sampleQrCode = null;
    }
}

package micromobilityTest;

import data.GeographicPoint;
import micromobility.*;
import micromobility.exceptions.*;
import org.junit.jupiter.api.*;
import java.awt.image.BufferedImage;

/**
 * Classe de tests per a PMVehicle.
 */
public class PMVehicleTest {
    private static BufferedImage sampleQrCode;
    private micromobility.PMVehicle PMVehicle;
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
    void testIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PMVehicle("PM12345", sampleLocation, "Temperatura: 22°C", 75.0, sampleQrCode, "BlaiGene0123456", "ST012345"));
        Assertions.assertDoesNotThrow(() -> new PMVehicle("PMV012345", sampleLocation, "Temperatura: 22°C", 75.0, sampleQrCode, "BlaiGene0123456", "ST012345"));
    }

    @Test
    void testClassCastException() {
        // ClassCastException: location no és una instància de GeographicPoint
        Assertions.assertThrows(ClassCastException.class, () -> new PMVehicle("PMV012345", (GeographicPoint) new Object(), "Temperatura: 22°C", 75.0, sampleQrCode, "BlaiGene0123456", "ST012345"));
    }

    @Test
    void testIncorrectChargeLevel() {
        // IncorrectChargeLevel: nivell de càrrega fora de rang
        Assertions.assertThrows(IncorrectChargeLevel.class, () -> new PMVehicle("PMV012345", sampleLocation, "Temperatura: 22°C", -1, sampleQrCode, "BlaiGene0123456", "ST012345"));
        Assertions.assertThrows(IncorrectChargeLevel.class, () -> new PMVehicle("PMV012345", sampleLocation, "Temperatura: 22°C", 101, sampleQrCode, "BlaiGene0123456", "ST012345"));
    }

    @Test
    void testStateTransitions() {
        PMVehicle.setNotAvailble();
        Assertions.assertEquals(PMVState.NotAvailable, PMVehicle.getState());

        PMVehicle.setUnderWay();
        Assertions.assertEquals(PMVState.UnderWay, PMVehicle.getState());

        PMVehicle.setAvailble();
        Assertions.assertEquals(PMVState.Available, PMVehicle.getState());

        // Añadido el test para TemporaryParking
        PMVehicle.setTemporaryParking();
        Assertions.assertEquals(PMVState.TemporaryParking, PMVehicle.getState());
    }

    @Test
    void testSetLocation() {
        GeographicPoint newLocation = new GeographicPoint(40.7128f, -74.0060f);
        PMVehicle.setLocation(newLocation);
        Assertions.assertEquals(newLocation, PMVehicle.getLocation());
    }

    @Test
    void testToString() {
        String expected = "PMVehicle{id='PMV012345', state=Available, location=" + sampleLocation +
                ", sensorsData='Temperatura: 22°C', chargeLevel=75.0, qrCode=present, username='BlaiGene0123456', stationID='ST012345'}";
        Assertions.assertEquals(expected, PMVehicle.toString());
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

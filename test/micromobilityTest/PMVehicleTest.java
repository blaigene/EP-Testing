package micromobilityTest;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
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
        System.out.println("INICIALIZANDO PMVEHICLE TESTS");
        // Crear un QR Code simulador per utilitzar a les proves
        sampleQrCode = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
    }

    @BeforeEach
    void setupBeforeEach() {
        System.out.println("Preparant PMVehicle abans de cada prova...");
        // Exemple de punt geogràfic
        sampleLocation = new GeographicPoint(41.3851f, 2.1734f);
        PMVehicle = new PMVehicle(
                new VehicleID("VH012345"),
                sampleLocation,
                "Temperatura: 22°C",
                75.0,
                sampleQrCode,
                new UserAccount("BlaiGene0123456"),
                new StationID("ST012345"));
    }

    @Test
    void testIllegalArgumentException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new PMVehicle(new VehicleID("VH12345"), sampleLocation, "Temperatura: 22°C", 75.0, sampleQrCode, new UserAccount("BlaiGene0123456"), new StationID("ST012345")));
        Assertions.assertDoesNotThrow(() -> new PMVehicle(new VehicleID("VH012345"), sampleLocation, "Temperatura: 22°C", 75.0, sampleQrCode, new UserAccount("BlaiGene0123456"), new StationID("ST012345")));
    }

    @Test
    void testClassCastException() {
        // ClassCastException: location no és una instància de GeographicPoint
        Assertions.assertThrows(ClassCastException.class, () -> new PMVehicle(new VehicleID("VH012345"), (GeographicPoint) new Object(), "Temperatura: 22°C", 75.0, sampleQrCode, new UserAccount("BlaiGene0123456"), new StationID("ST012345")));
    }

    @Test
    void testIncorrectChargeLevel() {
        // IncorrectChargeLevel: nivell de càrrega fora de rang
        Assertions.assertThrows(IncorrectChargeLevel.class, () -> new PMVehicle(new VehicleID("VH012345"), sampleLocation, "Temperatura: 22°C", -1, sampleQrCode, new UserAccount("BlaiGene0123456"), new StationID("ST012345")));
        Assertions.assertThrows(IncorrectChargeLevel.class, () -> new PMVehicle(new VehicleID("VH012345"), sampleLocation, "Temperatura: 22°C", 101, sampleQrCode, new UserAccount("BlaiGene0123456"), new StationID("ST012345")));
    }

    @Test
    void testStateTransitions() {
        Assertions.assertDoesNotThrow(() -> PMVehicle.setNotAvailable());
        Assertions.assertEquals(PMVState.NotAvailable, PMVehicle.getState());

        Assertions.assertDoesNotThrow(() -> PMVehicle.setUnderWay());
        Assertions.assertEquals(PMVState.UnderWay, PMVehicle.getState());

        Assertions.assertDoesNotThrow(() -> PMVehicle.setAvailable());
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

    @AfterEach
    void cleanupAfterEach() {
        System.out.println("Netejant després de cada prova...");
        PMVehicle = null;
        sampleLocation = null;
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("FINALIZANDO PMVEHICLE TESTS");
        System.out.println();
    }
}

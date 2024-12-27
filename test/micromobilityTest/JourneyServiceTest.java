package test.micromobilityTest;

import data.GeographicPoint;
import data.UserAccount;
import data.VehicleID;
import micromobility.*;
import micromobility.exceptions.*;
import org.junit.jupiter.api.*;

/**
 * Classe de proves per a la classe JourneyService.
 */
public class JourneyServiceTest {

    private JourneyService journeyService;

    @BeforeAll
    static void setupAll() {
        System.out.println("Iniciant proves de JourneyService...");
    }

    @BeforeEach
    void setup() {
        journeyService = new JourneyService("J123456", "S012345");
    }

    @Test
    void testConstructorValid() {
        Assertions.assertNotNull(journeyService);
        Assertions.assertEquals("J123456", journeyService.getJourneyId());
        Assertions.assertEquals("S012345", journeyService.getServiceID());
    }

    @Test
    void testConstructorInvalidJourneyId() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new JourneyService("J12345", "usuari_exemple1"); // Format incorrecte
        });
    }

    @Test
    void testConstructorInvalidServiceID() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new JourneyService("J123456", "usuari"); // Format incorrecte
        });
    }

    @Test
    void testGetters() {
        Assertions.assertEquals("J123456", journeyService.getJourneyId());
        Assertions.assertEquals("S012345", journeyService.getServiceID());
        Assertions.assertNotNull(journeyService.getInitDate());
        Assertions.assertNotNull(journeyService.getInitHour());
        Assertions.assertNull(journeyService.getEndDate());
        Assertions.assertNull(journeyService.getEndHour());
        journeyService.setEndDate();
        journeyService.setEndHour();
        Assertions.assertEquals(0, journeyService.getDuration());
        journeyService.duration = 60;
        journeyService.setDistance(10);
        Assertions.assertEquals(10, journeyService.getDistance());
        Assertions.assertEquals(10, journeyService.getAvgSpeed());
        Assertions.assertNull(journeyService.getOriginPoint());
        Assertions.assertNull(journeyService.getEndPoint());
        journeyService.setImportCost(100);
        Assertions.assertEquals(100, journeyService.getImportCost());
        Assertions.assertFalse(journeyService.isInProgress());
        journeyService.setUserAccount(new UserAccount("usuari_exemple1"));
        Assertions.assertEquals(new UserAccount("usuari_exemple1"), journeyService.getUserAccount());
        journeyService.setVehicleID(new VehicleID("VH012345"));
        Assertions.assertEquals(new VehicleID("VH012345"), journeyService.getVehicleID());
    }

    @Test
    void testSetDistanceValid() {
        journeyService.duration = 60;
        journeyService.setDistance(15.5);
        Assertions.assertEquals(15.5, journeyService.getDistance());
    }

    @Test
    void testAvgSpeedValid() {
        journeyService.duration = 60;
        journeyService.setDistance(15.0);
        journeyService.setAvgSpeed(distance / (minutesBetween / 60.0));
        Assertions.assertEquals(15.0, journeyService.getAvgSpeed());
    }

    @Test
    void testSetDistanceInvalid() {
        Assertions.assertThrows(NegativeDistanceException.class, () -> journeyService.setDistance(-10.0));
    }

    @Test
    void testSetOriginPoint() {
        journeyService.setOriginPoint(new GeographicPoint(20,20));
        Assertions.assertEquals(new GeographicPoint(20,20), journeyService.getOriginPoint());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            journeyService.setOriginPoint(null);
        });
    }

    @Test
    void testSetOriginPointNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            journeyService.setOriginPoint(null);
        });
    }

    @Test
    void testSetEndPoint() {
        journeyService.setEndPoint(new GeographicPoint(10,10));
        Assertions.assertEquals(new GeographicPoint(10,10), journeyService.getEndPoint());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            journeyService.setEndPoint(null);
        });
    }

    @Test
    void testSetEndPointNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            journeyService.setEndPoint(null);
        });
    }

    @Test
    void testSetEndPointEqualToOrigin() {
        GeographicPoint originalPoint = new GeographicPoint(41, 2);
        GeographicPoint endingPoint = new GeographicPoint(41, 2);
        journeyService.setOriginPoint(originalPoint);
        Assertions.assertThrows(EqualInitAndEndPointException.class, () -> {
            journeyService.setEndPoint(endingPoint);
        });
    }

    @Test
    void testSetImportCostNegative() {
        double importCost = -10.0;
        Assertions.assertThrows(NegativeImportCostException.class, () -> {
            journeyService.setImportCost(importCost);
        });
    }

    @Test
    void testSetImportCostPositive() {
        double importCost = 100.0;
        Assertions.assertDoesNotThrow(() -> {
            journeyService.setImportCost(importCost);
        });
    }

    @Test
    void testServiceInitAndFinish() {
        journeyService.setServiceInit();
        Assertions.assertTrue(journeyService.isInProgress());

        journeyService.setServiceFinish();
        Assertions.assertFalse(journeyService.isInProgress());
    }

    @Test
    void testSetUserAccountNull() {
        UserAccount userAccount = null;
        Assertions.assertThrows(NullArgumentException.class, () -> {
            journeyService.setUserAccount(userAccount);
        });
    }

    // Test per al setVehicleID amb vehicle nul
    @Test
    void testSetVehicleIDNull() {
        VehicleID vehicleID = null;
        Assertions.assertThrows(NullArgumentException.class, () -> {
            journeyService.setVehicleID(vehicleID);
        });
    }

    @AfterEach
    void tearDown() {
        journeyService = null;
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finalitzant proves de JourneyService...");
    }
}


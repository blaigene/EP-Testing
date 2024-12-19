package test.micromobility;

import exceptions.*;
import org.junit.jupiter.api.*;
import micromobility.*;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

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
        journeyService = new JourneyService(
                "J123456",
                LocalDate.of(2024, 12, 1),
                LocalTime.of(10, 30),
                60,
                10.5,
                10.5,
                "Plaça Catalunya",
                "Plaça Espanya",
                LocalDate.of(2024, 12, 1),
                LocalTime.of(11, 30),
                15.0,
                "S123456",
                false,
                "usuari_exemple1",
                "VH123456"
        );
    }

    @Test
    void testJourneyServiceCreationValid() {
        assertNotNull(journeyService);
        assertEquals("J123456", journeyService.getJourneyId());
        assertEquals("S123456", journeyService.getServiceID());
    }

    @Test
    void testInvalidJourneyIdFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JourneyService(
                    "J12345", // Format incorrecte
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(10, 30),
                    60,
                    10.5,
                    10.5,
                    "Plaça Catalunya",
                    "Plaça Espanya",
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(11, 30),
                    15.0,
                    "S123456",
                    false,
                    "usuari_exemple1",
                    "VH123456"
            );
        });
    }

    @Test
    void testInvalidServiceIdFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JourneyService(
                    "J123456",
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(10, 30),
                    60,
                    10.5,
                    10.5,
                    "Plaça Catalunya",
                    "Plaça Espanya",
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(11, 30),
                    15.0,
                    "S12345", // Format incorrecte
                    false,
                    "usuari_exemple1",
                    "VH123456"
            );
        });
    }

    @Test
    void testNegativeDuration() {
        assertThrows(NegativeDurationException.class, () -> {
            new JourneyService(
                    "J123456",
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(10, 30),
                    -1, // Duració negativa
                    10.5,
                    10.5,
                    "Plaça Catalunya",
                    "Plaça Espanya",
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(11, 30),
                    15.0,
                    "S123456",
                    false,
                    "usuari_exemple1",
                    "VH123456"
            );
        });
    }

    @Test
    void testEqualInitAndEndPoint() {
        assertThrows(EqualInitAndEndPointException.class, () -> {
            new JourneyService(
                    "J123456",
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(10, 30),
                    60,
                    10.5,
                    10.5,
                    "Plaça Catalunya",
                    "Plaça Catalunya", // Punt d'origen igual a punt de destinació
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(11, 30),
                    15.0,
                    "S123456",
                    false,
                    "usuari_exemple1",
                    "VH123456"
            );
        });
    }

    @Test
    void testInvalidUsernameFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JourneyService(
                    "J123456",
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(10, 30),
                    60,
                    10.5,
                    10.5,
                    "Plaça Catalunya",
                    "Plaça Espanya",
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(11, 30),
                    15.0,
                    "S123456",
                    false,
                    "usuari", // Format incorrecte
                    "VH123456"
            );
        });
    }

    @Test
    void testInvalidVehicleIdFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JourneyService(
                    "J123456",
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(10, 30),
                    60,
                    10.5,
                    10.5,
                    "Plaça Catalunya",
                    "Plaça Espanya",
                    LocalDate.of(2024, 12, 1),
                    LocalTime.of(11, 30),
                    15.0,
                    "S123456",
                    false,
                    "usuari_exemple1",
                    "VH12345" // Format incorrecte
            );
        });
    }

    @Test
    void testGetters() {
        assertEquals("J123456", journeyService.getJourneyId());
        assertEquals(LocalDate.of(2024, 12, 1), journeyService.getInitDate());
        assertEquals(LocalTime.of(10, 30), journeyService.getInitHour());
        assertEquals(60, journeyService.getDuration());
        assertEquals(10.5, journeyService.getDistance());
        assertEquals(10.5, journeyService.getAvgSpeed());
        assertEquals("Plaça Catalunya", journeyService.getOriginPoint());
        assertEquals("Plaça Espanya", journeyService.getEndPoint());
        assertEquals(LocalDate.of(2024, 12, 1), journeyService.getEndDate());
        assertEquals(LocalTime.of(11, 30), journeyService.getEndHour());
        assertEquals(15.0, journeyService.getImportCost());
        assertEquals("S123456", journeyService.getServiceID());
        assertFalse(journeyService.isInProgress());
        assertEquals("usuari_exemple1", journeyService.getUserAccount());
        assertEquals("VH123456", journeyService.getVehicleID());
    }

    @Test
    void testSetters() {
        journeyService.setServiceInit();
        assertTrue(journeyService.isInProgress());

        journeyService.setServiceFinish();
        assertFalse(journeyService.isInProgress());
    }

    @Test
    void testToString() {
        String expected = "JourneyService{" +
                "journeyId='J123456', " +
                "initDate=2024-12-01, " +
                "initHour=10:30, " +
                "duration=60, " +
                "distance=10.5, " +
                "avgSpeed=10.5, " +
                "originPoint='Plaça Catalunya', " +
                "endPoint='Plaça Espanya*', " +
                "endDate=2024-12-01, " +
                "endHour=11:30, " +
                "importCost=15.0, " +
                "serviceID='S123456', " +
                "inProgress=false, " +
                "username='usuari_exemple1', " +
                "vehicleID='VH123456'" +
                '}';
        assertEquals(expected, journeyService.toString());
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


package servicesTest;

import services.smartfeatures.QRDecoderImpl;
import services.exceptions.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.image.BufferedImage;

public class QRDecoderTest {

    private QRDecoderImpl qrDecoder;

    @BeforeAll
    static void setupAll() {
        System.out.println("Iniciant proves de QRDecoder...");
    }

    @BeforeEach
    void setup() {
        qrDecoder = new QRDecoderImpl();
    }

    @Test
    void testGetVehicleIDWhenNotCorrupted() throws CorruptedImgException {

        qrDecoder.setCorrupted(false);

        BufferedImage dummyImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB); // Dummy image

        // Es comprova que no retorni l'excepció i el mètode retorni null.
        assertDoesNotThrow(() -> qrDecoder.getVehicleID(dummyImage));
        assertNull(qrDecoder.getVehicleID(dummyImage));
    }

    @Test
    void testGetVehicleIDWhenCorrupted() {

        qrDecoder.setCorrupted(true);

        BufferedImage dummyImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB); // Dummy image

        // Es comprova que es llança CorruptedImgException quan la imatge és corrupta.
        CorruptedImgException exception = assertThrows(CorruptedImgException.class, () -> {
            qrDecoder.getVehicleID(dummyImage);
        });

        assertEquals("L'imatge del QR està danyada o no és valida.", exception.getMessage());
    }

    @AfterEach
    void tearDown() {
        qrDecoder = null;
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Finalitzant proves de QRDecoder...");
    }
}

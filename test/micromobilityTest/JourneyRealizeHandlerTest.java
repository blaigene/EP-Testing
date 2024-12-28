package micromobilityTest;

import data.GeographicPoint;
import micromobility.*;
import micromobility.exceptions.*;
import org.junit.jupiter.api.*;
import services.Server;
import data.VehicleID;

import java.net.ConnectException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas para JourneyRealizeHandler.
 */
public class JourneyRealizeHandlerTest {

    private JourneyRealizeHandler handler;
    private Server serverMock;
    private PMVehicle vehicleMock;
    private JourneyService serviceMock;

    @BeforeAll
    static void setupAll() {
        System.out.println("Iniciando pruebas para JourneyRealizeHandler...");
    }

    @BeforeEach
    void setup() {
        serverMock = mock(Server.class);
        vehicleMock = mock(PMVehicle.class);
        serviceMock = mock(JourneyService.class);
        handler = new JourneyRealizeHandler(serverMock, vehicleMock, serviceMock);
        System.out.println("Configurando el contexto para cada test...");
    }

    @Test
    void testScanQRWithConnectionFailure() {
        String qrCode = "validQRCode";
        when(serverMock.isConnected()).thenReturn(false);
        assertThrows(ConnectException.class, () -> handler.scanQR(qrCode));
    }

    @Test
    void testCalculateValues() {
        GeographicPoint endPoint = new GeographicPoint(45.123f, 12.456f);
        LocalDateTime endDate = LocalDateTime.now();
        // Suponiendo que setInitDate y setOriginPoint se llaman en algún momento antes.
        serviceMock.setInitDate(LocalDateTime.now().minusHours(2)); // Asumimos que el viaje empezó hace 2 horas.
        serviceMock.setOriginPoint(new GeographicPoint(40.712f, -74.006f)); // Punto de inicio.

        handler.calculateValues(endPoint, endDate);

        verify(serviceMock).setDuration(anyInt());
        verify(serviceMock).setDistance(anyDouble());
        verify(serviceMock).setAvgSpeed(anyDouble());
    }

    @Test
    void testCalculateImport() {
        float distance = 10.0f; // 10 km
        int duration = 120; // 120 minutos
        float avgSpeed = 50.0f; // 50 km/h
        LocalDateTime date = LocalDateTime.now();

        handler.calculateImport(distance, duration, avgSpeed, date);

        verify(serviceMock).setImportCost(anyDouble());
    }



    @Test
    void testScanQRWithValidParameters() throws Exception {
        String qrCode = "validQRCode";
        VehicleID mockVehicleID = new VehicleID("V123456"); // Asumiendo que tienes un constructor que acepta un string.

        when(serverMock.isConnected()).thenReturn(true);
        when(vehicleMock.getVehicleID()).thenReturn(mockVehicleID); // Asegúrate de que el mock de vehicle devuelva un VehicleID válido
        when(serverMock.checkPMVAvail(mockVehicleID)).thenReturn(true); // Usa el VehicleID en el stub de checkPMVAvail

        assertDoesNotThrow(() -> handler.scanQR(qrCode));
    }

    @Test
    void testStartDrivingWhenNotConnected() {
        when(serverMock.isConnected()).thenReturn(false);
        assertThrows(ConnectException.class, () -> handler.startDriving());
    }

    @Test
    void testStopDrivingWhenConnected() {
        when(serverMock.isConnected()).thenReturn(true);
        assertDoesNotThrow(() -> handler.stopDriving());
    }

    @Test
    void testInvalidVehicleIdThrowsProceduralException() throws ConnectException {
        String qrCode = "validQRCode";
        VehicleID invalidVehicleID = new VehicleID("InvalidID"); // Asumiendo que puedes crear un VehicleID así, aunque sea 'inválido' para tus reglas de negocio.

        when(serverMock.isConnected()).thenReturn(true);
        when(vehicleMock.getVehicleID()).thenReturn(invalidVehicleID); // Usa un VehicleID, no una cadena.
        when(serverMock.checkPMVAvail(invalidVehicleID)).thenThrow(new PMVNotAvailException("El vehículo no está disponible.")); // Configura el server mock para lanzar una excepción si el ID es inválido.

        assertThrows(ProceduralException.class, () -> handler.scanQR(qrCode)); // Asegúrate de que tu método scanQR convierte PMVNotAvailException en ProceduralException.
    }


    @AfterEach
    void tearDown() {
        System.out.println("Limpiando después de cada test...");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Todos los tests han finalizado.");
    }
}

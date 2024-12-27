package test.servicesTest;

import micromobility.exceptions.PMVPhisicalException;
import micromobility.exceptions.ProceduralException;
import org.junit.jupiter.api.*;
import services.*;
import services.smartfeatures.ArduinoMicroControllerImpl;

import java.net.ConnectException;

import static org.junit.jupiter.api.Assertions.*;

class ArduinoMicroControllerTest {

    private ArduinoMicroControllerImpl arduinoMicroController;

    @BeforeAll
    static void setupAll() {
        System.out.println("Iniciant proves de ArduinoMicroControllerImpl...");
    }

    @BeforeEach
    void setup() {
        arduinoMicroController = new ArduinoMicroControllerImpl();
    }

    @Test
    void testSetBTconnectionSuccessful() throws ConnectException {
        arduinoMicroController.setBTconnection();
        assertTrue(arduinoMicroController.isBTConnected());
    }

    @Test
    void testSetBTconnectionFailure() throws ConnectException {
        arduinoMicroController.setBTconnection();  // Primer emparellament correcte

        // Intentar emparejar de nou ha de llançar una excepció
        assertThrows(ConnectException.class, () -> {
            arduinoMicroController.setBTconnection();
        });
    }

    @Test
    void testStartDrivingSuccessful() throws PMVPhisicalException, ConnectException, ProceduralException {
        arduinoMicroController.setBTconnection();  // Estableix connexió BT
        arduinoMicroController.startDriving();     // Inicia el desplaçament
        assertTrue(arduinoMicroController.isDriving());
    }

    @Test
    void testStartDrivingWithoutBTConnection() {
        assertThrows(ConnectException.class, () -> {
            arduinoMicroController.startDriving(); // No hi ha connexió BT establerta
        });
    }

    @Test
    void testStartDrivingWithVehicleIssues() throws ConnectException {
        arduinoMicroController.setBTconnection();
        arduinoMicroController.setVehicleHasIssues(true);

        assertThrows(PMVPhisicalException.class, () -> {
            arduinoMicroController.startDriving(); // No pot començar per problemes tècnics
        });
    }

    @Test
    void testStopDrivingSuccessful() throws PMVPhisicalException, ConnectException, ProceduralException, ProceduralException, PMVPhisicalException {
        arduinoMicroController.setBTconnection();  // Estableix connexió BT
        arduinoMicroController.startDriving();     // Inicia el desplaçament
        arduinoMicroController.stopDriving();      // Atura el vehicle
        assertFalse(arduinoMicroController.isDriving());
    }

    @Test
    void testStopDrivingWithoutBTConnection() {
        assertThrows(ConnectException.class, () -> {
            arduinoMicroController.stopDriving();  // No hi ha connexió BT establerta
        });
    }

    @Test
    void testStopDrivingWhileVehicleNotInMotion() throws ConnectException {
        arduinoMicroController.setBTconnection();

        assertThrows(ProceduralException.class, () -> {
            arduinoMicroController.stopDriving(); // El vehicle no està en moviment
        });
    }

    @Test
    void testStopDrivingWithVehicleIssues() throws ConnectException, PMVPhisicalException, ProceduralException {
        arduinoMicroController.setBTconnection();
        arduinoMicroController.startDriving();      // Comença a conduir
        arduinoMicroController.setVehicleHasIssues(true);

        assertThrows(PMVPhisicalException.class, () -> {
            arduinoMicroController.stopDriving();   // No es pot aturar per problemes tècnics
        });
    }

    @AfterEach
    void tearDown() {
        arduinoMicroController = null;
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Totes les proves finalitzades.");
    }
}

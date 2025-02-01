package services.smartfeatures;

import java.net.ConnectException;
import java.util.concurrent.TimeUnit;
import data.StationID;
import micromobility.JourneyRealizeHandler;

public class UnbondedBTSignalImpl implements UnbondedBTSignal {

    private final StationID fakeStationID;
    private JourneyRealizeHandler jrh;

    public UnbondedBTSignalImpl() {
        fakeStationID = new StationID("ST123456");
    }

    @Override
    public void BTbroadcast() throws ConnectException {
        // Bucle infinit per continuar emetent l'ID de l'estació
        while (true) {
            try {
                // Crida al mètode responsable de transmetre l'ID de l'estació
                jrh.broadcastStationID(fakeStationID);

                // Espera un interval de temps definit abans de tornar a emetre (per exemple, cada 5 segons)
                TimeUnit.SECONDS.sleep(5);
            } catch (ConnectException ce) {
                // Gestiona les excepcions de connexió Bluetooth
                System.err.println("La connexió Bluetooth ha fallat: " + ce.getMessage());
                throw ce; // Torna a llençar l'excepció perquè la gestioni el nivell superior si cal
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    // Setter per injectar la dependència de JourneyRealizeHandler.
    public void setJRHandler (JourneyRealizeHandler jrh) {
        this.jrh = jrh;
    }

}

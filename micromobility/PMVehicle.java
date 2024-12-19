package micromobility;

import data.GeographicPoint;
import exceptions.*;
import java.awt.image.BufferedImage;

/**
 * Classe que representa un vehicle de micromobilitat.
 */
public class PMVehicle {
    private String id;
    private PMVState state;
    private GeographicPoint location;
    private String sensorsData;
    private double chargeLevel;
    private BufferedImage qrCode;
    private String driverUsername; //
    private String stationID; //
    
    public PMVehicle(String id, GeographicPoint location, String sensorsData, double chargeLevel, BufferedImage qrCode, String username, String stationID) {
        /*
        if (id == null || location == null || sensorsData == null || qrCode == null || username == null || stationID == null
        || id.trim().isEmpty() || sensorsData.trim().isEmpty() || username.trim().isEmpty() || stationID.trim().isEmpty()) {
            throw new NullArgumentException("Els paràmetres no poden ser nuls o buits");
        }
         */
        if (!id.matches("PMV[0-9]{6}")) {
            throw new IllegalArgumentException("L'identificador del vehicle ha de seguir el patró 'PMVxxxxxx' (6 dígits).");
        }
        if (chargeLevel < 0 || chargeLevel > 100) {
            throw new IncorrectChargeLevel("Nivell de càrrega ha de ser entre 0 i 100");
        }
        //
        if (!username.matches("[a-zA-Z0-9._-]{15}")) {
            throw new IllegalArgumentException("El nom d'usuari ha de tenir 15 caràcters (lletres, números, '.', '-', '_').");
        }
        //
        if (!stationID.matches("ST[0-9]{6}")) {
            throw new IllegalArgumentException("L'identificador de l'estació ha de seguir el patró 'STxxxxxx' (6 dígits).");
        }
        this.id = id;
        this.location = location;
        this.state = PMVState.Available;
        this.sensorsData = sensorsData;
        this.chargeLevel = chargeLevel;
        this.qrCode = qrCode;
        this.driverUsername = username;
        this.stationID = stationID;
    }

    public String getId() {
        return id;
    }

    public PMVState getState() {
        return state;
    }

    public GeographicPoint getLocation() {
        return location;
    }

    public String getSensorsData() {
        return sensorsData;
    }

    public double getChargeLevel() {
        return chargeLevel;
    }

    public BufferedImage getQrCode() {
        return qrCode;
    }

    public String getDriverUsername() { return driverUsername; }

    public String getStationID() { return stationID; }

    public void setNotAvailble() {
        this.state = PMVState.NotAvailable;
    }

    public void setUnderWay() {
        this.state = PMVState.UnderWay;
    }

    public void setAvailble() {
        this.state = PMVState.Available;
    }

    public void setLocation(GeographicPoint gP) {
        this.location = gP;
    }

    @Override
    public String toString() {
        return "PMVehicle{" +
                "id='" + id + '\'' +
                ", state=" + state +
                ", location=" + location +
                ", sensorsData='" + sensorsData + '\'' +
                ", chargeLevel=" + chargeLevel +
                ", qrCode=" + (qrCode != null ? "present" : "not present") +
                ", username='" + driverUsername + '\'' +
                ", stationID='" + stationID + '\'' +
                '}';
    }
}

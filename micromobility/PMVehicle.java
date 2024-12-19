package micromobility;

import data.GeographicPoint;
import micromobility.exceptions.*;
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
    private String username; //
    private String stationID; //
    
    public PMVehicle(String id, GeographicPoint location, String sensorsData, double chargeLevel, BufferedImage qrCode, String username, String stationID) {

        if (!id.matches("PMV[0-9]{6}")) {
            throw new IllegalArgumentException("L'identificador del vehicle ha de seguir el patró 'PMVxxxxxx' (6 dígits).");
        }
        if (chargeLevel < 0 || chargeLevel > 100) {
            throw new IncorrectChargeLevel("Nivell de càrrega ha de ser entre 0 i 100");
        }

        this.id = id;
        this.location = location;
        this.state = PMVState.Available;
        this.sensorsData = sensorsData;
        this.chargeLevel = chargeLevel;
        this.qrCode = qrCode;
        this.username = username;
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

    public String getUsername() { return username; }

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
                ", username='" + username + '\'' +
                ", stationID='" + stationID + '\'' +
                '}';
    }
}

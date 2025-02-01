package micromobility;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import micromobility.exceptions.*;
import java.awt.image.BufferedImage;

/**
 * Classe que representa un PMVehicle.
 */
public class PMVehicle {
    private VehicleID vehicleID;
    private PMVState state;
    private GeographicPoint location;
    private String sensorsData;
    private double chargeLevel;
    private BufferedImage qrCode;
    private UserAccount user; //
    private StationID stationID; //
    
    public PMVehicle(VehicleID vehicleID, GeographicPoint location, String sensorsData,
                     double chargeLevel, BufferedImage qrCode, UserAccount user, StationID stationID) {

        if (chargeLevel < 0 || chargeLevel > 100) {
            throw new IncorrectChargeLevel("Nivell de càrrega ha de ser entre 0 i 100");
        }

        this.vehicleID = vehicleID;
        this.location = location;
        this.state = PMVState.Available;
        this.sensorsData = sensorsData;
        this.chargeLevel = chargeLevel;
        this.qrCode = qrCode;
        this.user = user;
        this.stationID = stationID;
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

    public UserAccount getUser() { return user; }

    public StationID getStationID() {
        return stationID;
    }

    public void setNotAvailable() throws ProceduralException {
        if (!state.equals(PMVState.Available)) {
            throw new ProceduralException("El vehículo no se encuentra en el estado correcto.");
        }
        this.state = PMVState.NotAvailable;
    }

    public void setUnderWay() throws ProceduralException  {
        if (!state.equals(PMVState.NotAvailable)) {
            throw new ProceduralException("El vehículo no se encuentra en el estado correcto.");
        }
        this.state = PMVState.UnderWay;
    }

    public void setAvailable() throws ProceduralException  {
        if (!state.equals(PMVState.UnderWay)) {
            throw new ProceduralException("El vehículo no se encuentra en el estado correcto.");
        }
        this.state = PMVState.Available;
    }

    public VehicleID getVehicleID() { return vehicleID; }

    public void setVehicleID(VehicleID veh) {
        vehicleID = veh;
    }

    public void setTemporaryParking() {
        this.state = PMVState.TemporaryParking;  // Asigna el estado a TemporaryParking
    }

    public void setLocation(GeographicPoint gP) {
        this.location = gP;
    }

    @Override
    public String toString() {
        return "PMVehicle{" +
                "id='" + vehicleID + '\'' +
                ", state=" + state +
                ", location=" + location +
                ", sensorsData='" + sensorsData + '\'' +
                ", chargeLevel=" + chargeLevel +
                ", qrCode=" + (qrCode != null ? "present" : "not present") +
                ", username='" + user + '\'' +
                ", stationID='" + stationID + '\'' +
                '}';
    }
}

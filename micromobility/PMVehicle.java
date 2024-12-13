package micromobility;

import data.mocks.*;

/**
 * Classe que representa un vehicle de micromobilitat.
 */
public class PMVehicle {
    private String id;
    private PMVState state;
    private GeographicPoint location;

    public PMVehicle(String id, GeographicPoint location) {
        this.id = id;
        this.location = location;
        this.state = PMVState.Available;
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

    public void setNotAvailb() {
        this.state = PMVState.NotAvailable;
    }

    public void setUnderWay() {
        this.state = PMVState.UnderWay;
    }

    public void setAvailb() {
        this.state = PMVState.Available;
    }

    public void setLocation(GeographicPoint gP) {
        this.location = gP;
    }

    @Override
    public String toString() {
        return "PMVehicle{id='" + id + "', state=" + state + ", location=" + location + "}";
    }
}

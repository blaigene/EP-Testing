package data;
import data.exceptions.*;

/**
 * Classe que representa l'identificador d'un vehicle.
 */
final public class VehicleID {
    private final String id;

    public VehicleID(String id) {
        // TOCAR LO MATEIX QUE A STATION
        if (id == null || id.trim().isEmpty()) {
            throw new NullArgumentException("L'identificador del vehicle no pot ser nul o buit.");
        }
        if (!id.matches("VH[0-9]{6}")) {
            throw new IllegalArgumentException("L'identificador del vehicle ha de seguir el patró 'VHxxxxxx' (6 dígits).");
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleID vehicleID = (VehicleID) o;
        return id.equals(vehicleID.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "VehicleID{" +
                "id='" + id + '\'' +
                '}';
    }
}

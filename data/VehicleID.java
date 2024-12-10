package data;

/**
 * Classe que representa l'identificador d'un vehicle.
 */
final public class VehicleID {
    private final String id;

    public VehicleID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'identificador del vehicle no pot ser nul o buit.");
        }
        if (!id.matches("VH[0-9]{5}")) {
            throw new IllegalArgumentException("L'identificador del vehicle ha de seguir el patró 'VHxxxxx' (5 dígits).");
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

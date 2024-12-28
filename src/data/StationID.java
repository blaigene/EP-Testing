package data;
import data.exceptions.*;

/**
 * Classe que representa l'identificador d'una estació.
 */
final public class StationID {
    private final String id;

    public StationID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new NullArgumentException("L'identificador de l'estació no pot ser nul o buit.");
        }
        if (!id.matches("ST[0-9]{6}")) {
            throw new IllegalArgumentException("L'identificador de l'estació ha de seguir el patró 'STxxxxxx' (6 dígits).");
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
        StationID stationID = (StationID) o;
        return id.equals(stationID.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "StationID{" +
                "id='" + id + '\'' +
                '}';
    }
}

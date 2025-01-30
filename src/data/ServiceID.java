package data;

import data.exceptions.NullArgumentException;

/**
 * Classe que representa l'identificador d'un servei.
 */
final public class ServiceID {
    private final String id;

    public ServiceID(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new NullArgumentException("L'identificador del servei no pot ser nul o buit.");
        }
        if (!id.matches("SE[0-9]{6}")) {
            throw new IllegalArgumentException("L'identificador del servei ha de seguir el patró 'SExxxxxx' (6 dígits).");
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
        ServiceID serviceID = (ServiceID) o;
        return id.equals(serviceID.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "ServiceID{" +
                "id='" + id + '\'' +
                '}';
    }

    public boolean checkID() {
        return id.matches("SE[0-9]{6}");
    }
}

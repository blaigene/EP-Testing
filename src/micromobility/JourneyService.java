package micromobility;

import micromobility.exceptions.*;

import java.math.BigDecimal;
import java.security.Provider;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import data.*;

/**
 * Classe que representa el servei de trajectes.
 */
public class JourneyService {
    private LocalDateTime initDateTime;
    private int duration; // en minuts
    private float distance; // en quilòmetres
    private float avgSpeed; // en km/h
    private GeographicPoint originPoint;
    private GeographicPoint endPoint;
    private LocalDateTime endDateTime;
    private long importCost;
    private final ServiceID serviceID;
    private boolean inProgress;
    private UserAccount userAccount;
    private VehicleID vehicleID;

    public JourneyService(ServiceID serviceID) {

        // Validació del format de serviceID
        if (!serviceID.checkID()) {
            throw new IllegalArgumentException("L'identificador del servei ha de seguir el patró 'SExxxxxx' (6 dígits).");
        }

        this.serviceID = serviceID;
        setInitDateTime();
    }

    public ServiceID getServiceID() {
        return serviceID;
    }

    public LocalDateTime getInitDateTime() {
        return initDateTime;
    }

    public int getDuration() {
        return duration;
    }

    public float getDistance() {
        return distance;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public GeographicPoint getOriginPoint() {
        return originPoint;
    }

    public GeographicPoint getEndPoint() {
        return endPoint;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public long getImportCost() {
        return importCost;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public VehicleID getVehicleID() {
        return vehicleID;
    }

    public void setServiceInit() {
        this.inProgress = true;
    }

    public void setServiceFinish() {
        this.inProgress = false;
    }

    public void setInitDateTime() {
        this.initDateTime = LocalDateTime.now();
    }

    public void setEndDateTime() {
        LocalDateTime provDate = LocalDateTime.now();
        if (initDateTime != null && provDate.isBefore(initDateTime)) {
            throw new DataInconsistencyException("La data de finalització ha de ser posterior o igual a la data d'inici.");
        }
        this.endDateTime = provDate;
    }

    public void setEndDateTime(LocalDateTime date) {
        this.endDateTime = date;
    }

    public void setDuration() {
        if (initDateTime == null || endDateTime == null) {
            throw new DataInconsistencyException("Les dades d'inici i finalització han d'estar configurades abans de calcular la duració.");
        }

        if (endDateTime.isBefore(initDateTime)) {
            throw new DataInconsistencyException("La data i hora de finalització han de ser posteriors a la data i hora d'inici.");
        }

        this.duration = (int) ChronoUnit.MINUTES.between(initDateTime, endDateTime);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDistance(float distance) {
        if (distance < 0) {
            throw new NegativeDistanceException("La distància no pot ser negativa.");
        }
        this.distance = distance;
    }

    public void setAvgSpeed() {
        if (distance <= 0) {
            throw new NegativeDistanceException("La distància ha de ser superior a 0 per calcular la velocitat mitjana.");
        }
        if (duration <= 0) {
            throw new NegativeDurationException("La duració ha de ser superior a 0 per calcular la velocitat mitjana.");
        }

        // Convertir la duració de minuts a hores per calcular la velocitat mitjana
        float durationInHours = (float) duration / 60;

        // Calcular la velocitat mitjana
        this.avgSpeed = distance / durationInHours;

        if (this.avgSpeed < 0) {
            throw new NegativeAvgSpeedException("La velocitat mitjana no pot ser negativa.");
        }
    }

    public void setOriginPoint(GeographicPoint originPoint) {
        if (originPoint == null) {
            throw new IllegalArgumentException("El punt d'origen no pot ser nul.");
        }
        this.originPoint = originPoint;
    }

    public void setEndPoint(GeographicPoint endPoint) {
        if (endPoint == null) {
            throw new IllegalArgumentException("El punt de destinació no pot ser nul.");
        }
        if (originPoint != null && originPoint.equals(endPoint)) {
            throw new EqualInitAndEndPointException("El punt d'origen no pot ser igual al punt de destinació.");
        }
        this.endPoint = endPoint;
    }

    public void setImportCost(long importCost) {
        if (importCost < 0) {
            throw new NegativeImportCostException("El cost d'importació no pot ser negatiu.");
        }
        this.importCost = importCost;
    }

    public void setUserAccount(UserAccount userAccount) {
        if (userAccount == null) {
            throw new NullArgumentException("L'usuari no pot ser nul.");
        }
        this.userAccount = userAccount;
    }

    public void setVehicleID(VehicleID vehicleID) {
        if (vehicleID == null) {
            throw new NullArgumentException("El vehicle no pot ser nul.");
        }
        this.vehicleID = vehicleID;
    }

    @Override
    public String toString() {
        return "JourneyService{" +
                ", initDate=" + initDateTime +
                ", duration=" + duration +
                ", distance=" + distance +
                ", avgSpeed=" + avgSpeed +
                ", originPoint='" + originPoint + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", endDate=" + endDateTime +
                ", importCost=" + importCost +
                ", serviceID='" + serviceID + '\'' +
                ", inProgress=" + inProgress +
                ", username='" + userAccount + '\'' +
                ", vehicleID='" + vehicleID + '\'' +
                '}';
    }
}



package micromobility;

import data.*;
import micromobility.exceptions.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Classe que representa el servei de trajectes.
 */
public class JourneyService {
    private final String journeyId;
    private LocalDate initDate;
    private LocalTime initHour;
    public int duration; // en minuts
    private double distance; // en quilòmetres
    private double avgSpeed; // en km/h
    private GeographicPoint originPoint;
    private GeographicPoint endPoint;
    private LocalDate endDate;
    private LocalTime endHour;
    private double importCost;
    private final String serviceID;
    private boolean inProgress;
    private UserAccount userAccount;
    private VehicleID vehicleID;

    public JourneyService(String journeyId, String serviceID) {
        // Validació del format de journeyID
        if (!journeyId.matches("J[0-9]{6}")) {
            throw new IllegalArgumentException("L'identificador del viatje ha de seguir el patró 'Jxxxxxx' (6 dígits).");
        }

        // Validació del format de serviceID
        if (!serviceID.matches("S[0-9]{6}")) {
            throw new IllegalArgumentException("L'identificador del servei ha de seguir el patró 'Sxxxxxx' (6 dígits).");
        }

        this.journeyId = journeyId;
        this.serviceID = serviceID;
        setInitDate();
        setInitHour();
    }

    public String getJourneyId() {
        return journeyId;
    }

    public String getServiceID() {
        return serviceID;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public LocalTime getInitHour() {
        return initHour;
    }

    public int getDuration() {
        return duration;
    }

    public double getDistance() {
        return distance;
    }

    public double getAvgSpeed() {
        return avgSpeed;
    }

    public GeographicPoint getOriginPoint() {
        return originPoint;
    }

    public GeographicPoint getEndPoint() {
        return endPoint;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalTime getEndHour() {
        return endHour;
    }

    public double getImportCost() {
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

    public void setInitDate() {
        this.initDate = LocalDate.now();
    }

    public void setInitHour() {
        this.initHour = LocalTime.now();
    }

    public void setEndDate() {
        LocalDate provDate = LocalDate.now();
        if (initDate != null && provDate.isBefore(initDate)) {
            throw new DataInconsistencyException("La data de finalització ha de ser posterior o igual a la data d'inici.");
        }
        this.endDate = provDate;
    }

    public void setEndHour() {
        LocalTime provHour = LocalTime.now();
        if (endDate != null && initDate != null && initHour != null
                && endDate.equals(initDate) && provHour.isBefore(initHour)) {
            throw new DataInconsistencyException("L'hora de finalització ha de ser posterior a l'hora d'inici.");
        }
        this.endHour = provHour;
        setDuration((int) minutesBetween);
    }

    public void setDuration(int minutesBetween) {
        if (initDate == null || initHour == null || endDate == null || endHour == null) {
            throw new DataInconsistencyException("Les dades d'inici i finalització han d'estar configurades abans de calcular la duració.");
        }

        LocalDateTime startDateTime = LocalDateTime.of(initDate, initHour);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endHour);

        if (endDateTime.isBefore(startDateTime)) {
            throw new DataInconsistencyException("La data i hora de finalització han de ser posteriors a la data i hora d'inici.");
        }

        this.duration = (int) ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    public void setDistance(double distance) {
        if (distance < 0) {
            throw new NegativeDistanceException("La distància no pot ser negativa.");
        }
        this.distance = distance;
        setAvgSpeed(distance / (minutesBetween / 60.0));
    }

    public void setAvgSpeed(double v) {
        if (distance <= 0) {
            throw new NegativeDistanceException("La distància ha de ser superior a 0 per calcular la velocitat mitjana.");
        }
        if (duration <= 0) {
            throw new NegativeDurationException("La duració ha de ser superior a 0 per calcular la velocitat mitjana.");
        }

        // Convertir la duració de minuts a hores per calcular la velocitat mitjana
        double durationInHours = duration / 60.0;

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

    public void setImportCost(double importCost) {
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
                "journeyId='" + journeyId + '\'' +
                ", initDate=" + initDate +
                ", initHour=" + initHour +
                ", duration=" + duration +
                ", distance=" + distance +
                ", avgSpeed=" + avgSpeed +
                ", originPoint='" + originPoint + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", endDate=" + endDate +
                ", endHour=" + endHour +
                ", importCost=" + importCost +
                ", serviceID='" + serviceID + '\'' +
                ", inProgress=" + inProgress +
                ", username='" + userAccount + '\'' +
                ", vehicleID='" + vehicleID + '\'' +
                '}';
    }
}



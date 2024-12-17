package micromobility;

import exceptions.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Classe que representa el servei de trajectes.
 */
public class JourneyService {
    private String journeyId;
    private LocalDate initDate;
    private LocalTime initHour;
    private int duration; // en minuts
    private double distance; // en quilòmetres
    private double avgSpeed; // en km/h
    private String originPoint;
    private String endPoint;
    private LocalDate endDate;
    private LocalTime endHour;
    private double importCost;
    private String serviceID;
    private boolean inProgress;
    private String username;
    private String vehicleID;

    public JourneyService(String journeyId, LocalDate initDate, LocalTime initHour, int duration, double distance,
                          double avgSpeed, String originPoint, String endPoint, LocalDate endDate, LocalTime endHour,
                          double importCost, String serviceID, boolean inProgress, String username, String vehicleID) {
        // Validació de camps que no poden ser nuls
        if (journeyId == null || initDate == null || initHour == null || originPoint == null || endPoint == null || endDate == null
                || endHour == null || serviceID == null || username == null || vehicleID == null || journeyId.isEmpty()
                || originPoint.isEmpty() || endPoint.isEmpty() || serviceID.isEmpty() || username.isEmpty() || vehicleID.isEmpty()) {
            throw new NullArgumentException("Els paràmetres no poden ser nuls o buits");
        }

        // Validació del format de journeyID
        if (!journeyId.matches("J[0-9]{6}")) {
            throw new IllegalArgumentException("L'identificador del viatje ha de seguir el patró 'Jxxxxxx' (6 dígits).");
        }

        // Validació del format de journeyID
        if (!serviceID.matches("S[0-9]{6}")) {
            throw new IllegalArgumentException("L'identificador del servei ha de seguir el patró 'Sxxxxxx' (6 dígits).");
        }

        // Validació dels valors que no poden ser negatius
        if (duration < 0) {
            throw new NegativeDurationException("La duració no pot ser negativa");
        }
        if (distance < 0) {
            throw new NegativeDistanceException("La distància no pot ser negativa");
        }
        if (avgSpeed < 0) {
            throw new NegativeAvgSpeedException("La velocitat mitjana no pot ser negativa");
        }
        if (importCost < 0) {
            throw new NegativeImportCostException("El cost d'importació no pot ser negatiu");
        }
        // Validació de punts d'origen i destinació
        if (originPoint.equals(endPoint)) {
            throw new EqualInitAndEndPointException("El punt d'origen no pot ser igual al punt de destinació");
        }

        // Validació de coherència de dates i hores: Inici no pot ser posterior a final
        if (endDate.isBefore(initDate) || (endDate.equals(initDate) && endHour.isBefore(initHour))) {
            throw new DataInconsistencyException("La data i hora de finalització han de ser posteriors a la d'inici");
        }

        // Validació de consistència del camp inProgress
        if (!inProgress && (endDate == null || endHour == null)) {
            throw new ProgressInconsistencyException("Un viatge complet ha de tenir data i hora de finalització");
        }

        // Validació del format de username i vehicleID
        if (!username.matches("[a-zA-Z0-9._-]{15}")) {
            throw new IllegalArgumentException("El nom d'usuari ha de tenir 15 caràcters (lletres, números, '.', '-', '_').");
        }

        // Validació del format de vehicleID
        if (!vehicleID.matches("VH[0-9]{6}")) {
            throw new IllegalArgumentException("L'identificador del vehicle ha de seguir el patró 'VHxxxxxx' (6 dígits).");
        }
        this.journeyId = journeyId;
        this.initDate = initDate;
        this.initHour = initHour;
        this.duration = duration;
        this.distance = distance;
        this.avgSpeed = avgSpeed;
        this.originPoint = originPoint;
        this.endPoint = endPoint;
        this.endDate = endDate;
        this.endHour = endHour;
        this.importCost = importCost;
        this.serviceID = serviceID;
        this.inProgress = inProgress;
        this.username = username;
        this.vehicleID = vehicleID;
    }

    public String getJourneyId() {
        return journeyId;
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

    public String getOriginPoint() {
        return originPoint;
    }

    public String getEndPoint() {
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

    public String getServiceID() {
        return serviceID;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public String getUsername() {
        return username;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setServiceInit(LocalDate initDate, LocalTime initHour) {
        this.initDate = initDate;
        this.initHour = initHour;
        this.inProgress = true;
    }

    public void setServiceFinish(LocalDate endDate, LocalTime endHour) {
        this.endDate = endDate;
        this.endHour = endHour;
        this.inProgress = false;
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
                ", username='" + username + '\'' +
                ", vehicleID='" + vehicleID + '\'' +
                '}';
    }
}



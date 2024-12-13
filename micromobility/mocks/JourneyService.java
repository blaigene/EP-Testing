package micromobility.mocks;

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
    }

    public void setServiceFinish(LocalDate endDate, LocalTime endHour) {
        this.endDate = endDate;
        this.endHour = endHour;
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



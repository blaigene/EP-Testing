package micromobility;

import java.time.LocalDateTime;

/**
 * Classe que representa el servei de trajectes.
 */
public class JourneyService {
    // SERVEIS IMPLICITS
    // MODEL DEL DOMINI, ATRIBUTS DE LA CLASSE, ALGUN NO IMPLICITATS, REVISAR CLASSE ASOCITIVA, DRIVER, PMVehicle (NOMÉS EL ID, USER ACCOUNT), PER PARTT 2, SERVICE ID
    private String journeyId;
    private LocalDateTime serviceInit;
    private LocalDateTime serviceFinish;

    public JourneyService(String journeyId) {
        this.journeyId = journeyId;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public LocalDateTime getServiceInit() {
        return serviceInit;
    }

    public LocalDateTime getServiceFinish() {
        return serviceFinish;
    }

    public void setServiceInit(LocalDateTime serviceInit) {
        this.serviceInit = serviceInit;
    }

    public void setServiceFinish(LocalDateTime serviceFinish) {
        this.serviceFinish = serviceFinish;
    }

    @Override
    public String toString() {
        return "JourneyService{journeyId='" + journeyId + "', serviceInit=" + serviceInit +
                ", serviceFinish=" + serviceFinish + "}";
    }
}


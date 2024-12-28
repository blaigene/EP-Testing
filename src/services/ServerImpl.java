package services;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import services.exceptions.*;
import micromobility.JourneyService;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ServerImpl implements Server {

    // Simulació d'una base de dades en memòria per emmagatzemar l'estat dels vehicles i els emparellaments.
    public Map<VehicleID, Boolean> vehicleAvailability = new HashMap<>();
    public Map<VehicleID, UserAccount> activePairings = new HashMap<>();
    public Map<StationID, GeographicPoint> stationGPs = new HashMap<>();
    public Map<StationID, VehicleID> vehiclesInStation = new HashMap<>();

    @Override
    public void checkPMVAvail(VehicleID vhID) throws PMVNotAvailException, ConnectException {
        if (!vehicleAvailability.containsKey(vhID)) {
            throw new ConnectException("No s'ha pogut connectar amb el servidor per verificar la disponibilitat del vehicle.");
        }

        // Verifiquem si el vehicle ja està emparellat amb un altre usuari
        if (activePairings.containsKey(vhID)) {
            throw new PMVNotAvailException("El vehicle està emparellat amb un altre usuari.");
        }
    }

    @Override
    public void registerPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date) throws InvalidPairingArgsException, ConnectException {
        // Verificar si el vehicle està disponible
        if (!vehicleAvailability.containsKey(veh)) {
            throw new ConnectException("No s'ha pogut connectar amb el servidor per registrar l'emparellament.");
        }

        if (activePairings.containsKey(veh)) {
            throw new InvalidPairingArgsException("El vehicle ja està emparellat amb un altre usuari.");
        }

        if (!stationGPs.get(st).equals(loc)) {
            throw new InvalidPairingArgsException("L'ubicació facilitada no correspon amb l'ubicació de l'estació.");
        }

        if (!vehiclesInStation.get(st).equals(veh)) {
            throw new InvalidPairingArgsException("El vehicle facilitat no es troba en aquesta estació.");
        }

        setPairing(user, veh, st, loc, date);
    }

    @Override
    public void stopPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date, float avSp, float dist, int dur, BigDecimal imp) throws InvalidPairingArgsException, ConnectException, PairingNotFoundException {
        // Verificar si el vehicle està emparellat amb aquest usuari
        if (!activePairings.containsKey(veh) || !activePairings.get(veh).equals(user)) {
            throw new InvalidPairingArgsException("No es pot aturar l'emparellament perquè no s'ha trobat el vehicle emparellat amb aquest usuari.");
        }

        if (!vehicleAvailability.containsKey(veh)) {
            throw new ConnectException("No s'ha pogut connectar amb el servidor per registrar l'emparellament.");
        }

        // En aquest punt es registra la finalització del servei (guardar-ho a una base de dades).
        JourneyService journeyService = new JourneyService("J000000", "S000000");
        journeyService.setUserAccount(user);
        journeyService.setVehicleID(veh);
        journeyService.setEndDate();
        journeyService.setDuration(dur);
        journeyService.setDistance(dist);
        journeyService.setImportCost(imp.doubleValue());

        unPairRegisterService(journeyService);
    }

    @Override
    public void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date) {
        activePairings.put(veh, user);  // Registra l'emparellament del vehicle amb l'usuari
        vehicleAvailability.put(veh, false); // El vehicle deixa d'estar disponible
    }

    @Override
    public void unPairRegisterService(JourneyService s) throws PairingNotFoundException {

        // Aquest mètode gestiona el registre del servei i desvinculació al final d'un viatge.

        VehicleID veh = s.getVehicleID();

        if (!activePairings.containsKey(veh)) {
            throw new PairingNotFoundException("No s'ha trobat l'emparellament per al servei proporcionat.");
        }

        // Desemparellar el vehicle i registrar el servei
        activePairings.remove(veh);
        vehicleAvailability.put(veh, true);
    }

    @Override
    public void registerLocation(VehicleID veh, StationID st) {
        // Aquest mètode registra la nova ubicació del vehicle.
        vehiclesInStation.put(st, veh);
    }
}

package services;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import exceptions.InvalidPairingArgsException;
import exceptions.PMVNotAvailException;
import exceptions.PairingNotFoundException;
import micromobility.JourneyService;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ServerImpl implements Server{

    // Simulació d'una base de dades en memòria per emmagatzemar l'estat dels vehicles i els emparellaments.
    private Map<VehicleID, Boolean> vehicleAvailability = new HashMap<>();
    private Map<VehicleID, UserAccount> activePairings = new HashMap<>();
    private Map<StationID, GeographicPoint> stationGPs = new HashMap<>();
    private Map<StationID, VehicleID> vehiclesInStation = new HashMap<>();

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


        activePairings.put(veh, user);  // Registra l'emparellament del vehicle amb l'usuari
        vehicleAvailability.put(veh, false); // El vehicle deixa d'estar disponible
    }

    @Override
    public void stopPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date, float avSp, float dist, int dur, BigDecimal imp) throws InvalidPairingArgsException, ConnectException {
        // Verificar si el vehicle està emparellat amb aquest usuari
        if (!activePairings.containsKey(veh) || !activePairings.get(veh).equals(user)) {
            throw new InvalidPairingArgsException("No es pot aturar l'emparellament perquè no s'ha trobat el vehicle emparellat amb aquest usuari.");
        }

        // Registrar la finalització del servei (això podria implicar guardar-ho a una base de dades).
        System.out.println("Servei registrat amb èxit: ");
        System.out.println("Usuari: " + user);
        System.out.println("Vehicle: " + veh);
        System.out.println("Estació: " + st);
        System.out.println("Ubicació: " + loc);
        System.out.println("Data: " + date);
        System.out.println("Velocitat mitjana: " + avSp);
        System.out.println("Distància: " + dist);
        System.out.println("Duració: " + dur + " minuts");
        System.out.println("Import: " + imp);

        // El vehicle torna a estar disponible
        activePairings.remove(veh);
        vehicleAvailability.put(veh, true);
    }

    @Override
    public void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date) {
        // Aquest mètode sembla redundant amb registerPairing, però pot ser per forçar un emparellament.
        activePairings.put(veh, user);
        vehicleAvailability.put(veh, false);
    }

    @Override
    public void unPairRegisterService(JourneyService s) throws PairingNotFoundException {
        /*
        // Aquest mètode podria gestionar el registre del servei i desvinculació al final d'un viatge.
        String veh = s.getVehicleID();  // Suposant que JourneyService té aquest mètode
        if (!activePairings.containsKey(veh)) {
            throw new PairingNotFoundException("No s'ha trobat l'emparellament per al vehicle proporcionat.");
        }

        // Desemparellar el vehicle i registrar el servei
        activePairings.remove(veh);
        vehicleAvailability.put(veh, true);
        System.out.println("Servei desemparellat i registrat correctament.");
        */
    }

    @Override
    public void registerLocation(VehicleID veh, StationID st) {
        // Aquest mètode registra la nova ubicació del vehicle. Podries implementar lògica aquí
        // per actualitzar la base de dades o registres al servidor amb la nova estació.
        System.out.println("Ubicació del vehicle " + veh + " actualitzada a l'estació " + st);
    }
}

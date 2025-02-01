package services;

import data.*;
import micromobility.payment.Payment;
import micromobility.payment.Wallet;
import micromobility.payment.WalletPayment;
import services.exceptions.*;
import micromobility.JourneyService;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ServerImpl implements Server {

    // Simulació d'una base de dades en memòria per emmagatzemar l'estat dels vehicles i els emparellaments.
    public Map<VehicleID, Boolean> vehicleAvailability;
    public Map<VehicleID, UserAccount> activePairings;
    public Map<StationID, GeographicPoint> stationGPs;
    public Map<StationID, VehicleID> vehiclesInStation;
    public Map<ServiceID, Payment> paymentsDB;

    public ServerImpl() {
        vehicleAvailability = new HashMap<>();
        activePairings = new HashMap<>();
        stationGPs = new HashMap<>();
        vehiclesInStation = new HashMap<>();
        paymentsDB = new HashMap<>();
    }

    @Override
    public void checkPMVAvail(VehicleID vhID) throws PMVNotAvailException, ConnectException {
        // Verifiquem si el vehicle ja està emparellat amb un altre usuari
        if (vehicleAvailability.containsKey(vhID) && !vehicleAvailability.get(vhID)) {
            throw new PMVNotAvailException("El vehicle està emparellat amb un altre usuari.");
        }

        System.out.println("Vehículo disponible.");
    }

    @Override
    public void registerPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date) throws InvalidPairingArgsException, ConnectException {
        //Verifiquem paràmetres entrada.
        if (loc.getLatitude() < -90 || loc.getLatitude() > 90 || loc.getLongitude() < -180 || loc.getLongitude() > 180) {
            throw new InvalidPairingArgsException("La localització es incorrecta.");
        }

        setPairing(user, veh, st, loc);
        System.out.println("Emparejamiento registrado.");
    }

    @Override
    public void stopPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date, double avSp, double dist, int dur, BigDecimal imp) throws InvalidPairingArgsException, ConnectException, PairingNotFoundException {
        // Verificar si el vehicle està emparellat amb aquest usuari
        if (!vehicleAvailability.containsKey(veh)) {
            throw new ConnectException("No s'ha pogut connectar amb el servidor per registrar l'emparellament.");
        }

        if (!activePairings.containsKey(veh) || !activePairings.get(veh).equals(user)) {
            throw new InvalidPairingArgsException("No es pot aturar l'emparellament perquè no s'ha trobat el vehicle emparellat amb aquest usuari.");
        }

        // En aquest punt es registra la finalització del servei (guardar-ho a una base de dades).
        JourneyService journeyService = new JourneyService(new ServiceID("SE000000"));
        journeyService.setUserAccount(user);
        journeyService.setVehicleID(veh);
        journeyService.setEndDateTime();
        journeyService.setDuration(dur);
        journeyService.setDistance((float) dist);
        journeyService.setImportCost(imp.longValue());

        unPairRegisterService(journeyService);
        registerLocation(veh, st);
        System.out.println("Emparejamiento finalizado.");
    }

    @Override
    public void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc) {
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

    public void registerPayment(ServiceID servID, UserAccount user, BigDecimal imp, char payMeth) throws
            ConnectException, MethodNotSupportedException {
        // Comprovar si existeix el servei
        if (servID == null || user == null) {
            throw new ConnectException("No s'ha pogut connectar amb el servidor per enregistrar el pagament.");
        }

        if (payMeth == 'W') { // Wallet
            Wallet fakeWallet = new Wallet(new BigDecimal(200));
            WalletPayment payment = new WalletPayment(servID, user, imp, fakeWallet);
            paymentsDB.put(servID, payment);
        } else {
            throw new MethodNotSupportedException("Mètode no implementat.");
        }
    }
}

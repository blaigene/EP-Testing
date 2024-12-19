package services;
//External services involved in the shared micromobility system
// TREURE HASHMAP
// FER DOBLES O MÉS SIMPLE POSSIBLE
import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
// FER EL PAQUET D'EXCEPCIONS DE SERVICES I CANVIAR PER EL DE MICROMOBILITY
import micromobility.exceptions.*;
import micromobility.JourneyService;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;

public interface Server { // External service for the persistent storage

    // To be invoked by the use case controller

    void checkPMVAvail(VehicleID vhID)
            throws PMVNotAvailException, ConnectException;

    void registerPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date)
            throws InvalidPairingArgsException, ConnectException;

    void stopPairing(UserAccount user, VehicleID veh, StationID st,
                     GeographicPoint loc, LocalDateTime date, float avSp, float dist, int dur, BigDecimal imp)
            throws InvalidPairingArgsException, ConnectException;

    // Internal operations

    void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date);

    void unPairRegisterService(JourneyService s)
            throws PairingNotFoundException;
    void registerLocation(VehicleID veh, StationID st);

}
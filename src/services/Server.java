package services;

import data.GeographicPoint;
import data.StationID;
import data.UserAccount;
import data.VehicleID;
import micromobility.JourneyService;
import services.exceptions.*;

import java.math.BigDecimal;
import java.net.ConnectException;
import java.time.LocalDateTime;

public interface Server { // External service for the persistent storage

    // To be invoked by the use case controller

    boolean checkPMVAvail(VehicleID vhID)
            throws PMVNotAvailException, ConnectException;

    void registerPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date)
            throws InvalidPairingArgsException, ConnectException;

    void stopPairing(UserAccount user, VehicleID veh, StationID st,
                     GeographicPoint loc, LocalDateTime date, double avSp, double dist, int dur, BigDecimal imp)
            throws InvalidPairingArgsException, ConnectException;

    // Internal operations

    void setPairing(UserAccount user, VehicleID veh, StationID st, GeographicPoint loc, LocalDateTime date);

    void unPairRegisterService(JourneyService s)
            throws PairingNotFoundException;
    void registerLocation(VehicleID veh, StationID st);

    boolean isConnected();

    void broadcastStationID(StationID stationID);

    void startJourney(VehicleID vehicleID, String serviceID);

    void endJourney(VehicleID vehicleID, String serviceID, GeographicPoint endPoint, LocalDateTime endDate, float avgSpeed, float distance, int duration, BigDecimal importCost);
}
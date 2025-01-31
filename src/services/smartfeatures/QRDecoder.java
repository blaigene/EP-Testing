package services.smartfeatures;

import data.VehicleID;
import services.exceptions.*;

import java.awt.image.BufferedImage;

public interface QRDecoder {

  VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException;

  void setCorrupted(boolean isCorrupted);
}
package services.smartfeatures;

import data.VehicleID;
// FER EL PAQUET D'EXCEPCIONS DE SERVICES I CANVIAR PER EL DE MICROMOBILITY
import micromobility.exceptions.*;

import java.awt.image.BufferedImage;

public interface QRDecoder {
  VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException;

  String decodeQR(String qrCode);
}
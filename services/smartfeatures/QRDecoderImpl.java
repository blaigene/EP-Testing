package services.smartfeatures;

import data.VehicleID;
import micromobility.exceptions.CorruptedImgException;

import java.awt.image.BufferedImage;

public class QRDecoderImpl implements QRDecoder {
    @Override
    public VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException {
        return null;
    }
}

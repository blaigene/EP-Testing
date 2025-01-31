package services.smartfeatures;

import data.VehicleID;
import services.exceptions.*;

import java.awt.image.BufferedImage;

public class QRDecoderImpl implements QRDecoder {

    private boolean isCorrupted;

    public QRDecoderImpl() {
        isCorrupted = false;
    }

    @Override
    public VehicleID getVehicleID(BufferedImage QRImg) throws CorruptedImgException {
        if (isCorrupted) {
            throw new CorruptedImgException("L'imatge del QR està danyada o no és valida.");
        }
        return new VehicleID("VH123456");
    }

    @Override
    public void setCorrupted(boolean isCorrupted) {
        this.isCorrupted = isCorrupted;
    }
}

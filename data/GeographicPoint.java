package data;

/**
* Essential data classes
*/
final public class GeographicPoint {
    // The geographical coordinates expressed as decimal degrees
    private final float latitude;
    private final float longitude;
    public GeographicPoint (float lat, float lon) {
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("La latitud ha d'estar entre -90 i 90 graus.");
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("La longitud ha d'estar entre -180 i 180 graus.");
        }
        this.latitude = lat;
        this.longitude = lon;
    }

    public float getLatitude () { return latitude; }
    public float getLongitude () { return longitude; }

    @Override
    public boolean equals (Object o) {
        boolean eq;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeographicPoint gP = (GeographicPoint) o;
        eq = ((latitude == gP.latitude) && (longitude == gP.longitude));
        return eq;
    }

    @Override
    public int hashCode () {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(latitude);
        result = prime * result + Float.floatToIntBits(longitude);
        return result;
    }

    @Override
    public String toString () {
        return "Geographic point {" + "latitude='" + latitude + '\'' +
        ",longitude='" + longitude + '\'' +'}';
    }
}
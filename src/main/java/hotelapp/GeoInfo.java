package hotelapp;

public class GeoInfo {
    private double lat;
    private double lng;

    public GeoInfo(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }

    public String toString() {
        return lat + ", " + lng;
    }
}

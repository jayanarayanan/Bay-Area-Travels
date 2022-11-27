package hotelapp;

public class GeoInfo {
    private double lat;
    private double lng;

    public GeoInfo(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return String.valueOf(lat);
    }
    public String getLng() {
        return String.valueOf(lng);
    }

    public String toString() {
        return lat + ", " + lng;
    }
}

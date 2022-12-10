package hotelapp;

import com.google.gson.annotations.SerializedName;

//stores the details of the hotel, and has the methods to search for a hotel given the hotel ID, & display the contents of a Hotel object.
public class Hotel {
    @SerializedName(value = "f")
    private String hotelName;
    @SerializedName(value = "id")
    private String hotelID;
    @SerializedName(value = "ll")
    private GeoInfo coordinates;
    @SerializedName(value = "ad")
    private String address;
    @SerializedName(value = "ci")
    private String city;
    @SerializedName(value = "pr")
    private String state;

    //constructor for class Hotel
    public Hotel(String hotelName, String hotelID, String address, String city, String state, GeoInfo coordinates) {
        this.hotelName = hotelName;
        this.hotelID = hotelID;
        this.coordinates = coordinates;
        this.address = address;
        this.city = city;
        this.state = state;
    }

    //checks if this object's hotelId matches the given hotelID, returns a boolean value
    public boolean searchHotel(String hotelID) {
        if(this.hotelID.equals(hotelID)) {
            return true;
        }
        return false;
    }
    //getter function for the hotel ID, returns the hotelID of this object.
    public String getHotelID() {
        return this.hotelID;
    }
    public String getHotelName() {
        return this.hotelName;
    }
    public String getHotelAddress() {
        return this.address;
    }
    public String getHotelCity() {
        return this.city;
    }
    public String getHotelState() {
        return this.state;
    }
    public double getHotelLat() {
        return coordinates.getLat();
    }
    public double getHotelLng() {
        return coordinates.getLng();
    }

    public GeoInfo getCoordinates() {
        return coordinates;
    }

    //displays the contents of a Hotel object
    public String toString() {
        return System.lineSeparator() + "********************" +
                System.lineSeparator() + hotelName + ": " + hotelID +
                System.lineSeparator() + address +
                System.lineSeparator() + city + ", " + state;
    }
}

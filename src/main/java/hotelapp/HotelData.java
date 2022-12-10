package hotelapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import database.DatabaseHandler;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;
import java.util.TreeMap;

public class HotelData {

    //TreeMap for storing all the hotels details, key is the hotelID, value is the hotel object with that hotelID.
    private TreeMap<String, Hotel> hotelMap = new TreeMap<>();

    public HotelData() {}

    //Adds the list of hotels to the hotelMap hashmap
    public void addHotels(String hotelPath) {
//        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        ArrayList<Hotel> hotels = HotelParser.readHotel(hotelPath);
        for (Hotel h : hotels) {
            hotelMap.put(h.getHotelID(), h);
//            dbHandler.addHotelsToDB(h.getHotelID(), h.getHotelName(), h.getHotelAddress(), h.getHotelCity(), h.getHotelState(), h.getCoordinates());
        }
    }

    //getter function for the hotel object, returns the hotel object with the given hotelID.
    public Hotel getHotelObject(String hotelID) {
        return hotelMap.get(hotelID);
    }
    public String getExpediaLink(String hotelID) {
        return "https://www.expedia.com/h" + hotelMap.get(hotelID).getHotelID() + ".Hotel-Information";
    }

    //getter function for the hotel map keyset, returns the hotelMap keyset.
    public Set<String> getHotelKeySet() {
        return hotelMap.keySet();
    }

    public double getLatString(String hotelID) {
        return hotelMap.get(hotelID).getHotelLat();
    }
    public double getLngString(String hotelID) {
        return hotelMap.get(hotelID).getHotelLng();
    }

    //Returns the json object containing the hotel details.
    public JsonObject printHotel(String hotelID) {
        JsonObject json = new JsonObject();
        if(hotelID == null || !hotelMap.containsKey(hotelID)) {
            JsonObject jsonErr = new JsonObject();
            jsonErr.addProperty("hotelId", "invalid");
            jsonErr.addProperty("success", false);
            return jsonErr;
        } else {
            String link = "https://www.expedia.com/h" + hotelMap.get(hotelID).getHotelID() + ".Hotel-Information";
            json.addProperty("success", true);
            json.addProperty("hotelId", hotelMap.get(hotelID).getHotelID());
            json.addProperty("name", hotelMap.get(hotelID).getHotelName());
            json.addProperty("addr", hotelMap.get(hotelID).getHotelAddress());
            json.addProperty("city", hotelMap.get(hotelID).getHotelCity());
            json.addProperty("state", hotelMap.get(hotelID).getHotelState());
            json.addProperty("lat", hotelMap.get(hotelID).getHotelLat());
            json.addProperty("lng", hotelMap.get(hotelID).getHotelLng());
            json.addProperty("link", link);
            return json;
        }
    }
//    public ArrayList<Hotel> findHotel(String word) {
//        ArrayList<Hotel> hotelArr = new ArrayList<>();
//        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
//        ArrayList<Hotel> hotels = dbHandler.searchHotelsInDB(word);
//        if(word == null || word.isEmpty()) {
//            return hotels;
//        } else {
//            String wordLC = word.toLowerCase(Locale.ROOT);
//            for(Hotel h : hotels) {
//                String hName = h.getHotelName().toLowerCase();
//                if(hName.contains(wordLC)) {
//                    hotelArr.add(h);
//                }
//            }
//            if(hotelArr.size() > 0) {
//                return hotelArr;
//            } else {
//                return hotels;
//            }
//        }
//    }

    //Writes the hotel details of a given hotelID into the specified output file.
    public void writeHotelToFile(String output) {
        try(PrintWriter writer = new PrintWriter(output)) {
            for(String i : hotelMap.keySet()) {
                writer.println(hotelMap.get(i));
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

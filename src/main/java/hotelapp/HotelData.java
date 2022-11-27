package hotelapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

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
        ArrayList<Hotel> hotels = HotelParser.readHotel(hotelPath);
        for (Hotel h : hotels) {
            hotelMap.put(h.getHotelID(), h);
        }
    }

    //getter function for the hotel object, returns the hotel object with the given hotelID.
    public Hotel getHotelObject(String hotelID) {
        return hotelMap.get(hotelID);
    }

    //getter function for the hotel map keyset, returns the hotelMap keyset.
    public Set<String> getHotelKeySet() {
        return hotelMap.keySet();
    }

    public String getLatString(String hotelID) {
        return hotelMap.get(hotelID).getHotelLat();
    }
    public String getLngString(String hotelID) {
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
    public JsonObject findHotel(String word) {
        JsonObject json = new JsonObject();
        JsonArray jsonArr = new JsonArray();
        if(word == null || word.isEmpty()) {
            json.addProperty("success", true);
            for(Hotel h : hotelMap.values()) {
                JsonObject jsonTemp = new JsonObject();
                jsonTemp.addProperty("hotelId", h.getHotelID());
                jsonTemp.addProperty("name", h.getHotelName());
                jsonTemp.addProperty("addr", h.getHotelAddress());
                jsonTemp.addProperty("city", h.getHotelCity());
                jsonTemp.addProperty("state", h.getHotelState());
                jsonTemp.addProperty("lat", h.getHotelLat());
                jsonTemp.addProperty("lng", h.getHotelLng());
                jsonArr.add(jsonTemp);
            }
            json.add("Hotels", jsonArr);
            return json;
        } else {
            String wordLC = word.toLowerCase(Locale.ROOT);
            json.addProperty("success", true);
            for(Hotel h : hotelMap.values()) {
                String hName = h.getHotelName().toLowerCase();
                if(hName.contains(wordLC)) {
                    JsonObject jsonTemp = new JsonObject();
                    jsonTemp.addProperty("hotelId", h.getHotelID());
                    jsonTemp.addProperty("name", h.getHotelName());
                    jsonTemp.addProperty("addr", h.getHotelAddress());
                    jsonTemp.addProperty("city", h.getHotelCity());
                    jsonTemp.addProperty("state", h.getHotelState());
                    jsonTemp.addProperty("lat", h.getHotelLat());
                    jsonTemp.addProperty("lng", h.getHotelLng());
                    jsonArr.add(jsonTemp);
                }
            }
            if(jsonArr.size() > 0) {
                json.add("Hotels", jsonArr);
                return json;
            } else {
                JsonObject jsonErr = new JsonObject();
                jsonErr.addProperty("hotel", "Not Found");
                jsonErr.addProperty("success", false);
                return jsonErr;
            }
        }
    }

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

package hotelapp;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class HotelParser{
    //parses the hotel details from the json file, returns the arraylist of hotel objects.
    public static ArrayList<Hotel> readHotel(String hotelPath) {
        Gson gson = new Gson();

        try (FileReader br = new FileReader(hotelPath)) {
            JsonParser parser = new JsonParser();
            JsonObject jo = (JsonObject) parser.parse(br);
            JsonArray jsonArr = jo.getAsJsonArray("sr");
            Type hotelType = new TypeToken<ArrayList<Hotel>>(){}.getType();
            ArrayList<Hotel> hotels = gson.fromJson(jsonArr, hotelType);
            return hotels;
        } catch (IOException e) {
            System.out.println("Could not read the file: " + e);
            return null;
        }
    }
}

package hotelapp;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.URL;

public class WeatherData {

    //sends a request to the weather api for the location with the given coordinates, and converts the response into a json object, and returns that object.
    public static JsonObject callWeatherAPI(String lat, String lng) {
        String s = "";
        String urlString = "https://api.open-meteo.com/v1/forecast?latitude=" + lat + "&longitude=" + lng + "&daily=temperature_2m_max,temperature_2m_min,sunrise,sunset,windspeed_10m_max&current_weather=true&timezone=America%2FLos_Angeles";
        PrintWriter out = null;
        BufferedReader in = null;
        SSLSocket socket = null;
        JsonObject weatherData = new JsonObject();
        try {
            URL url = new URL(urlString);

            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            socket = (SSLSocket) factory.createSocket(url.getHost(), 443);

            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String request = getRequest(url.getHost(), url.getPath() + "?"+ url.getQuery());

            out.println(request);
            out.flush();

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                if(line.startsWith("{")) {
                    sb.append(line);
                }
            }
            JsonParser parser = new JsonParser();
            weatherData = (JsonObject) parser.parse(sb.toString());
        } catch (IOException e) {
            System.out.println(
                    "An IOException occured while writing to the socket stream or reading from the stream: " + e);
        } finally {
            try {
                // close the streams and the socket
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                System.out.println("An exception occured while trying to close the streams or the socket: " + e);
            }
        }
        return weatherData;
    }

    //This function creates the api request packet that is sent to the api server in the callWeatherAPI function
    private static String getRequest(String host, String pathResourceQuery) {
        String request = "GET " + pathResourceQuery + " HTTP/1.1" + System.lineSeparator()
                // request
                + "Host: " + host + System.lineSeparator()
                + "Connection: close" + System.lineSeparator()
                + System.lineSeparator();
        return request;
    }

    //This function adds the weather details of the given hotel that is obtained from the above api call to the hotel details, and returns the json object storing the same.
    public JsonObject printWeather(ThreadSafeHotelData hotelData, String hotelID) {
        JsonObject json = new JsonObject();
        json = hotelData.printHotel(hotelID);
//        if(hotelID != null) {
//            json.add("weather", callWeatherAPI(hotelData.getLatString(hotelID), hotelData.getLngString(hotelID)));
//        }
        return json;
    }

    public static void main(String[] args) {
//        JsonObject jsonResult = callWeatherAPI("37.78", "-122.40");
//        System.out.println(jsonResult);
    }
}

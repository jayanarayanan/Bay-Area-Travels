package jettyServer;

import hotelapp.ThreadSafeHotelData;
import hotelapp.WeatherData;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WeatherServlet extends HttpServlet {
    private ThreadSafeHotelData hotelData;

    //constructor function
    public WeatherServlet(ThreadSafeHotelData hotelData) {
        this.hotelData = hotelData;
    }

    //this method gets executed when the get request is sent to /weather, and creates the response packet that calls printWeather() to display the hotel details, and the weather at that location in the browser.
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        // Get the word from the get request
        String hotelId = request.getParameter("hotelId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        WeatherData weather = new WeatherData();
        try{
            out.println(weather.printWeather(hotelData, hotelId));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

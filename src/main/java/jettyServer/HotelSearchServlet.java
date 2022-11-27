package jettyServer;

import hotelapp.ThreadSafeHotelData;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HotelSearchServlet extends HttpServlet {
    private ThreadSafeHotelData hotelData;

    //constructor function
    public HotelSearchServlet(ThreadSafeHotelData hotelData) {
        this.hotelData = hotelData;
    }

    //this method gets executed when the get request is sent to /hotelInfo, and creates the response packet that calls printHotel() to display the hotel details in the browser.
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        // Get the word from the get request
        String hotelName = request.getParameter("hotelName");
        hotelName = StringEscapeUtils.escapeHtml4(hotelName);
        try{
            out.println(hotelData.findHotel(hotelName));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

package jettyServer;

import hotelapp.ThreadSafeHotelData;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class HotelServlet extends HttpServlet {
    private ThreadSafeHotelData hotelData;

    //constructor function
    public HotelServlet(ThreadSafeHotelData hotelData) {
        this.hotelData = hotelData;
    }

    //this method gets executed when the get request is sent to /hotelInfo, and creates the response packet that calls printHotel() to display the hotel details in the browser.
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        // Get the word from the get request
        String hotelId = request.getParameter("hotelId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/HotelDetails.html");
        context.put("Hotels", hotelData.printHotel(hotelId));
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        try{
            out.println(writer);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

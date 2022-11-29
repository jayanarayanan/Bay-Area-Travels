package jettyServer;

import hotelapp.ThreadSafeHotelData;
import hotelapp.ThreadSafeHotelReviewData;
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

public class ReviewServlet extends HttpServlet {
    private ThreadSafeHotelReviewData reviewData;
    private ThreadSafeHotelData hotelData;

    public ReviewServlet(hotelapp.ThreadSafeHotelReviewData reviewData, ThreadSafeHotelData hotelData) {
        this.reviewData = reviewData;
        this.hotelData = hotelData;
    }

    //this method gets executed when the get request is sent to /reviews, and creates the response packet that calls printHotelReview() to display the review details of "num" number of reviews in the browser.
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Helper helper = new Helper();
        helper.notLoggedIn(request, response);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        // Get the word from the get request
        String hotelId = request.getParameter("hotelId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/HotelDetails.html");
        context.put("Hotels", hotelData.getHotelObject(hotelId));
        context.put("Elink", hotelData.getExpediaLink(hotelId));
        context.put("Reviews", reviewData.printHotelReview(hotelId));
        context.put("avgRating", reviewData.avgRating(hotelId));
        context.put("user", helper.getUser(request));
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        try{
            out.println(writer);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

package jettyServer;

import hotelapp.HotelReview;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ModifyReviewServlet extends HttpServlet {
    private ThreadSafeHotelReviewData reviewData;
    private ThreadSafeHotelData hotelData;
    public ModifyReviewServlet(hotelapp.ThreadSafeHotelReviewData reviewData, ThreadSafeHotelData hotelData) {
        this.hotelData = hotelData;
        this.reviewData = reviewData;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Helper helper = new Helper();
        helper.notLoggedIn(request, response);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        String hotelId = request.getParameter("hotelId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        String reviewId = request.getParameter("reviewId");
        reviewId = StringEscapeUtils.escapeHtml4(reviewId);
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/ModifyReview.html");
        context.put("review", reviewData.getReviewObj(hotelId, reviewId));
        context.put("hotel", hotelData.getHotelObject(hotelId));
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        try{
            out.println(writer);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        reviewData.modifyReview(request.getParameter("review-hotelId"), request.getParameter("review-reviewId"), request.getParameter("review-title"), request.getParameter("review-text"));

        response.sendRedirect("/reviews?hotelId=" + request.getParameter("review-hotelId"));
    }
}

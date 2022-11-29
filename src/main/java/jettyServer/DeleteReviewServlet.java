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

public class DeleteReviewServlet  extends HttpServlet {
    private ThreadSafeHotelReviewData reviewData;
    private ThreadSafeHotelData hotelData;
    public DeleteReviewServlet(hotelapp.ThreadSafeHotelReviewData reviewData, ThreadSafeHotelData hotelData) {
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
        reviewData.deleteReview(hotelId, reviewId);
        response.sendRedirect("/reviews?hotelId=" + hotelId);
    }
}

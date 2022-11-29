package jettyServer;

import hotelapp.HotelReview;
import hotelapp.ThreadSafeHotelData;
import hotelapp.ThreadSafeHotelReviewData;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AddReviewServlet extends HttpServlet {
    private ThreadSafeHotelReviewData reviewData;
    public AddReviewServlet(hotelapp.ThreadSafeHotelReviewData reviewData) {
        this.reviewData = reviewData;
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Helper helper = new Helper();
        helper.notLoggedIn(request, response);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        String uuid = UUID.randomUUID().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();
        String curTime = dateFormat.format(date);

        HotelReview review = new HotelReview(Integer.parseInt(request.getParameter("review-hotelId")), uuid, 0,  request.getParameter("review-title"), request.getParameter("review-text"), helper.getUser(request), curTime);
        reviewData.addReviewToMap(request.getParameter("review-hotelId"), review);

        response.sendRedirect("/reviews?hotelId=" + request.getParameter("review-hotelId"));
    }
}

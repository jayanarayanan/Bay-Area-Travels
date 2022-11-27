package jettyServer;

import hotelapp.ThreadSafeHotelData;
import hotelapp.ThreadSafeHotelReviewData;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class WordServlet extends HttpServlet {
    private ThreadSafeHotelReviewData reviewData;

    //constructor function
    public WordServlet(hotelapp.ThreadSafeHotelReviewData reviewData) {
        this.reviewData = reviewData;
    }

    //this method gets executed when the get request is sent to /index, and creates the response packet that calls findWordInHotelReview() to display the review details of "num" number of reviews that contain the given word in the browser.
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();

        // Get the word from the get request
        String word = request.getParameter("word");
        word = StringEscapeUtils.escapeHtml4(word);
        String num = request.getParameter("num");
        num = StringEscapeUtils.escapeHtml4(num);
        try{
            out.println(reviewData.findWordInHotelReview(word, num));
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

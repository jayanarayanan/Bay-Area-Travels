package jettyServer;

import database.DatabaseHandler;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AddLinkServlet extends HttpServlet {

    public AddLinkServlet() {
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Helper helper = new Helper();
        helper.notLoggedIn(request, response);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();

        // Get the word from the get request
        String link = request.getParameter("link");
        link = StringEscapeUtils.escapeHtml4(link);
        String hotelId = request.getParameter("hotelId");
        hotelId = StringEscapeUtils.escapeHtml4(hotelId);
        dbHandler.addLinkInDB(helper.getUser(request), link);
        response.sendRedirect("/reviews?hotelId=" + hotelId);
    }
}


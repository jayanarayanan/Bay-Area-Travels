package jettyServer;

import database.DatabaseHandler;
import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ClearLinksServlet extends HttpServlet {

    public ClearLinksServlet() {
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Helper helper = new Helper();
        helper.notLoggedIn(request, response);
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();

        dbHandler.removeLinksInDB(helper.getUser(request));
        response.sendRedirect("/viewLinks");
    }
}
package jettyServer;

import database.DatabaseHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Helper helper = new Helper();
        HttpSession session = request.getSession();
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();
        if(session.getAttribute("newUser") == "true") {
            dbHandler.addLoginDateInDB(helper.getUser(request), session.getAttribute("loginTime").toString());
        } else {
            dbHandler.modifyLoginDateInDB(helper.getUser(request), session.getAttribute("loginTime").toString());
        }
        helper.logout(request, response);
    }
}

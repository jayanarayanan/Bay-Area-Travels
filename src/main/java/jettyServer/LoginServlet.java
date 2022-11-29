package jettyServer;

import org.apache.commons.text.StringEscapeUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        username = StringEscapeUtils.escapeHtml4(username);
        String password = request.getParameter("password");
        password = StringEscapeUtils.escapeHtml4(password);

        String user = (String) session.getAttribute("username");
        if(user != null) {
            
        }
    }
}

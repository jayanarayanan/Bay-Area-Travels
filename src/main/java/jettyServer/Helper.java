package jettyServer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Helper {
//    public static boolean isLoggedIn(HttpServletRequest request) {
//        String user = getUser(request);
//        if(user == null) {
//            return false;
//        }
//        return true;
//    }

    public Helper() {}
    public static String getUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("username");
    }

    public static void notLoggedIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = getUser(request);
        if (user == null) {
            response.sendRedirect("/login");
        }
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("username");
        session.removeAttribute("newUser");
        session.removeAttribute("loginTime");
        session.invalidate();
        response.sendRedirect("/login");
    }
}

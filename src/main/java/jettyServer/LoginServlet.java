package jettyServer;

import database.DatabaseHandler;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String user = (String) session.getAttribute("username");
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/LoginPage.html");
        if(user == null) {
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            try{
                out.println(writer);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        } else {
            response.sendRedirect("/hotelSearch");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        username = StringEscapeUtils.escapeHtml4(username);
        String password = request.getParameter("password");
        password = StringEscapeUtils.escapeHtml4(password);
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();

        if(dbHandler.authenticateUser(username, password)) {
            String user = (String) session.getAttribute("username");
            if(user == null) {
                session.setAttribute("username", username);
            }
            response.sendRedirect("/hotelSearch");
        }
        else {
            PrintWriter out = response.getWriter();
            VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
            VelocityContext context = new VelocityContext();
            Template template = ve.getTemplate("templates/LoginPage.html");
            context.put("error", "Incorrect login details!");
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            try{
                out.println(writer);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

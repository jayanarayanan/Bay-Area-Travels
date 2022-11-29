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
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter out = response.getWriter();
        VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
        VelocityContext context = new VelocityContext();
        Template template = ve.getTemplate("templates/RegisterPage.html");
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
        String username = request.getParameter("username");
        username = StringEscapeUtils.escapeHtml4(username);
        String password = request.getParameter("password");
        password = StringEscapeUtils.escapeHtml4(password);
        DatabaseHandler dbHandler = DatabaseHandler.getInstance();

        if (dbHandler.validation(username, password).equals("err-free")) {
            dbHandler.registerUser(username, password);
            response.sendRedirect("/login");
        } else {
            PrintWriter out = response.getWriter();
            VelocityEngine ve = (VelocityEngine) request.getServletContext().getAttribute("templateEngine");
            VelocityContext context = new VelocityContext();
            Template template = ve.getTemplate("templates/RegisterPage.html");
            if(dbHandler.validation(username, password).equals("no-lc")) {
                context.put("error", "No lowercase character found in the password!");
            }
            else if(dbHandler.validation(username, password).equals("no-uc")) {
                context.put("error", "No uppercase character found in the password!");
            }
            else if(dbHandler.validation(username, password).equals("no-digit")) {
                context.put("error", "No digits found in the password!");
            }
            else if(dbHandler.validation(username, password).equals("no-sp")) {
                context.put("error", "No special character found in the password!");
            }
            else if(dbHandler.validation(username, password).equals("password-small")) {
                context.put("error", "The password you entered is too small!");
            }
            else if(dbHandler.validation(username, password).equals("user-exists")) {
                context.put("error", "The username already exists!");
            }
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











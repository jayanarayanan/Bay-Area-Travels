package jettyServer;

import hotelapp.ThreadSafeHotelData;
import hotelapp.ThreadSafeHotelReviewData;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;

/** This class uses Jetty & servlets to implement server serving hotel and review info */
public class JettyServer {
    // FILL IN CODE
    private static final int PORT = 8090;

    private ThreadSafeHotelData hotelData;
    private ThreadSafeHotelReviewData reviewData;
    private HashMap<String, HttpServlet> handlers = new HashMap<>();

    //constructor function
    public JettyServer(ThreadSafeHotelData hotelData, ThreadSafeHotelReviewData reviewData) {
        this.hotelData = hotelData;
        this.reviewData = reviewData;
    }

    //this function adds the given route and it's corresponding HttpServlet object to the handlers hashmap.
    public void addHandlers(String route, HttpServlet servlet) {
        handlers.put(route, servlet);
    }
    /**
     * Function that starts the server
     * @throws Exception throws exception if access failed
     */

    //this function starts the server
    public  void start() throws Exception {
        Server server = new Server(PORT);

        ServletHandler servhandler = new ServletHandler();
        for(String r : handlers.keySet()) {
            servhandler.addServletWithMapping(new ServletHolder(handlers.get(r)), r);
        }
        server.setHandler(servhandler);
        try {
            server.start();
            server.join();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}

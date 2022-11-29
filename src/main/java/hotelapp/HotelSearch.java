package hotelapp;

//import servers.httpServer.*;
import database.DatabaseHandler;
import jettyServer.*;

import java.util.HashMap;
import java.util.Scanner;

/** The driver class for project 4
 * Copy your project 3 classes to the hotelapp package.
 * The main function should take the following command line arguments:
 * -hotels hotelFile -reviews reviewsDirectory -threads numThreads -output filepath
 * (order may be different)
 * and read general information about the hotels from the hotelFile (a JSON file),
 * as read hotel reviews from the json files in reviewsDirectory (can be multithreaded or
 * single-threaded).
 * The data should be loaded into data structures that allow efficient search.
 * If the -output flag is provided, hotel information (about all hotels and reviews)
 * should be output into the given file.
 * Then in the main method, you should start an HttpServer that responds to
 * requests about hotels and reviews.
 * See pdf description of the project for the requirements.
 * You are expected to add other classes and methods from project 3 to this project,
 * and take instructor's / TA's feedback from a code review into account.
 * Please download the input folder from Canvas.
 */
public class HotelSearch {

    //Main function that calls the parseArgs function
    public static void main(String[] args) throws Exception {
        parseArgs(args);
    }

    //Parses the CLI arguments into a hashmap, and calls the functions to parse the hotel and review files.
    //Also calls the functions to write the hotel and review data into the specified output file.
    public static void parseArgs(String[] args) throws Exception {
        ThreadSafeHotelData threadSafeHotel = new ThreadSafeHotelData();
        ThreadSafeHotelReviewData threadSafeHotelReview = new ThreadSafeHotelReviewData();
        HashMap<String, String> argsMap = new HashMap<>();
        DatabaseHandler dhandler = DatabaseHandler.getInstance();
        dhandler.createTable();
        threadSafeHotelReview.fillStopWordsMap("input/StopWords.txt");
        for(int i = 0; i < args.length; i++) {
            if(args[i].startsWith("-")) {
                argsMap.put(args[i], args[i+1]);
            }
        }
        threadSafeHotel.addHotels(argsMap.get("-hotels"));
        if(argsMap.containsKey("-threads") && argsMap.containsKey("-reviews")) {
            MultithreadedReviewLoader builder = new MultithreadedReviewLoader(threadSafeHotelReview, Integer.parseInt(argsMap.get("-threads")));
            builder.buildHotelReview(argsMap.get("-reviews"));
            builder.finishWork();
        } else if(argsMap.containsKey("-reviews")) {
            MultithreadedReviewLoader builder = new MultithreadedReviewLoader(threadSafeHotelReview, 1);
            builder.buildHotelReview(argsMap.get("-reviews"));
            builder.finishWork();
        }

//        if(argsMap.containsKey("-output")) {
//            if(argsMap.containsKey("-reviews")) {
//                threadSafeHotelReview.writeHotelReviewToFile(threadSafeHotel, argsMap.get("-output"));
//            } else {
//                threadSafeHotel.writeHotelToFile(argsMap.get("-output"));
//            }
//        } else {
//            mainMenu(argsMap, threadSafeHotel, threadSafeHotelReview);
//        }


        //HttpServer Connection
//        HttpServer serv = new HttpServer();
//        serv.addConstructorHandler("hotelInfo", HotelHandler.class);
//        serv.addConstructorName("hotelInfo", ThreadSafeHotelData.class);
//        serv.addConstructorObject("hotelInfo", threadSafeHotel);
//        serv.addConstructorHandler("reviews", ReviewsHandler.class);
//        serv.addConstructorName("reviews", ThreadSafeHotelReviewData.class);
//        serv.addConstructorObject("reviews", threadSafeHotelReview);
//        serv.addConstructorHandler("index", WordHandler.class);
//        serv.addConstructorName("index", ThreadSafeHotelReviewData.class);
//        serv.addConstructorObject("index", threadSafeHotelReview);
//        serv.addConstructorHandler("weather", WeatherHandler.class);
//        serv.addConstructorName("weather", ThreadSafeHotelData.class);
//        serv.addConstructorObject("weather", threadSafeHotel);
//        if(argsMap.containsKey("-threads")) {
//            serv.startServer(Integer.parseInt(argsMap.get("-threads")));
//        } else {
//            serv.startServer(5);
//        }



        //JettyServer Connection
        JettyServer server = new JettyServer(threadSafeHotel, threadSafeHotelReview);
        server.addHandlers("/hotelInfo", new HotelServlet(threadSafeHotel));
        server.addHandlers("/hotelSearch", new HotelSearchServlet(threadSafeHotel));
        server.addHandlers("/reviews", new ReviewServlet(threadSafeHotelReview, threadSafeHotel));
        server.addHandlers("/index", new WordServlet(threadSafeHotelReview));
        server.addHandlers("/weather", new WeatherServlet(threadSafeHotel));
        server.addHandlers("/addReview", new AddReviewServlet(threadSafeHotelReview));
        server.addHandlers("/modifyReview", new ModifyReviewServlet(threadSafeHotelReview, threadSafeHotel));
        server.addHandlers("/deleteReview", new DeleteReviewServlet(threadSafeHotelReview, threadSafeHotel));
        server.addHandlers("/login", new LoginServlet());
        server.addHandlers("/register", new RegisterServlet());
        server.addHandlers("/logout", new LogoutServlet());
        server.start();
    }

    //main menu function that displays the menu in the CLI and asks for the user's input.
    public static void mainMenu(HashMap<String, String> argsMap, ThreadSafeHotelData threadSafeHotel, ThreadSafeHotelReviewData threadSafeHotelReview) {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Choose the operation you want to perform :\na. Search for a hotel using the Hotel ID, and display the details of that Hotel\nb. Display all the reviews for a given Hotel using the Hotel ID\nc. Display all the comments that contain the specified keyword\nq. Quit\nEnter your choice(a/b/c/q): " );
        String choice = myObj.nextLine();
        while(!choice.equals("q")) {
            if(choice.equals("a")) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter the Hotel ID: ");
                String hotelIDString = scanner.nextLine();
                threadSafeHotel.printHotel(hotelIDString);
            } else if(choice.equals("b")) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter the Hotel ID: ");
                String hotelIDString = scanner.nextLine();
                threadSafeHotelReview.printHotelReview(hotelIDString);
            } else if(choice.equals("c")) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Enter the keyword to search ");
                String searchWord = scanner.nextLine();
                threadSafeHotelReview.findWordInHotelReview(searchWord, "3");
            } else if(choice.equals("q")) {
                break;
            } else {
                System.out.println("The input provided is wrong, please re-enter the input.");
            }
            System.out.println("Choose the operation you want to perform :\na. Search for a hotel using the Hotel ID, and display the details of that Hotel\nb. Display all the reviews for a given Hotel using the Hotel ID\nc. Display all the comments that contain the specified keyword\nq. Quit\nEnter your choice(a/b/c/q): " );
            choice = myObj.nextLine();
        }
    }

}


package hotelapp;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class MultithreadedReviewLoader {
    private ThreadSafeHotelReviewData hotelReview;
    private ExecutorService poolThreads;
    private Phaser phaser;
    private Logger logger = LogManager.getLogger();

    //Constructor for the class
    public MultithreadedReviewLoader(ThreadSafeHotelReviewData hotelReview, int threadCount) {
        this.hotelReview = hotelReview;
        poolThreads = Executors.newFixedThreadPool(threadCount);
        phaser = new Phaser();
    }

    //Worker class that implements Runnable
    private class Worker implements Runnable {
        Path path;

        //Constructor for the worker
        Worker(Path path) {
            this.path = path;
        }
        @Override
        public void run() {

            try {
                parseHotelReview(path);
            } finally {
                phaser.arriveAndDeregister();
            }
        }
    }

    //Parses the review json file and calls the function to add the review data into a hashmap.
    public void parseHotelReview(Path path) {
        ArrayList<HotelReview> hotelReviewsArray;
        Gson gson = new Gson();
        String pathString = path.toString();
        try (FileReader br = new FileReader(pathString)) {
            JsonParser parser = new JsonParser();
            JsonObject jo = (JsonObject) parser.parse(br);
            JsonArray jsonArr = jo.getAsJsonObject("reviewDetails").getAsJsonObject("reviewCollection").getAsJsonArray("review");
//            double avgRating = jo.getAsJsonObject("reviewDetails").getAsJsonObject("reviewSummaryCollection").getAsJsonArray("reviewSummary").get(0).getAsJsonObject().get("avgOverallRating").getAsDouble();
//            String hotelId = jo.getAsJsonObject("reviewDetails").getAsJsonObject("reviewSummaryCollection").getAsJsonArray("reviewSummary").get(0).getAsJsonObject().get("hotelId").getAsString();
//            System.out.println(hotelId + ": " + avgRating);
            Type hotelReviewType = new TypeToken<ArrayList<HotelReview>>(){}.getType();
            hotelReviewsArray = gson.fromJson(jsonArr, hotelReviewType);
            hotelReview.fillReviewsMap(hotelReviewsArray);
            logger.debug("Worker is done processing " + path);
        } catch (IOException e) {
            System.out.println("Could not read the file: " + e);
        }
    }

    //Iterates through folders in the directory and processes all the json files in all the directories.
    //Uses pool of threads and phaser to implement multithreading on json parsing
    public void buildHotelReview(String hotelReviewPath) {
        Path p = Paths.get(hotelReviewPath);
        try (DirectoryStream<Path> pathsInDir = Files.newDirectoryStream(p)) {
            for (Path path : pathsInDir) {
                if (Files.isDirectory(path)) {
                    String pathString = path.toString();
                    buildHotelReview(pathString);
                } else if (path.toString().endsWith(".json")) {
                    Worker worker = new Worker(path);
                    logger.debug("Worker has been created.");
                    phaser.register();
                    poolThreads.submit(worker);
                }
            }
        } catch(IOException e) {
            System.out.println("Can not open directory: " + p);
        }
    }
    //Called after the job has been completed by the thread, calls the shutdown function
    public void finishWork() {
        int phase = phaser.getPhase();
        phaser.awaitAdvance(phase);
        shutDown();
    }

    //shuts down the pool of threads after the task has been completed by the thread in a safe manner.
    public void shutDown(){
        poolThreads.shutdown();
        try {
            poolThreads.awaitTermination(3, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}

package hotelapp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class HotelReviewData {
    private HashMap<String, ArrayList<HotelReview>> reviewsMap = new HashMap<>();
    private static HashMap<String, TreeMap<Integer, ArrayList<HotelReview>>> wordMap = new HashMap<>();
    private static HashSet<String> stopWords = new HashSet<>();
//    private static HashMap<String, Double> ratingsMap = new HashMap<>();


    //Called by the run function, adds the arraylist of hotelreview objects read from the json file into the hashmap.
    public void fillReviewsMap(ArrayList<HotelReview> hotelReviews) {
        Collections.sort(hotelReviews);
        if(!hotelReviews.isEmpty()) {
            reviewsMap.put(hotelReviews.get(0).getHotelID(), hotelReviews);
        }
        fillWordMap(hotelReviews);
    }

    public void addReviewToMap(String hotelId, HotelReview review) {
        reviewsMap.get(hotelId).add(review);
    }

//    public void fillRatingsMap(String hotelId, Double avgRating) {
//        ratingsMap.put(hotelId, avgRating);
//    }

    //Called during the start of the program, adds stop words to the stopWords map.
    public void fillStopWordsMap(String stopWordsFilePath) {

        try(BufferedReader br = new BufferedReader(new FileReader(stopWordsFilePath))) {
            String line = br.readLine();
            while (line != null) {
                stopWords.add(line);
                line = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("Could not read the file" + e);
        }
    }

    //After adding the review details into reviewsMap, this function is called which creates an inverted index
    //to enable occurences of a word in the review set.
    public void fillWordMap(ArrayList<HotelReview> hotelReviews) {
        boolean exists = false;
        for(HotelReview hr : hotelReviews) {
            String[] tempReviewText = hr.getReviewText().split(" ");
            for(String word : tempReviewText) {
                word = word.replaceAll("[,.]", "");
                if(!stopWords.contains(word)) {
                    if(!wordMap.containsKey(word)) {
                        wordMap.put(word, new TreeMap<>());
                        wordMap.get(word).put(1, new ArrayList<>());
                        wordMap.get(word).get(1).add(hr);
                    } else {
                        for(Integer i : wordMap.get(word).keySet()) {
                            if(wordMap.get(word).get(i).contains(hr)) {
                                wordMap.get(word).get(i).remove(hr);
                                if(wordMap.get(word).containsKey(i+1)){
                                    wordMap.get(word).get(i+1).add(hr);
                                } else {
                                    wordMap.get(word).put(i+1, new ArrayList<>());
                                    wordMap.get(word).get(i+1).add(hr);
                                }
                                exists = true;
                                break;
                            }
                        }
                        if(!exists) {
                            wordMap.get(word).get(1).add(hr);
                        }
                    }
                }
            }
        }
    }

    //prints the hotel review details for a given hotel in the CLI
    public ArrayList<HotelReview> printHotelReview(String hotelID) {
            ArrayList<HotelReview> hotelReviewArr = new ArrayList<>();
            if(!reviewsMap.containsKey(hotelID) || hotelID == null) {
                return hotelReviewArr;
            }
            else if(reviewsMap.containsKey(hotelID)) {
                for(HotelReview r : reviewsMap.get(hotelID)) {
                        hotelReviewArr.add(r);
                }
                return hotelReviewArr;
            } else {
                return hotelReviewArr;
            }
    }

    public double avgRating(String hotelId) {
        double avgOverallRating = 0;
        int count = 0;
        for(HotelReview r : reviewsMap.get(hotelId)) {
            avgOverallRating += r.getRating();
            count++;
        }
        return avgOverallRating/count;
    }

    public JsonObject addToJson(HotelReview r) {
        JsonObject jsonTemp = new JsonObject();
        jsonTemp.addProperty("reviewId", r.getReviewID());
        jsonTemp.addProperty("title", r.getReviewTitle());
        jsonTemp.addProperty("user", r.getNickName());
        jsonTemp.addProperty("reviewText", r.getReviewText());
        jsonTemp.addProperty("date", r.getReviewDate());
        return jsonTemp;
    }

    //Writes the review details of a given hotel into the specified output file.
    public void writeHotelReviewToFile(HotelData hd, String output) {
        try(PrintWriter writer = new PrintWriter(output)) {
            Set<String> hotelKeySet = hd.getHotelKeySet();
            for(String i : hotelKeySet) {
                writer.println(hd.getHotelObject(i));
                if(reviewsMap.containsKey(i)) {
                    for(HotelReview r : reviewsMap.get(i)) {
                        writer.println(r);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //Searches for occurences of a word in the review set,
    // and prints all the reviews that contain the given word.
    public JsonObject findWordInHotelReview(String word, String num) {
        try {
            int tempNum = 0;
            JsonObject json = new JsonObject();
            JsonArray jsonArr = new JsonArray();
            json.addProperty("success", true);
            json.addProperty("word", word);
            if(stopWords.contains(word)) {
                System.out.println("The provided word is a stop word. Please enter another word");
            } else if(word == null || num == null) {
                JsonObject jsonErr = new JsonObject();
                jsonErr.addProperty("success", false);
                jsonErr.addProperty("word", "invalid");
                return jsonErr;
            }
            else {
                int maxFreq = wordMap.get(word).lastKey();
                for(int i = maxFreq; i >= 1; i--) {
                    if(wordMap.get(word).containsKey(i)) {
                        ArrayList<HotelReview> hotelReviews = wordMap.get(word).get(i);
                        Collections.sort(hotelReviews);
                        for(HotelReview r : hotelReviews) {
                            int numReviews = Integer.parseInt(num);
                            if(tempNum < numReviews) {
                                JsonObject jsonTemp = addToJson(r);
                                jsonArr.add(jsonTemp);
                                tempNum++;
                            }
                        }
                        json.add("reviews", jsonArr);
                    }
                }
            }
            return json;
        } catch(Exception e) {
            System.out.println(e.getMessage());
            JsonObject jsonErr = new JsonObject();
            jsonErr.addProperty("success", false);
            jsonErr.addProperty("word", "invalid");
            return jsonErr;
        }
    }
}

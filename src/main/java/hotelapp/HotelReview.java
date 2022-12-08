package hotelapp;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HotelReview implements Comparable<HotelReview> {
    @SerializedName(value = "hotelId")
    private String hotelID;
    @SerializedName(value = "reviewId")
    private String reviewID;
    @SerializedName(value = "ratingOverall")
    private float overallRating;
    private String title;
    private String reviewText;
    private String userNickname;
    @SerializedName(value = "reviewSubmissionTime")
    private String postDate;

    //constructor for HotelReview.
    public HotelReview(String hotelID, String reviewID, float overallRating, String title, String reviewText, String userNickname, String stringDate) {
        this.hotelID = hotelID;
        this.reviewID = reviewID;
        this.overallRating = overallRating;
        this.title = title;
        this.reviewText = reviewText;
        this.userNickname = userNickname;
        this.postDate = stringDate;
    }

    //Returns Anonymous as the userNickname if the variable is empty,
    // prints the nickname read from the json file otherwise
    public String getNickName() {
        if(userNickname.isEmpty()) {
            return "Anonymous";
        } else {
            return userNickname;
        }
    }
    //getter function for the review text, returns the reviewText of this object.
    public String getReviewText() {
        return this.reviewText;
    }
    //getter function for the review ID, returns the reviewID of this object.
    public String getReviewID() {
        return this.reviewID;
    }
    //getter function for the hotel ID, returns the hotelID of this object.
    public String getHotelID() {
        return this.hotelID;
    }
    public String getReviewTitle() {
        return this.title;
    }
    public String getReviewDate() {
        return this.postDate;
    }
    public float getRating() {
        return this.overallRating;
    }
    public void modifyReviewTitle(String reviewTitle) {
        this.title = reviewTitle;
    }
    public void modifyReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    //displays the contents of a HotelReview object
    public String toString() {
        return "Review by " + getNickName() + " on " + getPostDate() + "<br>" +
                "Rating: " + overallRating + "<br>" +
                "ReviewId: " + reviewID + "<br>" +
                "Title: " + title + "<br>" +
                "Review Text: " + reviewText + "<br>";
    }

    //used by the Collections.sort() function to sort the ArrayList of type HotelReview by date.
    public int compareTo(HotelReview review2) {
        LocalDate postDate1 = this.getPostDate();
        LocalDate postDate2 = review2.getPostDate();
        if(postDate1.compareTo(postDate2) == 0) {
            return this.reviewID.compareTo(review2.reviewID);
        }
        return -postDate1.compareTo(postDate2);
    }

    //Converts the postDate String to a LocalDate type
    public LocalDate getPostDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDate.parse(postDate, formatter);
    }
}

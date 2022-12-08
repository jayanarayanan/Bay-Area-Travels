package hotelapp;

import java.util.ArrayList;
import java.util.HashMap;

public class Likes {

    private String reviewId;
    private String likedUser;

    public Likes(String reviewId, String user) {
        this.reviewId = reviewId;
        this.likedUser = user;
    }

    public Likes() {
    }

    public String getReviewId() {
        return this.reviewId;
    }

    public String getLikedUser() {
        return this.likedUser;
    }

    public HashMap<String, ArrayList<String>> getLikeMap(ArrayList<Likes> arr) {
        HashMap<String, ArrayList<String>> likeMap = new HashMap<>();
        for(Likes l : arr) {
            if(likeMap.containsKey(l.getReviewId())) {
                likeMap.get(l.getReviewId()).add(l.getLikedUser());
            } else {
                likeMap.put(l.getReviewId(), new ArrayList<>());
                likeMap.get(l.getReviewId()).add(l.getLikedUser());
            }
        }
        return likeMap;
    }
}

package hotelapp;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeHotelReviewData extends HotelReviewData {
    private ReentrantReadWriteLock lock;

    //Constructor for this class, creates the lock.
    public ThreadSafeHotelReviewData() {
        lock = new ReentrantReadWriteLock();
    }

    //thread-safe function for fillReviewsMap() utilizing the write lock.
    @Override
    public void fillReviewsMap(ArrayList<HotelReview> hotelReviews) {
        try {
            lock.writeLock().lock();
            super.fillReviewsMap(hotelReviews);
        } finally {
            lock.writeLock().unlock();
        }
    }
    @Override
    public void addReviewToMap(String hotelId, HotelReview review) {
        try {
            lock.writeLock().lock();
            super.addReviewToMap(hotelId, review);
        } finally {
            lock.writeLock().unlock();
        }
    }


    @Override
    public void deleteReview(String hotelId, String reviewId) {
        try {
            lock.writeLock().lock();
            super.deleteReview(hotelId, reviewId);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public HotelReview getReviewObj(String hotelId, String reviewId) {
        try {
            lock.readLock().lock();
            return super.getReviewObj(hotelId, reviewId);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void modifyReview(String hotelId, String reviewId, String reviewTitle, String reviewText) {
        try {
            lock.writeLock().lock();
            super.modifyReview(hotelId, reviewId, reviewTitle, reviewText);
        } finally {
            lock.writeLock().unlock();
        }
    }

    //thread-safe function for fillWordMap() utilizing the write lock.
    @Override
    public void fillWordMap(ArrayList<HotelReview> hotelReviews) {
        try {
            lock.writeLock().lock();
            super.fillWordMap(hotelReviews);
        } finally {
            lock.writeLock().unlock();
        }
    }

    //thread-safe function for fillStopWordsMap() utilizing the write lock.
    @Override
    public void fillStopWordsMap(String stopWordsFilePath) {
        try {
            lock.writeLock().lock();
            super.fillStopWordsMap(stopWordsFilePath);
        } finally {
            lock.writeLock().unlock();
        }
    }

    //thread-safe function for printHotelReview() utilizing the read lock.
    @Override
    public ArrayList<HotelReview> printHotelReview(String hotelID) {
        try {
            lock.readLock().lock();
            return super.printHotelReview(hotelID);
        } finally {
            lock.readLock().unlock();
        }
    }

    //thread-safe function for writeHotelReviewToFile() utilizing the read lock.
    @Override
    public void writeHotelReviewToFile(HotelData hd, String output) {
        try {
            lock.readLock().lock();
            super.writeHotelReviewToFile(hd, output);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public double avgRating(String hotelId) {
        try {
            lock.readLock().lock();
            return super.avgRating(hotelId);
        } finally {
            lock.readLock().unlock();
        }
    }

    public JsonObject addToJson(HotelReview r) {
        try {
            lock.readLock().lock();
            return super.addToJson(r);
        } finally {
            lock.readLock().unlock();
        }
    }

    //thread-safe function for findWordInHotelReview() utilizing the read lock.
    @Override
    public JsonObject findWordInHotelReview(String word, String num) {
        try {
            lock.readLock().lock();
            return super.findWordInHotelReview(word, num);
        } finally {
            lock.readLock().unlock();
        }
    }
}

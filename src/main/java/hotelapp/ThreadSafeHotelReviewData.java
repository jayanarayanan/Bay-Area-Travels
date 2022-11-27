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
    public JsonObject printHotelReview(String hotelID, String num) {
        try {
            lock.readLock().lock();
            return super.printHotelReview(hotelID, num);
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

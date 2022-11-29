package hotelapp;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ThreadSafeHotelData extends HotelData {
    private ReentrantReadWriteLock lock;

    //Constructor for this class, creates the lock.
    public ThreadSafeHotelData() {
        lock = new ReentrantReadWriteLock();
    }

    //thread-safe function for addHotels() utilizing the write lock.
    @Override
    public void addHotels(String hotelPath) {
        try {
            lock.writeLock().lock();
            super.addHotels(hotelPath);
        } finally {
            lock.writeLock().unlock();
        }
    }

    //thread-safe function for printHotel() utilizing the read lock.
    @Override
    public JsonObject printHotel(String hotelID) {
        try {
            lock.readLock().lock();
            return super.printHotel(hotelID);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Hotel getHotelObject(String hotelID) {
        try {
            lock.readLock().lock();
            return super.getHotelObject(hotelID);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public String getExpediaLink(String hotelID) {
        try {
            lock.readLock().lock();
            return super.getExpediaLink(hotelID);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Set<String> getHotelKeySet() {
        try {
            lock.readLock().lock();
            return super.getHotelKeySet();
        } finally {
            lock.readLock().unlock();
        }
    }


    public ArrayList<Hotel> findHotel(String word) {
        try {
            lock.readLock().lock();
            return super.findHotel(word);
        } finally {
            lock.readLock().unlock();
        }
    }

    //thread-safe function for writeHotelToFile() utilizing the read lock.
    @Override
    public void writeHotelToFile(String output) {
        try {
            lock.readLock().lock();
            super.writeHotelToFile(output);
        } finally {
            lock.readLock().unlock();
        }
    }
}

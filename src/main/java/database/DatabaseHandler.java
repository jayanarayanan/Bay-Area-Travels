package database;

import database.PreparedStatements;
import hotelapp.GeoInfo;
import hotelapp.Hotel;
import hotelapp.HotelReview;
import hotelapp.Likes;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseHandler {

        private static DatabaseHandler dbHandler = new DatabaseHandler("database.properties"); // singleton pattern
        private Properties config; // a "map" of properties
        private String uri = null; // uri to connect to mysql using jdbc
        private Random random = new Random(); // used in password  generation

        /**
         * DataBaseHandler is a singleton, we want to prevent other classes
         * from creating objects of this class using the constructor
         */
        private DatabaseHandler(String propertiesFile){
            this.config = loadConfigFile(propertiesFile);
            this.uri = "jdbc:mysql://"+ config.getProperty("hostname") + "/" + config.getProperty("username") + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            //System.out.println("uri = " + uri);
        }

        /**
         * Returns the instance of the database handler.
         * @return instance of the database handler
         */
        public static DatabaseHandler getInstance() {
            return dbHandler;
        }

        // Load info from config file database.properties
        public Properties loadConfigFile(String propertyFile) {
            Properties config = new Properties();
            try (FileReader fr = new FileReader(propertyFile)) {
                config.load(fr);
            }
            catch (IOException e) {
                System.out.println(e);
            }

            return config;
        }

        public void createTable() {
            Statement statement;
            try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
                System.out.println("dbConnection successful");
                statement = dbConnection.createStatement();
//                statement.executeUpdate(PreparedStatements.CREATE_USER_TABLE);
//                statement.executeUpdate(PreparedStatements.CREATE_HOTEL_TABLE);
//                statement.executeUpdate(PreparedStatements.CREATE_REVIEW_TABLE);
//                statement.executeUpdate(PreparedStatements.CREATE_LIKES_TABLE);
//                statement.executeUpdate(PreparedStatements.CREATE_LINKS_TABLE);
//                statement.executeUpdate(PreparedStatements.CREATE_LAST_LOGIN_TABLE);
            }
            catch (SQLException ex) {
                System.out.println(ex);
            }
        }

        /**
         * Returns the hex encoding of a byte array.
         *
         * @param bytes - byte array to encode
         * @param length - desired length of encoding
         * @return hex encoded byte array
         */
        public static String encodeHex(byte[] bytes, int length) {
            BigInteger bigint = new BigInteger(1, bytes);
            String hex = String.format("%0" + length + "X", bigint);

            assert hex.length() == length;
            return hex;
        }

        /**
         * Calculates the hash of a password and salt using SHA-256.
         *
         * @param password - password to hash
         * @param salt - salt associated with user
         * @return hashed password
         */
        public static String getHash(String password, String salt) {
            String salted = salt + password;
            String hashed = salted;

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(salted.getBytes());
                hashed = encodeHex(md.digest(), 64);
            }
            catch (Exception ex) {
                System.out.println(ex);
            }

            return hashed;
        }

        /**
         * Registers a new user, placing the username, password hash, and
         * salt into the database.
         *
         * @param newuser - username of new user
         * @param newpass - password of new user
         */
        public void registerUser(String newuser, String newpass) {
            // Generate salt
            byte[] saltBytes = new byte[16];
            random.nextBytes(saltBytes);

            String usersalt = encodeHex(saltBytes, 32); // salt
            String passhash = getHash(newpass, usersalt); // hashed password
            System.out.println(usersalt);

            PreparedStatement statement;
            try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
                System.out.println("dbConnection successful");
                try {
                    statement = connection.prepareStatement(PreparedStatements.USER_REGISTER_SQL);
                    statement.setString(1, newuser);
                    statement.setString(2, passhash);
                    statement.setString(3, usersalt);
                    statement.executeUpdate();
                    statement.close();
                }
                catch(SQLException e) {
                    System.out.println(e);
                }
            }
            catch (SQLException ex) {
                System.out.println(ex);
            }
        }

    public void addHotelsToDB(String hotelId, String hotelName, String address, String city, String state, GeoInfo coordinates) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.INSERT_HOTEL_SQL);
                statement.setString(1, hotelName);
                statement.setString(2, hotelId);
                statement.setString(3, address);
                statement.setString(4, city);
                statement.setString(5, state);
                statement.setDouble(6, coordinates.getLat());
                statement.setDouble(7, coordinates.getLng());
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    public void addReviewsToDB(String reviewId, String hotelId, String title, String text, String username, float rating, String postDate) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.INSERT_REVIEW_SQL);
                statement.setString(1, reviewId);
                statement.setString(2, hotelId);
                statement.setString(3, title);
                statement.setString(4, text);
                statement.setString(5, username);
                statement.setFloat(6, rating);
                statement.setString(7, postDate);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public Hotel getHotelFromDB(String hotelId) {
        PreparedStatement statement;
        Hotel h = null;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.FIND_HOTEL_SQL);
                statement.setString(1, hotelId);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    h = new Hotel(rs.getString("hotelName"), rs.getString("hotelId"), rs.getString("address"), rs.getString("city"), rs.getString("state"), new GeoInfo(rs.getDouble("lat"), rs.getDouble("lng")));
                }
                statement.close();
                return h;
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public ArrayList<Hotel> searchHotelsInDB(String word) {
        PreparedStatement statement;
        ResultSet rs;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                if(word == null || word.isEmpty()) {
                    statement = connection.prepareStatement(PreparedStatements.GET_ALL_HOTEL_SQL);
                    rs = statement.executeQuery();
                } else {
                    statement = connection.prepareStatement(PreparedStatements.GET_GIVEN_HOTEL_SQL);
                    statement.setString(1, "%" + word + "%");
                    rs = statement.executeQuery();
                }
                ArrayList<Hotel> arr = new ArrayList<>();
                while(rs.next()) {
                    Hotel h = new Hotel(rs.getString("hotelName"), rs.getString("hotelId"), rs.getString("address"), rs.getString("city"), rs.getString("state"), new GeoInfo(rs.getDouble("lat"), rs.getDouble("lng")));
                    arr.add(h);
                }
                statement.close();
                return arr;
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public ArrayList<HotelReview> getReviewsFromDB(String hotelId, int num) {
        PreparedStatement statement;
        ArrayList<HotelReview> arr = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.FIND_REVIEW_SQL);
                statement.setString(1, hotelId);
                statement.setInt(2, num);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    HotelReview hr = new HotelReview(rs.getString("hotelId"), rs.getString("reviewId"), rs.getFloat("reviewRating"), rs.getString("title"), rs.getString("reviewText"), rs.getString("username"), rs.getString("postDate"));
                    arr.add(hr);
                }
                statement.close();
                Collections.sort(arr);
                return arr;
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public int getAvgRatingFromDB(String hotelId) {
        PreparedStatement statement;
        ArrayList<HotelReview> arr = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.FIND_REVIEW_SQL);
                statement.setString(1, hotelId);
                statement.setInt(2, num);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    HotelReview hr = new HotelReview(rs.getString("hotelId"), rs.getString("reviewId"), rs.getFloat("reviewRating"), rs.getString("title"), rs.getString("reviewText"), rs.getString("username"), rs.getString("postDate"));
                    arr.add(hr);
                }
                statement.close();
                Collections.sort(arr);
                return arr;
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public int getTotalReviews(String hotelId) {
        PreparedStatement statement;
        int count = 0;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.FIND_REVIEW_COUNT_SQL);
                statement.setString(1, hotelId);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    count = rs.getInt("count");
                }
                statement.close();
                return count;
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return 0;
    }

    public HotelReview getReviewObjFromDB(String reviewId) {
        PreparedStatement statement;
        HotelReview h = null;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.FIND_REVIEWID_SQL);
                statement.setString(1, reviewId);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    h = new HotelReview(rs.getString("hotelId"), rs.getString("reviewId"), rs.getFloat("reviewRating"), rs.getString("title"), rs.getString("reviewText"), rs.getString("username"), rs.getString("postDate"));
                }
                statement.close();
                return h;
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void editReviewsInDB(String reviewId, String hotelId, String title, String text, String username, float rating, String postDate) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.UPDATE_REVIEW_SQL);
                statement.setFloat(1, rating);
                statement.setString(2, title);
                statement.setString(3, text);
                statement.setString(4, postDate);
                statement.setString(5, reviewId);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void deleteReviewFromDB(String reviewId) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.DELETE_REVIEW_SQL);
                statement.setString(1, reviewId);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void addLikesInDB(String reviewId, String hotelId, String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.INSERT_LIKES_SQL);
                statement.setString(1, reviewId);
                statement.setString(2, hotelId);
                statement.setString(3, username);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public ArrayList<Likes> findLikesInDB(String hotelId) {
        PreparedStatement statement;
        ArrayList<Likes> likes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.FIND_LIKES_SQL);
                statement.setString(1, hotelId);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    Likes l = new Likes(rs.getString("reviewId"), rs.getString("likedUser"));
                    likes.add(l);
                }
                statement.close();
                return likes;
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void removeLikesInDB(String reviewId, String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.DELETE_LIKES_SQL);
                statement.setString(1, reviewId);
                statement.setString(2, username);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void addLinkInDB(String username, String link) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.INSERT_LINKS_SQL);
                statement.setString(1, username);
                statement.setString(2, link);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public ArrayList<String> findLinksInDB(String username) {
        PreparedStatement statement;
        ArrayList<String> links = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.GET_ALL_LINKS_SQL);
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                    links.add(rs.getString("expediaLink"));
                }
                statement.close();
                return links;
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void removeLinksInDB(String username) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.DELETE_LINKS_SQL);
                statement.setString(1, username);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public void addLoginDateInDB(String username, String date) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.INSERT_DATE_SQL);
                statement.setString(1, username);
                statement.setString(2, date);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    public String findLoginDateInDB(String username) {
        PreparedStatement statement;
        String date = "";
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.GET_LOGIN_DATE_SQL);
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();
                while(rs.next()) {
                   date = rs.getString("date");
                }
                statement.close();
                return date;
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
    }

    public void modifyLoginDateInDB(String username, String date) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            System.out.println("dbConnection successful");
            try {
                statement = connection.prepareStatement(PreparedStatements.UPDATE_LAST_LOGIN_SQL);
                statement.setString(1, date);
                statement.setString(2, username);
                statement.executeUpdate();
                statement.close();
            }
            catch(SQLException e) {
                System.out.println(e);
            }
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
    }

        public String validation(String username, String password) {
            if(userExists(username)) {
                return "user-exists";
            }
            if(password.length()>=8)
            {
                Pattern uppercase = Pattern.compile("[A-z]");
                Pattern lowercase = Pattern.compile("[a-z]");
                Pattern digit = Pattern.compile("[0-9]");
                Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");

                Matcher hasLC = lowercase.matcher(password);
                Matcher hasUC = uppercase.matcher(password);
                Matcher hasDigit = digit.matcher(password);
                Matcher hasSpecial = special.matcher(password);

                if(!hasLC.find()) {
                    return "no-lc";
                }
                else if(!hasUC.find()) {
                    return "no-uc";
                }
                else if(!hasDigit.find()) {
                    return "no-digit";
                }
                else if(!hasSpecial.find()) {
                    return "no-sp";
                } else {
                    return "err-free";
                }
            }
            else
                return "password-small";
        }

    public boolean userExists(String username) {
        boolean exists = false;
        try (Connection dbConnection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            PreparedStatement ps = dbConnection.prepareStatement(PreparedStatements.FIND_USER_SQL);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            exists = rs.next();
        }
        catch (SQLException ex) {
            System.out.println(ex);
        }
        return exists;
    }

    public boolean authenticateUser(String username, String password) {
        PreparedStatement statement;
        try (Connection connection = DriverManager.getConnection(uri, config.getProperty("username"), config.getProperty("password"))) {
            //System.out.println("dbConnection successful");
            statement = connection.prepareStatement(PreparedStatements.AUTH_SQL);
            String usersalt = getSalt(connection, username);
            String passhash = getHash(password, usersalt);

            statement.setString(1, username);
            statement.setString(2, passhash);
            ResultSet results = statement.executeQuery();
            boolean flag = results.next();
            return flag;
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    private String getSalt(Connection connection, String user) {
        String salt = null;
        try (PreparedStatement statement = connection.prepareStatement(PreparedStatements.SALT_SQL)) {
            statement.setString(1, user);
            ResultSet results = statement.executeQuery();
            if (results.next()) {
                salt = results.getString("usersalt");
                return salt;
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return salt;
    }

        public static void main(String[] args) {
        }
}

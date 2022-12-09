package database;

public class PreparedStatements {
    /** Prepared Statements  */
    /** For creating the users table */
    public static final String CREATE_USER_TABLE =
            "CREATE TABLE users (" +
                    "userid INTEGER AUTO_INCREMENT PRIMARY KEY, " +
                    "username VARCHAR(32) NOT NULL UNIQUE, " +
                    "password CHAR(64) NOT NULL, " +
                    "usersalt CHAR(32) NOT NULL);";

    public static final String CREATE_HOTEL_TABLE =
            "CREATE TABLE hotels (" +
                    "hotelName VARCHAR(60) NOT NULL UNIQUE, " +
                    "hotelId VARCHAR(20) NOT NULL PRIMARY KEY, " +
                    "address VARCHAR(50) NOT NULL, " +
                    "city VARCHAR(20) NOT NULL, " +
                    "state VARCHAR(20) NOT NULL, " +
                    "lat FLOAT(5) NOT NULL, " +
                    "lng FLOAT(5) NOT NULL);";

    public static final String CREATE_REVIEW_TABLE =
            "CREATE TABLE reviews (" +
                    "reviewId VARCHAR(40) NOT NULL PRIMARY KEY, " +
                    "hotelId VARCHAR(20) NOT NULL, " +
                    "title VARCHAR(100) NOT NULL, " +
                    "reviewText VARCHAR(2000) NOT NULL, " +
                    "username VARCHAR(20) NOT NULL, " +
                    "reviewRating FLOAT(5) NOT NULL, " +
                    "postDate VARCHAR(20) NOT NULL);";

    public static final String CREATE_LIKES_TABLE =
            "CREATE TABLE likes (" +
                    "reviewId VARCHAR(40) NOT NULL, " +
                    "hotelId VARCHAR(20) NOT NULL, " +
                    "likedUser VARCHAR(40) NOT NULL);";

    public static final String CREATE_LINKS_TABLE =
            "CREATE TABLE linkClicks (" +
                    "username VARCHAR(40) NOT NULL, " +
                    "expediaLink VARCHAR(200) NOT NULL);";

    /** Used to insert a new user into the database. */
    public static final String USER_REGISTER_SQL =
            "INSERT INTO users (username, password, usersalt) " +
                    "VALUES (?, ?, ?);";

    public static final String INSERT_HOTEL_SQL =
            "INSERT INTO hotels (hotelName, hotelId, address, city, state, lat, lng) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

    public static final String INSERT_REVIEW_SQL =
            "INSERT INTO reviews (reviewId, hotelId, title, reviewText, username, reviewRating, postDate) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);";

    public static final String INSERT_LIKES_SQL =
            "INSERT INTO likes (reviewId, hotelId, likedUser) " +
                    "VALUES (?, ?, ?);";

    public static final String INSERT_LINKS_SQL =
            "INSERT INTO linkClicks (username, expediaLink) " +
                    "VALUES (?, ?);";

    /** Used to check if a user with a given username exists. */
    public static final String FIND_USER_SQL =
            "SELECT username FROM users WHERE username = ?";

    public static final String FIND_HOTEL_SQL =
            "SELECT * FROM hotels WHERE hotelId = ?";

    public static final String FIND_LIKES_SQL =
            "SELECT reviewId, likedUser FROM likes WHERE hotelId = ?";

    public static final String GET_ALL_LINKS_SQL =
            "SELECT * FROM linkClicks WHERE username = ?";

    public static final String GET_ALL_HOTEL_SQL =
            "SELECT * FROM hotels";

    public static final String GET_GIVEN_HOTEL_SQL =
            "SELECT * FROM hotels WHERE hotelName LIKE ?";

    public static final String FIND_REVIEW_SQL =
            "SELECT * FROM reviews WHERE hotelId = ?";

    public static final String FIND_REVIEWID_SQL =
            "SELECT * FROM reviews WHERE hotelId = ?";

    public static final String DELETE_REVIEW_SQL =
            "DELETE FROM reviews WHERE reviewId = ?";

    public static final String DELETE_LIKES_SQL =
            "DELETE FROM likes WHERE reviewId = ? AND likedUser = ?";

    public static final String DELETE_LINKS_SQL =
            "DELETE FROM linkClicks WHERE username = ?";

    public static final String UPDATE_REVIEW_SQL =
            "UPDATE reviews SET reviewRating = ?, title = ?, reviewText = ?, postDate = ? WHERE reviewId = ?";

    /** Used to retrieve the salt associated with a specific user. */
    public static final String SALT_SQL =
            "SELECT usersalt FROM users WHERE username = ?";

    /** Used to authenticate a user. */
    public static final String AUTH_SQL =
            "SELECT username FROM users " +
                    "WHERE username = ? AND password = ?";
}
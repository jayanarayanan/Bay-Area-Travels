For the final project, I have implemented a website with a subset of the functionality of TripAdvisor (or oyster.com, hotels.com), where users can register, login, search for hotels, read reviews written by other people, write and edit their own reviews, and more. However, the website does not allow users to check availability or book hotels.

In terms of requirements, I have implemented both the front end and back end using Java (plus some Javascript for the front end as needed). I have used Jetty/servlets for this project and used a locally installed MySQL database. To give my website a clean, professional look, I have used Bootstrap templates for all pages, including the registration and login pages. The look is consistent across all webpages, and I have used Bootstrap components such as Table, Modal, Jumbotron, Navbar.

To store all data, including hotels and reviews, and the data required to implement features such as "favorite hotels", "history of expedia clicks" etc., I have used a MySQL database, and all operations use the database (accessed using JDBC).

For certain features, I have used Ajax to update only the part of the webpage without reloading the whole page. These features include showing the hotel on the map, allowing the user to save favorite hotels, and displaying the weather at the latitude and longitude of the given hotel.

I have also implemented a feature to store a history of all expedia hotel links visited by the given user. This history is stored in the database, and users can view and clear the history. This page is available only to users who successfully logged in.

To provide pagination, I have shown a fixed number of reviews on each page, and the user can navigate through multiple pages with reviews. I have implemented this feature on the frontend (using JavaScript).

Lastly, I have implemented a feature to store and display the last date and time the user logged in to the website before this time (the feature is available only for logged-in users). The website displays the last login time in the UTC format.

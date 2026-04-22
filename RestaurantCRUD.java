import java.sql.*;

public class RestaurantCRUD {

    static final String URL = "jdbc:mysql://localhost:3308/restaurantdb";
    static final String USER = "root";
    static final String PASSWORD = "sit123"; 

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("Connected to database successfully.\n");

                insertRestaurants(con);
                insertMenuItems(con);

                System.out.println("----- All Menu Items where Price <= 100 -----");
                selectMenuItemsPriceLessThan100(con);

                System.out.println("\n----- Menu Items available in Restaurant named 'Cafe Java' -----");
                selectMenuItemsInCafeJava(con);

                System.out.println("\n----- Updating Menu Items: Price <= 100 -> 200 -----");
                updateMenuItemsPrice(con);

                System.out.println("\n----- Menu Items after Update -----");
                displayAllMenuItems(con);

                System.out.println("\n----- Deleting Menu Items where Name starts with 'P' -----");
                deleteMenuItemsStartingWithP(con);

                System.out.println("\n----- Menu Items after Deletion -----");
                displayAllMenuItems(con);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found. Make sure MySQL Connector JAR is added.");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    public static void insertRestaurants(Connection con) throws SQLException {
        String deleteMenu = "DELETE FROM MenuItem";
        String deleteRestaurant = "DELETE FROM Restaurant";
        String insertSql = "INSERT INTO Restaurant (Id, Name, Address) VALUES (?, ?, ?)";

        try (Statement st = con.createStatement();
             PreparedStatement pst = con.prepareStatement(insertSql)) {

            st.executeUpdate(deleteMenu);
            st.executeUpdate(deleteRestaurant);

            Object[][] restaurants = {
                    {1, "Cafe Java", "FC Road, Pune"},
                    {2, "Spice Villa", "JM Road, Pune"},
                    {3, "Food Hub", "Baner, Pune"},
                    {4, "Tasty Treat", "Kothrud, Pune"},
                    {5, "Urban Bites", "Shivaji Nagar, Pune"},
                    {6, "Green Leaf", "Wakad, Pune"},
                    {7, "Royal Dine", "Aundh, Pune"},
                    {8, "Hungry Point", "Hinjewadi, Pune"},
                    {9, "The Curry House", "Viman Nagar, Pune"},
                    {10, "Street Flavors", "Pimpri, Pune"}
            };

            for (Object[] r : restaurants) {
                pst.setInt(1, (int) r[0]);
                pst.setString(2, (String) r[1]);
                pst.setString(3, (String) r[2]);
                pst.executeUpdate();
            }
        }

        System.out.println("10 records inserted into Restaurant table.");
    }

    public static void insertMenuItems(Connection con) throws SQLException {
        String insertSql = "INSERT INTO MenuItem (Id, Name, Price, ResId) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pst = con.prepareStatement(insertSql)) {
            Object[][] items = {
                    {101, "Pasta", 120.0, 1},
                    {102, "Pizza", 250.0, 1},
                    {103, "Burger", 90.0, 2},
                    {104, "Sandwich", 80.0, 3},
                    {105, "Coffee", 100.0, 1},
                    {106, "Paneer Tikka", 180.0, 4},
                    {107, "Paratha", 70.0, 5},
                    {108, "Noodles", 110.0, 6},
                    {109, "Pepsi", 50.0, 7},
                    {110, "Pav Bhaji", 95.0, 8}
            };

            for (Object[] i : items) {
                pst.setInt(1, (int) i[0]);
                pst.setString(2, (String) i[1]);
                pst.setDouble(3, (double) i[2]);
                pst.setInt(4, (int) i[3]);
                pst.executeUpdate();
            }
        }

        System.out.println("10 records inserted into MenuItem table.");
    }

    public static void selectMenuItemsPriceLessThan100(Connection con) throws SQLException {
        String sql = "SELECT * FROM MenuItem WHERE Price <= 100";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            printMenuItemResultSet(rs);
        }
    }

    public static void selectMenuItemsInCafeJava(Connection con) throws SQLException {
        String sql = "SELECT m.Id, m.Name, m.Price, m.ResId " +
                     "FROM MenuItem m " +
                     "JOIN Restaurant r ON m.ResId = r.Id " +
                     "WHERE r.Name = 'Cafe Java'";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            printMenuItemResultSet(rs);
        }
    }

    public static void updateMenuItemsPrice(Connection con) throws SQLException {
        String sql = "UPDATE MenuItem SET Price = 200 WHERE Price <= 100";

        try (Statement st = con.createStatement()) {
            int rows = st.executeUpdate(sql);
            System.out.println(rows + " record(s) updated.");
        }
    }

    public static void deleteMenuItemsStartingWithP(Connection con) throws SQLException {
        String sql = "DELETE FROM MenuItem WHERE Name LIKE 'P%'";

        try (Statement st = con.createStatement()) {
            int rows = st.executeUpdate(sql);
            System.out.println(rows + " record(s) deleted.");
        }
    }

    public static void displayAllMenuItems(Connection con) throws SQLException {
        String sql = "SELECT * FROM MenuItem";

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            printMenuItemResultSet(rs);
        }
    }

    public static void printMenuItemResultSet(ResultSet rs) throws SQLException {
        System.out.printf("%-10s %-20s %-10s %-10s%n", "ID", "NAME", "PRICE", "RESID");
        System.out.println("--------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-10d %-20s %-10.2f %-10d%n",
                    rs.getInt("Id"),
                    rs.getString("Name"),
                    rs.getDouble("Price"),
                    rs.getInt("ResId"));
        }
    }
}
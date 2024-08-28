import objects.Room;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class Database {

    public enum Table {

        LAB7_RESERVATIONS("lab7_reservations"),
        LAB7_ROOMS("lab7_rooms"),
        ;

        final String s;
        Table(String newS) {
            this.s = newS;
        }

        public String toString() {
            return this.s;
        }

    }

    public static void execute(String query) throws SQLException {
        System.out.println("Attempting to execute query: " + query);
        try (Connection conn = DriverManager.getConnection(
                ConnectionData.JDBC_URL.s,
                ConnectionData.DB_USER.s,
                ConnectionData.DB_PASSWORD.s)) {
            Statement statement = conn.createStatement();
            statement.execute(query);
        }
    }

    public static ArrayList<Reservation> getReservations() throws SQLException {
        ArrayList<Reservation> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                ConnectionData.JDBC_URL.s,
                ConnectionData.DB_USER.s,
                ConnectionData.DB_PASSWORD.s)) {

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + Table.LAB7_RESERVATIONS.s);
            while (rs.next()) {
                String CODE = rs.getString("CODE");
                String Room = rs.getString("Room");
                String CheckIn = rs.getString("CheckIn");
                String Checkout = rs.getString("Checkout");
                String Rate = rs.getString("Rate");
                String LastName = rs.getString("LastName");
                String FirstName = rs.getString("FirstName");
                String Adults = rs.getString("Adults");
                String Kids = rs.getString("Kids");

                Reservation reservation = new Reservation(Integer.parseInt(CODE), Room,
                        DataConversion.toDate(CheckIn), DataConversion.toDate(Checkout),
                        DataConversion.toBigDecimal(Rate),
                        LastName, FirstName,
                        Integer.parseInt(Adults), Integer.parseInt(Kids));

                list.add(reservation);

            }

            return list;
        }
    }

    public static ArrayList<Room> getRooms() throws SQLException {
        ArrayList<Room> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                ConnectionData.JDBC_URL.s,
                ConnectionData.DB_USER.s,
                ConnectionData.DB_PASSWORD.s)) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + Table.LAB7_ROOMS.s);
            while (rs.next()) {
                String RoomCode = rs.getString("RoomCode");
                String RoomName = rs.getString("RoomName");
                String Beds = rs.getString("Beds");
                String bedType = rs.getString("bedType");
                String maxOcc = rs.getString("maxOcc");
                String basePrice = rs.getString("basePrice");
                String decor = rs.getString("decor");
                Room newRoom = new Room(RoomCode, RoomName, Integer.parseInt(Beds),
                        bedType, Integer.parseInt(maxOcc), new BigDecimal(basePrice), decor);
                list.add(newRoom);
            }
        }

        return list;
    }

}

import objects.Room;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

// This class is used for functional requirement 2
// Precondition: data provided in constructor will be valid to be formatted
public class ReservationGenerator {

    String firstName, lastName;
    String desiredRoomCode, desiredBedType; // Could be blank, indicating any
    Date checkInDate, checkOutDate;
    int numberOfChildren, numberOfAdults;

    public ReservationGenerator(
            String newFirstName, String newLastName,
            String newDesiredRoomCode, String newDesiredBedType,
            Date newCheckInDate, Date newCheckOutDate,
            int newNumberOfChildren, int newNumberOfAdults) {
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.desiredRoomCode = newDesiredRoomCode;
        this.desiredBedType = newDesiredBedType;
        this.checkInDate = newCheckInDate;
        this.checkOutDate = newCheckOutDate;
        this.numberOfChildren = newNumberOfChildren;
        this.numberOfAdults = newNumberOfAdults;
    }


    // Return: Max number of occupations allowed
    public int getMaxOccupation() throws SQLException {
        try (Connection conn = DriverManager.getConnection(
                ConnectionData.JDBC_URL.s,
                ConnectionData.DB_USER.s,
                ConnectionData.DB_PASSWORD.s)) {

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT MAX(maxOcc)" +
                            " FROM " + Database.Table.LAB7_ROOMS.s);
            if (rs.next()) {
                String maxOcc = rs.getString(1);

                return Integer.parseInt(maxOcc);
            }
        }
        return 0;
    }

    // Return: If the desired data is conflicting with anything
    public boolean isConflictingData() {
        // Room = 'HBB' AND
        //    bedType = 'Queen' AND
        //    1 <= maxOcc
        WhereBuilder whereBuilder = new WhereBuilder();
        if (desiredRoomCode != null)
            whereBuilder.addCondition("Room", desiredRoomCode, true);
        if (desiredBedType != null)
            whereBuilder.addCondition("bedType", desiredBedType, true);

        // TODO add in more where conditions

        return false;
    }


    // Generate reservation based on the desired
    public Reservation getExactReservation() {

        /*
        int newCode, String newRoom, Date newCheckIn, Date newCheckout, BigDecimal newRate,
                       String newLastName, String newFirstName, int newAdults, int newKids
         */

        // Room rate

        // Room Code: TODO make sure it is a valid one instead of a random one
        Random rand = new Random(); //instance of random class

        // generate random values from 0-9999999
        int randomReservationCode = rand.nextInt(9999999);

        return new Reservation(
                randomReservationCode, // TODO generate lab7_reservations.CODE
                desiredRoomCode,
                checkInDate,
                checkOutDate,
                BigDecimal.valueOf(100), // TODO get rate
                lastName,
                firstName,
                numberOfAdults,
                numberOfChildren
        );
    }

    public ArrayList<Reservation> getReservations() throws SQLException {
        ArrayList<Reservation> reservations = new ArrayList<>();

        ArrayList<Room> roomsInDatabase = Database.getRooms();
        ArrayList<Reservation> reservationsInDatabase = Database.getReservations();

        return reservations;
    }

    // Generates 5 reservations that are similar, but not an exact match
    public ArrayList<Reservation> generateSimilar() {
        ArrayList<Reservation> reservations = new ArrayList<>();

        return reservations;
    }

}

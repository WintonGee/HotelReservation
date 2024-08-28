import objects.Room;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;

public class Reservation {

    /*
    CODE int(11) PRIMARY KEY,
    Room char(5) NOT NULL,
    CheckIn date NOT NULL,
    CheckOut date NOT NULL,
    Rate DECIMAL(6,2) NOT NULL,
    LastName varchar(15) NOT NULL,
    FirstName varchar(15) NOT NULL,
    Adults int(11) NOT NULL,
    Kids int(11) NOT NULL,
    FOREIGN KEY (Room) REFERENCES lab7_rooms (RoomCode)
     */

    public int CODE;
    public String Room;
    public Date CheckIn, CheckOut;
    public BigDecimal Rate;
    public String LastName, FirstName;
    public int Adults, Kids;

    public Reservation(int newCode, String newRoom, Date newCheckIn, Date newCheckout, BigDecimal newRate,
                       String newLastName, String newFirstName, int newAdults, int newKids) {
        this.CODE = newCode;
        this.Room = newRoom;
        this.CheckIn = newCheckIn;
        this.CheckOut = newCheckout;
        this.Rate = newRate;
        this.LastName = newLastName;
        this.FirstName = newFirstName;
        this.Adults = newAdults;
        this.Kids = newKids;
    }


    public String toString() {
        return CODE + ", " + Room + ", " + CheckIn + ", " + CheckOut + ", " +
                Rate + ", " + LastName + ", " + FirstName + ", " + Adults + ", " + Kids;
    }

    /* Example
    INSERT INTO lab7_reservations
    (CODE, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids)
    VALUES (10100, 'HBB', '2021-12-23', '2021-12-25', 100.00, 'LastName', 'Conrad', 1, 1)
     */
    // Return: The string used to insert this reservation into the sql database
    // TODO test
    public String getSqlInsertString() {
        return "INSERT INTO lab7_reservations " +
                "(CODE, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) " +
                "VALUES (" +
                CODE + "," +
                DataConversion.toSQLFormation(Room) + "," +
                DataConversion.toSQLFormation(CheckIn.toString()) + "," +
                DataConversion.toSQLFormation(CheckOut.toString()) + "," +
                Rate + "," +
                DataConversion.toSQLFormation(LastName) + "," +
                DataConversion.toSQLFormation(FirstName) + "," +
                Adults + "," +
                + Kids
                + ")";
    }

    public long getDaysStayed() {
        LocalDate dateBefore = LocalDate.parse(CheckIn.toString());
        LocalDate dateAfter = LocalDate.parse(CheckOut.toString());

        return ChronoUnit.DAYS.between(dateBefore, dateAfter) + 1;
    }

    public double getRevenue() {
        return Rate.doubleValue() * (double) getDaysStayed();
    }

    public String getRoomName() throws SQLException {

        ArrayList<Room> rooms = Database.getRooms();

        // Using Reservation.Room, get the full name of the room from Room.RoomCode
        Optional<String> room = rooms.stream()
                .filter(c -> c.RoomCode.equals(Room))
                .map(c -> c.RoomName)
                .findFirst();

        return room.isEmpty() ? "" : room.get();
    }


}

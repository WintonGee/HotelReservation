import objects.Room;
import objects.RoomRevenue;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

public class FunctionalRequirements {

    // TODO
    // FR1: Rooms and Rates
    // Steps -> output a list of rooms to the user sorted by popularity, highest to lowest
    // - Room popularity score: number of days the room has been occupied during
    //      the previous 180 days divided by 180 (round to two decimal places)
    // - Next available check-in date
    // - Length in days and check out date of the most recent (completed) stay in the room
    // List of rooms: Popularity score, next available check-in date, length of previous checkout date, previous checkout date
    public static void roomAndRates_1() throws SQLException {
        String s =
        """
        SELECT *,
          (
            SELECT ROUND((CheckOut - CheckIn) / 180, 2) as Days
            FROM lab7_reservations
            WHERE lab7_reservations.Room = room1.RoomCode
              AND CheckIn > DATE_ADD(CURRENT_TIMESTAMP, INTERVAL -180 DAY)
              AND CheckOut < CURRENT_TIMESTAMP
          )  AS PopScore,
          (
            SELECT MIN(CheckOut)
            FROM lab7_reservations
            WHERE lab7_reservations.Room = room1.RoomCode
              AND CheckOut < CURRENT_TIMESTAMP
          ) AS nextAvailCheckIn,
          (
            SELECT MIN(CheckOut)
            FROM lab7_reservations
            WHERE lab7_reservations.Room = room1.RoomCode
              AND CheckOut < CURRENT_TIMESTAMP
          ) AS recentCheckOut,
          (
            SELECT MIN(CheckOut - CheckIn)
            FROM lab7_reservations
            WHERE lab7_reservations.Room = room1.RoomCode
              AND CheckOut = recentCheckOut
          ) AS recentStayLength
        FROM lab7_rooms room1
        ORDER BY PopScore DESC
        """;
        try (Connection conn = DriverManager.getConnection(
                ConnectionData.JDBC_URL.s,
                ConnectionData.DB_USER.s,
                ConnectionData.DB_PASSWORD.s)) {

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(s);

            while (rs.next()) {
                String roomCode = rs.getString("RoomCode");
                String roomName = rs.getString("RoomName");
                String popScore = rs.getString("PopScore");
                String nextAvailCheckIn = rs.getString("nextAvailCheckIn");
                String recentCheckOut = rs.getString("recentCheckOut");
                String recentStayLength = rs.getString("recentStayLength");


                System.out.println(roomCode + ", " + roomName + ", PopScore: " + popScore + ", Available Check In:" +
                        nextAvailCheckIn + ", Recent Check Out:" + recentCheckOut + ", Recent Stay length:" + recentStayLength);
            }

        }

    }


    // TODO
    // FR2: Reservations
    // Steps -> system shall accept from the user the following information:
    // - First name
    // - Last name
    // - A room code to indicate the specific room desired (or “Any” to indicate no preference)
    // - A desired bed type (or “Any” to indicate no preference)
    // - Begin date of stay
    // - End date of stay
    // - Number of children
    // - Number of adults
    // Condition:
    // - maximum room occupancy must be considered
    // - the dates must not overlap with another existing reservation
    // Output:
    // (Available) a numbered list of available rooms w/ booking by option number
    // (None) 5 possibilities should be chosen based on similarity to the desired reservation
    // Allow the user to cancel or confirm
    // Confirmation Screen Output:
    // - First name, last name
    // - Room code, room name, bed type
    // - Begin and end date of stay
    // - Number of adults
    // - Number of children
    // - Total cost of stay, based on a sum of the following:
    // - Number of weekdays multiplied by room base rate
    // – Number of weekend days multiplied by 110% of the room base rate
    public static void reservations_2() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        // First name
        System.out.println("Enter First Name: ");
        String firstName = scanner.nextLine();
        if (firstName.length() == 0) {
            System.out.println("No First Name Entered");
            return;
        }

        // Last name
        System.out.println("Enter Last Name: ");
        String lastName = scanner.nextLine();
        if (lastName.length() == 0) {
            System.out.println("No Last Name Entered");
            return;
        }

        // A room code to indicate the specific room desired (or “Any” to indicate no preference)
        System.out.println("Enter Room Code Of Desired Room (Blank if Any): ");
        String desiredRoomCode = scanner.nextLine();
        if (desiredRoomCode.length() > 0) {
            // TODO desired room
            // Return if the room code does not exist in the database
//            return;
        }

        // A desired bed type (or “Any” to indicate no preference)
        System.out.println("Enter Type Of Desired Bed (Blank if Any): ");
        String desiredBedType = scanner.nextLine();
        if (desiredBedType.length() > 0) {
            // TODO desired bed type
            // Return if it is not a valid bed type
//            return;
        }

        // Begin date of stay
        System.out.println("Enter Begin Date Of Stay (yyyy-mm-dd): ");
        String beginDate = scanner.nextLine();
        if (beginDate.length() == 0) {
            System.out.println("No Begin Date Entered");
            return;
        }

        if (!DataChecker.isValidDate(beginDate)) {
            System.out.println("Invalid Date Formatting");
            return;
        }

        // End date of stay
        System.out.println("Enter End Date Of Stay (yyyy-mm-dd): ");
        String endDate = scanner.nextLine();
        if (endDate.length() == 0) {
            System.out.println("No End Date Entered");
            return;
        }

        if (!DataChecker.isValidDate(endDate)) {
            System.out.println("Invalid Date Formatting");
            return;
        }

        // Number of children
        System.out.println("Enter Number Of Children: ");
        String numberOfChildren = scanner.nextLine();
        if (!DataChecker.isValidInteger(numberOfChildren)) {
            System.out.println("Invalid Entry: Integer needed");
            return;
        }

        // Number of adults
        System.out.println("Enter Number Of Adults: ");
        String numberOfAdults = scanner.nextLine();
        if (!DataChecker.isValidInteger(numberOfAdults)) {
            System.out.println("Invalid Entry: Integer needed");
            return;
        }

        // TODO
        // Allow the user to choose from a set of options
        // - All the possible ones, or generate 5 similar ones if none possible
        // - Output the option selections based on the index, make sure the index is not out of bound.
        ReservationGenerator reservationGenerator = new ReservationGenerator(
                firstName, lastName,
                desiredRoomCode, desiredBedType,
                DataConversion.toDate(beginDate), DataConversion.toDate(endDate),
                Integer.parseInt(numberOfChildren), Integer.parseInt(numberOfAdults)
        );

        // Checking if request exceeds the maximum capacity and check if any persons are requested
        int occupationRequest = Integer.parseInt(numberOfChildren) + Integer.parseInt(numberOfAdults);
        if (occupationRequest == 0) {
            System.out.println("Reservation needs to contain at least one person!");
            return;
        }

        int maxOccupation = reservationGenerator.getMaxOccupation();
        if (maxOccupation < occupationRequest) {
            System.out.println("No room with enough space, Requested: " + occupationRequest + ", Max: " + maxOccupation);
            return;
        }

        // TODO build method
        Reservation tempReservation = reservationGenerator.getExactReservation();

        // Allow the user to cancel or confirm
        System.out.println("Confirm Reservation (Y/N): ");
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Failed to confirm reservation");
            return;
        }

        Database.execute(tempReservation.getSqlInsertString());

        // Output of reservation confirmation
        new ReservationConfirmation(tempReservation).showInformation();
    }


    // FR3: Reservation Change
    // Status: TODO make sure the date change does not conflict with another reservation in the system
    // Allow the user to provide a new value or to indicate “no change” for a given field
    // Steps -> system shall accept from the user reservation code and any of the following information:
    // - First name
    // - Last name
    // - Begin date: Check if this new begin date is valid
    // - End date: Check if new end date is valid
    // - Number of children
    // - Number of adults
    public static void reservationChange_3() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        // Get reservation code
        System.out.println("Enter Reservation Code: ");
        String reservationCode = scanner.nextLine();
        if (!DataChecker.isValidInteger(reservationCode)) {
            System.out.println("Invalid Reservation Code: " + reservationCode);
            return;
        }

        // Check if reservation code exists in the database, end if it does not
        Optional<Reservation> reservation = Database.getReservations().stream()
                .filter(c -> c.CODE == Integer.parseInt(reservationCode)).findFirst();
        if (reservation.isEmpty()) {
            System.out.println("Reservation code does not exist in database: " + reservationCode);
            return;
        }

        String change;
        SetBuilder setBuilder = new SetBuilder();
        // - First name
        System.out.println("Change First Name (Y/N): ");
        change = scanner.nextLine();
        if (change.equalsIgnoreCase("Y")) {
            System.out.println("New First Name: ");
            String newValue = scanner.nextLine();
            setBuilder.addSet("FirstName", newValue, true);
        }

        // - Last name
        System.out.println("Change Last Name (Y/N): ");
        change = scanner.nextLine();
        if (change.equalsIgnoreCase("Y")) {
            System.out.println("New Last Name: ");
            String newValue = scanner.nextLine();
            setBuilder.addSet("LastName", newValue, true);
        }

        // - Begin date: Check if this new begin date is valid
        System.out.println("Change Begin Date (Y/N): ");
        String changeBeginDate = scanner.nextLine();
        String newBeginDate = "";
        if (changeBeginDate.equalsIgnoreCase("Y")) {
            System.out.println("New Begin Date (YYYY-MM-DD): ");
            newBeginDate = scanner.nextLine();
            if (!DataChecker.isValidDate(newBeginDate)) {
                System.out.println("Invalid Date or Formatting");
                return;
            }
            setBuilder.addSet("CheckIn", newBeginDate, true);
        }

        // - End date: Check if new end date is valid
        System.out.println("Change End Date (Y/N): ");
        String changeEndDate = scanner.nextLine();
        String newEndDate = "";
        if (changeEndDate.equalsIgnoreCase("Y")) {
            System.out.println("New End Date (YYYY-MM-DD): ");
            newEndDate = scanner.nextLine();
            if (!DataChecker.isValidDate(newEndDate)) {
                System.out.println("Invalid Date or Formatting");
                return;
            }
            setBuilder.addSet("CheckOut", newEndDate, true);
        }

        // Check if the date range will be valid
        String beginDateToCheck = changeBeginDate.equalsIgnoreCase("Y") ? newBeginDate
                : reservation.get().CheckIn.toString();
        String endDateToCheck = changeEndDate.equalsIgnoreCase("Y") ? newEndDate
                : reservation.get().CheckOut.toString();
        boolean isValidDateRange = DataChecker.isValidDateRange(beginDateToCheck, endDateToCheck);
        if (!isValidDateRange) {
            System.out.println("Invalid Date Range");
            return;
        }

        // TODO Check if the date will conflict with any other reservations

        // - Number of children
        System.out.println("Change Number of children (Y/N): ");
        change = scanner.nextLine();
        if (change.equalsIgnoreCase("Y")) {
            System.out.println("New number of children: ");
            String newValue = scanner.nextLine();
            if (!DataChecker.isValidInteger(newValue)) {
                System.out.println("Invalid Integer");
                return;
            }
            setBuilder.addSet("Kids", newValue, false);
        }

        // - Number of adults
        System.out.println("Change Number of adults (Y/N): ");
        change = scanner.nextLine();
        if (change.equalsIgnoreCase("Y")) {
            System.out.println("New number of adults: ");
            String newValue = scanner.nextLine();
            if (!DataChecker.isValidInteger(newValue)) {
                System.out.println("Invalid Integer");
                return;
            }
            setBuilder.addSet("Adults", newValue, false);
        }

        // No changes
        if (setBuilder.sets.size() == 0) {
            System.out.println("No changes being made, skipping reservation change");
            return;
        }

        // Execute query
        WhereBuilder whereBuilder = new WhereBuilder();
        whereBuilder.addCondition("CODE = " + reservationCode);

        Database.execute("UPDATE " + Database.Table.LAB7_RESERVATIONS.s
                + setBuilder.build() + whereBuilder.build());
    }


    // FR4: Reservation Cancellation
    // Status: Complete and tested
    // Steps -> system shall accept from the user reservation code
    // - confirm cancellation
    // - remove from database
    public static void reservationCancellation_4() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        // Get reservation code
        System.out.println("Enter Reservation Code: ");
        String reservationCode = scanner.nextLine();
        if (!DataChecker.isValidInteger(reservationCode)) {
            System.out.println("Invalid Reservation Code: " + reservationCode);
            return;
        }

        // Get Cancellation Confirmation
        System.out.println("Confirm Cancellation (Y/N): ");
        String confirmation = scanner.nextLine();
        if (!confirmation.equalsIgnoreCase("Y")) {
            System.out.println("Failed to confirm Cancellation");
            return;
        }

        // Check if reservation code exists in the database, end if it does not
        boolean inDatabase = Database.getReservations().stream()
                .anyMatch(c -> c.CODE == Integer.parseInt(reservationCode));
        if (!inDatabase) {
            System.out.println("Reservation code does not exist in database: " + reservationCode);
            return;
        }

        // Remove from database
        // DELETE EXAMPLE: DELETE FROM table_name WHERE condition;
        WhereBuilder where = new WhereBuilder();
        where.addCondition("CODE = " + reservationCode);
        Database.execute("DELETE FROM " + Database.Table.LAB7_RESERVATIONS.s + where.build());
    }


    // FR5: Detailed Reservation Information
    // Status: Complete and tested
    // Steps -> system shall accept from the user any of the following:
    // - First name
    // - Last name
    // - A range of dates
    // - Room code
    // - Reservation code
    // Output: display a list of all matching reservations found in the database.
    public static void detailedReservationInformation_5() throws SQLException {
        WhereBuilder where = new WhereBuilder();
        Scanner scanner = new Scanner(System.in);

        // - First name
        System.out.println("First Name (Blank if any): ");
        String firstName = scanner.nextLine();
        if (firstName.length() > 0) {
            where.addCondition("FirstName", firstName, true, "LIKE");
        }

        // - Last name
        System.out.println("Last Name (Blank if any): ");
        String lastName = scanner.nextLine();
        if (lastName.length() > 0) {
            where.addCondition("LastName", lastName, true, "LIKE");
        }

        // - After a date
        System.out.println("After Date (Blank if any): ");
        String afterDate = scanner.nextLine();
        if (afterDate.length() > 0) {
            if (!DataChecker.isValidDate(afterDate)) {
                System.out.println("Invalid After Date");
                return;
            }
            where.addCondition("'" + afterDate + "' < CheckIn");
            where.addCondition("'" + afterDate + "' < CheckOut");
        }

        // - Before a date
        System.out.println("Before Date (Blank if any): ");
        String beforeDate = scanner.nextLine();
        if (beforeDate.length() > 0) {
            if (!DataChecker.isValidDate(beforeDate)) {
                System.out.println("Invalid Before Date");
                return;
            }
            where.addCondition("'" + beforeDate + "' > CheckOut");
            where.addCondition("'" + beforeDate + "' > CheckIn");
        }

        // - Room code
        System.out.println("Room code (Blank if any): ");
        String roomCode = scanner.nextLine();
        if (roomCode.length() > 0) {
            where.addCondition("Room", roomCode, true);
        }

        // - Reservation code
        System.out.println("Reservation code (Blank if any): ");
        String reservationCode = scanner.nextLine();
        if (reservationCode.length() > 0) {
            if (!DataChecker.isValidInteger(reservationCode)) {
                System.out.println("Invalid Integer");
                return;
            }
            where.addCondition("CODE = " + Integer.parseInt(reservationCode));
        }

        // Print
        try (Connection conn = DriverManager.getConnection(
                ConnectionData.JDBC_URL.s,
                ConnectionData.DB_USER.s,
                ConnectionData.DB_PASSWORD.s)) {

            ArrayList<Room> rooms = Database.getRooms();

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " +
                    Database.Table.LAB7_RESERVATIONS.s + where.build());
            System.out.println("Debug: Attempting to print: " + "SELECT * FROM " +
                    Database.Table.LAB7_RESERVATIONS.s + where.build());
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

                // Using Reservation.Room, get the full name of the room from Room.RoomCode
                Optional<String> room = rooms.stream()
                        .filter(c -> c.RoomCode.equals(Room))
                        .map(c -> c.RoomName)
                        .findFirst();

                System.out.println(room.isEmpty() ? "" : room.get() + ", " + reservation);
            }
        }

    }


    // FR6: Revenue
    // Status: Complete and tested
    // Steps -> system shall provide a month-by-month overview of revenue for the current calendar year
    // - Use SQL‘s CURRENT DATE to determine the current year
    // - Use CheckOut date to determine the month and current year
    // - Round to the nearest whole dollar
    // - Each room: 13 Columns: 12 Columns - 1 per month ; 1 Column - Totals
    public static void revenue_6() throws SQLException {
        ArrayList<Reservation> reservations = getCurrentYearReservations();

        // Loading room revenues
        ArrayList<RoomRevenue> roomRevenues = new ArrayList<>();

        // Init RoomRevenue objects for each Room - Distinct
        for (Reservation reservation : reservations) {
            String roomCode = reservation.Room;
            boolean shouldInitRoom = roomRevenues.stream()
                    .noneMatch(roomRev -> roomRev.isSameRoom(roomCode));
            if (shouldInitRoom)
                roomRevenues.add(new RoomRevenue(roomCode));
        }

        // Adding in revenues
        reservations
                .forEach(res -> {
                    String roomCode = res.Room;
                    int month = res.CheckOut.toLocalDate().getMonthValue();
                    double revenue = res.getRevenue();

                    roomRevenues.forEach(c -> {
                        if (c.isSameRoom(roomCode))
                            c.addMonthRevenue(month, revenue);
                    });
                });

        roomRevenues.forEach(RoomRevenue::showInformation);
    }

    // Get the reservations of the current year, used for functional requirement 6
    public static ArrayList<Reservation> getCurrentYearReservations() throws SQLException {
        ArrayList<Reservation> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(
                ConnectionData.JDBC_URL.s,
                ConnectionData.DB_USER.s,
                ConnectionData.DB_PASSWORD.s)) {

            // Use SQL‘s CURRENT DATE to determine the current year
            WhereBuilder whereBuilder = new WhereBuilder();
            whereBuilder.addCondition("YEAR(CURRENT_DATE) = YEAR(CheckOut)");

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM " + Database.Table.LAB7_RESERVATIONS.s
                    + whereBuilder.build());
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

}

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// This class is used for Functional Requirement 2, Used for printing the confirmation output
public class ReservationConfirmation {

    Reservation reservation;

    // Confirmation Screen Output:
    // - First name, last name
    // - Room code, room name, bed type
    // - Begin and end date of stay
    // - Number of adults
    // - Number of children
    // - Total cost of stay, based on a sum of the following:
    // - Number of weekdays multiplied by room base rate
    // – Number of weekend days multiplied by 110% of the room base rate

    public ReservationConfirmation(Reservation newReservation) {
        this.reservation = newReservation;
    }

    public void showInformation() throws SQLException {
        System.out.println("Room Confirmed: ");
        System.out.println("Name: " + reservation.FirstName + ", " + reservation.LastName);
        System.out.println("Room: " + reservation.Room + ", " + reservation.getRoomName() + ", "); // TODO bedType
        System.out.println("Check In and Check Out: " + reservation.CheckIn + ", " + reservation.CheckOut);
        System.out.println("Number of Adults: " + reservation.Adults);
        System.out.println("Number of Children: " + reservation.Kids);
        double weekdayCosts = getWeekdayCosts(), weekendCosts = getWeekendCosts(),
                totalCosts = weekdayCosts + weekendCosts;
        System.out.println("Total cost of stay: " + totalCosts);
        System.out.println("Weekday Costs: " + weekdayCosts);
        System.out.println("Weekend Costs: " + weekendCosts);
    }

    // Return: Total cost of reservation for the weekdays
    public double getWeekdayCosts() {
        return getDatesBetween(reservation.CheckIn, reservation.CheckOut)
                .stream()
                .filter(c -> !isWeekend(c))
                .mapToDouble(c -> reservation.Rate.doubleValue())
                .sum();
    }

    // Return: Total cost of reservation for the weekends
    public double getWeekendCosts() {
        return getDatesBetween(reservation.CheckIn, reservation.CheckOut)
                .stream()
                .filter(this::isWeekend)
                .mapToDouble(c -> reservation.Rate.doubleValue() * 1.1)
                .sum();
    }

    // TODO check/test if the start and end dates are included
    public static List<LocalDate> getDatesBetween(Date startDate, Date endDate) {
        ArrayList<LocalDate> list = startDate
                .toLocalDate()
                .datesUntil(endDate.toLocalDate())
                .collect(Collectors.toCollection(ArrayList::new));

        list.add(startDate.toLocalDate());

        return list;
    }

    // Param: String:Day
    // Return: If the day is a weekend
    public boolean isWeekend(LocalDate date) {
        String day = date.getDayOfWeek().toString();
        return day.equalsIgnoreCase("SATURDAY") || day.equalsIgnoreCase("SUNDAY");
    }


}

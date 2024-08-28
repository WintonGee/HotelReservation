import java.sql.Date;

// Used for checking if data type is correct/valid.
public class DataChecker {

    // Valid integer
    // Params: String string
    // Return: string is valid integer
    public static boolean isValidInteger(String string) {
        return string.chars().allMatch(Character::isDigit);
    }

    // Valid date
    // Params: String string
    // Return: string is valid date
    public static boolean isValidDate(String string) {
        try {
            Date.valueOf(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Valid date range
    // Params: String: checkIn, String: checkOut
    // Return: checkIn is valid date, checkOut is valid date, checkIn <= checkOut
    public static boolean isValidDateRange(String checkIn, String checkOut) {
        return isValidDate(checkIn) && isValidDate(checkOut)
                && DataConversion.toDate(checkIn).compareTo(DataConversion.toDate(checkOut)) <= 0;
    }



}

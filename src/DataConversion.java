import java.math.BigDecimal;
import java.sql.Date;

// This class will be used for converting ANSI SQL Data Type to Java Type
// PreCondition for all functions in this class: All strings are valid for the data type
public class DataConversion {

    public static Date toDate(String s) {
        return Date.valueOf(s);
    }

    public static BigDecimal toBigDecimal(String s) {
        return new BigDecimal(s);
    }

    // Return: String with the ' on both sides of the string
    public static String toSQLFormation(String s) {
       return "'" + s + "'";
    }


}

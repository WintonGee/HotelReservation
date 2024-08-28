import java.util.ArrayList;

// This class will be used to build set statements
public class SetBuilder {

    ArrayList<String> sets = new ArrayList<>();

    public String build() {
        if (sets.size() == 0)
            return "";
        String string = " SET ";
        string = string + String.join(", ", sets);

        return string;
    }


    public void addSet(String set) {
        this.sets.add(set);
    }

    /* Example
        UPDATE employees
        SET
            address = '1300 Carter St',
            city = 'San Jose',
            postalcode = 95125,
            region = 'CA'
        WHERE
            employeeID = 3;
         */
    public void addSet(String key, String value, boolean addQuotations) {
        String s = "";

        s = s + key + " = ";

        if (addQuotations)
            s = s + "'";

        s = s + value;

        if (addQuotations)
            s = s + "'";

        this.sets.add(s);
    }


}

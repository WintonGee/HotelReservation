import java.util.ArrayList;

// This class is used for building the where condition, which will be used for sending queries.
public class WhereBuilder {

    ArrayList<String> conditions = new ArrayList<>();

    public String build() {
        if (conditions.size() == 0)
            return "";
        String string = " WHERE ";
        string = string + String.join(" AND ", conditions);

        return string;
    }

    public void addCondition(String condition) {
        this.conditions.add(condition);
    }

    public void addCondition(String key, String value, boolean addQuotations) {
        addCondition(key, value, addQuotations, "=");
    }

    public void addCondition(String key, String value, boolean addQuotations, String operator) {
        String s = "";

        s = s + key + " " + operator + " ";

        if (addQuotations)
            s = s + "'";

        s = s + value;

        if (addQuotations)
            s = s + "'";

        this.conditions.add(s);
    }


}

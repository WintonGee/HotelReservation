package objects;

import java.util.HashMap;

// This class is used for Functional Requirement 6, Used for storing the revenue in a month
public class RoomRevenue {
    public String room;
    HashMap<Integer, Double> monthRevenue = new HashMap<>();

    public RoomRevenue(String newRoom) {
        this.room = newRoom;
    }

    public boolean isSameRoom(String newRoom) {
        return room.equals(newRoom);
    }

    public void addMonthRevenue(int month, double revenue) {
        monthRevenue.put(month, monthRevenue.getOrDefault(month, 0.0) + revenue);
    }

    // Format: RoomCode, 1:Rev, 2:Rev, 3:Rev, ... , 12:Rev, Total Rev
    public void showInformation() {
        StringBuilder s = new StringBuilder();
        s.append("Room:").append(room).append(" | ");

        s.append("Months ");
        double total = 0;
        for (int i = 1; i <= 12; i++) {
            s.append(i).append(":");
            double amount = monthRevenue.containsKey(i) ? monthRevenue.get(i) : 0;
            s.append(Math.round(amount)).append(" | ");
            total += amount;
        }

        s.append("Total:").append(Math.round(total));

        System.out.println(s);
    }

}
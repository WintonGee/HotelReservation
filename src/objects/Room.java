package objects;

import java.math.BigDecimal;

public class Room {

    /*
    RoomCode char(5) PRIMARY KEY,
    RoomName varchar(30) NOT NULL,
    Beds int(11) NOT NULL,
    bedType varchar(8) NOT NULL,
    maxOcc int(11) NOT NULL,
    basePrice DECIMAL(6,2) NOT NULL,
    decor varchar(20) NOT NULL,
    UNIQUE (RoomName)
     */

    public String RoomCode;
    public String RoomName;
    int Beds;
    String bedType;
    int maxOcc;
    BigDecimal basePrice;
    String decor;

    public Room(String newRoomCode, String newRoomName, int newBeds, String newBedType,
                int newMaxOcc, BigDecimal newBasePrice, String newDecor) {
        this.RoomCode = newRoomCode;
        this.RoomName = newRoomName;
        this.Beds = newBeds;
        this.bedType = newBedType;
        this.maxOcc = newMaxOcc;
        this.basePrice = newBasePrice;
        this.decor = newDecor;
    }

    public String toString() {
        return RoomCode + ", " + RoomName + ", " + Beds + ", " + bedType + ", " +
                maxOcc + ", " + basePrice + ", " + decor;
    }

}

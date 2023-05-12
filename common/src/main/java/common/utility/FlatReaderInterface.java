package common.utility;

import common.data.*;

public interface FlatReaderInterface {
    public Flat read();
    String readName();
    Coordinates readCoordinates();
    int readArea();
    long readNumberOfRooms();
    long readNumberOfBathrooms();
    Furnish readFurnish();
    View readView();
    House readHouse();

}

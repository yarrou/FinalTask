package building.floors;


import passengers.Passenger;
import service.Direction;

import java.util.Queue;

public abstract class Floor {
    public abstract int getNumber();

    public abstract void newPeople(Passenger passenger);

    public abstract Queue<Passenger> getQuery(Direction direction);

    public boolean equals(Floor floor) {
        return getNumber() == floor.getNumber();
    }

}

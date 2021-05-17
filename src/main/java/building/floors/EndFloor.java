package building.floors;

import lombok.extern.slf4j.Slf4j;
import passengers.Passenger;
import service.Direction;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class EndFloor extends Floor {
    private final int number;
    private final Queue<Passenger> passengers;

    public EndFloor(int number) {
        this.number = number;
        this.passengers = new LinkedList<Passenger>();
    }

    public int getNumber() {
        return number;
    }

    public void newPeople(Passenger passenger) {
        synchronized (passengers) {
            log.info("пассажир стал в очередь на низ на {} этаже", number);
            passengers.add(passenger);
        }
    }

    @Override
    public Queue<Passenger> getQuery(Direction direction) {
        return passengers;
    }
}

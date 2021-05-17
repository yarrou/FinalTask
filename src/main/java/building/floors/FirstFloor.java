package building.floors;

import lombok.extern.slf4j.Slf4j;
import passengers.Passenger;
import service.Direction;

import java.util.LinkedList;
import java.util.Queue;

@Slf4j
public class FirstFloor extends Floor {
    private int number;

    public FirstFloor() {
        this.number = 1;
        this.passengers = new LinkedList<>();
    }

    public int getNumber() {
        return number;
    }

    private Queue<Passenger> passengers;

    public void newPeople(Passenger passenger) {
        synchronized (passengers) {
            log.info("пассажир встал в очередь на верх на 1 этаже");
            passengers.add(passenger);
        }
    }

    @Override
    public Queue<Passenger> getQuery(Direction direction) {
        return passengers;
    }
}

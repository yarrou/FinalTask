package building.floors;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import passengers.Passenger;
import service.Direction;

import java.util.LinkedList;
import java.util.Queue;

@Getter
@Slf4j
public class StandardFloor extends Floor {
    private final int number;

    public StandardFloor(int number) {
        this.number = number;
        this.passengersToUp = new LinkedList<>();
        this.passengersToDown = new LinkedList<>();
    }

    private Queue<Passenger> passengersToUp;
    private Queue<Passenger> passengersToDown;

    public void newPeople(Passenger passenger) {
        if (passenger.getRequiredFloor() - passenger.getFromFloor() > 0) {
            synchronized (passengersToUp) {
                passengersToUp.add(passenger);
                log.info("пассажир встал в очередь на верх на {} этаже", number);
            }
        } else {
            synchronized (passengersToDown) {
                passengersToDown.add(passenger);
                log.info("пассажир встал в очередь на низ на {} этаже", number);
            }
        }

    }

    @Override
    public Queue<Passenger> getQuery(Direction direction) {
        if (direction.equals(Direction.STOP)){
            return passengersToUp.size()>0?passengersToUp:passengersToDown;
        }
        return direction.equals(Direction.DOWN) ? passengersToDown : passengersToUp;
    }
}

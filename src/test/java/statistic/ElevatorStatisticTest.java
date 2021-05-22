package statistic;

import org.junit.jupiter.api.Test;
import passengers.Passenger;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorStatisticTest {

    @Test
    void addOne() {
        ElevatorStatistic elevatorStatistic = new ElevatorStatistic(1);
        Passenger passenger = new Passenger(100,1,2);
        elevatorStatistic.addOne(passenger);
        assertTrue(elevatorStatistic.getCountPassengers()==1);
        assertTrue(elevatorStatistic.getTotalWeight()==100);
    }
}
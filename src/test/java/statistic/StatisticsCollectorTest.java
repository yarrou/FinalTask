package statistic;

import org.junit.jupiter.api.Test;
import passengers.Passenger;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsCollectorTest {

    @Test
    void event() {
        int numberElevator = 1;
        int numberFloorFrom = 2;
        int numberFloorRequest = 3;
        Passenger passenger = new Passenger(100, numberFloorFrom, numberFloorRequest);
        StatisticsCollector collector = new StatisticsCollector();
        collector.initFloor(numberFloorFrom);
        collector.initFloor(numberFloorRequest);
        collector.initElevator(numberElevator);
        collector.event(passenger, numberElevator);
        assertTrue(collector.getElevatorsStatisticListNow().get(0).getTotalWeight() == 100);
        assertTrue(collector.getElevatorsStatisticListNow().get(0).getCountPassengers() == 1);
        FloorStatistic statisticFloor2 = collector.getFloorsStatisticsNow().stream().filter(fs -> fs.getNumber() == 2).findFirst().get();
        assertTrue(statisticFloor2.getCountLeft() == 1);
        FloorStatistic statisticFloor3 = collector.getFloorsStatisticsNow().stream().filter(fs -> fs.getNumber() == 3).findFirst().get();
        assertTrue(statisticFloor3.getCountArrived() == 1);
    }
}
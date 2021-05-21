package samples;

import building.Elevator;
import building.floors.Floor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import service.Dispatcher;
import statistic.StatisticsCollector;


import java.util.List;



public class ElevatorSample {
    @Mock
    private StatisticsCollector collector;

    public Elevator genElevator(int maxLoad, int speedOpenDoors, int speedOfMovement, int countFloors) {
        List<Floor> floors = FloorsSample.getFloors(countFloors);
        MockitoAnnotations.initMocks(this);
        return new Elevator(1, maxLoad, speedOpenDoors, speedOfMovement, floors, new Dispatcher(), collector);
    }
}

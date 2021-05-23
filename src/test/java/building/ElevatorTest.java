package building;

import building.floors.Floor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import passengers.Passenger;
import samples.ElevatorSample;
import service.Direction;
import service.Dispatcher;
import service.Goal;
import statistic.StatisticsCollector;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class ElevatorTest {
    private Elevator elevator;
    @Mock
    private StatisticsCollector statisticsCollector;
    @Mock
    private List<Floor> floors;
    @Mock
    Dispatcher dispatcher;

    private Queue<Passenger> generationTenPassenger() {
        Queue<Passenger> passengers = new LinkedList<>();
        Stream.iterate(1, n -> n + 1).limit(10).map(n -> new Passenger(80, 1, 2)).forEach(p -> passengers.add(p));
        return passengers;
    }

    @BeforeEach
    void before() {
        MockitoAnnotations.initMocks(this);
        elevator = (new ElevatorSample()).genElevator(250, 1, 3, 10);
    }

    @Test
    void constructor(){
        assertThrows(IllegalArgumentException.class,()->new Elevator(1,0,2,2,floors,dispatcher,statisticsCollector));
        assertThrows(IllegalArgumentException.class,()->new Elevator(1,200,0,2,floors,dispatcher,statisticsCollector));
        assertThrows(IllegalArgumentException.class,()->new Elevator(1,200,2,0,floors,dispatcher,statisticsCollector));
    }

    @Test
    void getPosition() {
        assertEquals(1, elevator.getPosition());
    }

    @Test
    void load() {
        Queue<Passenger> passengers = generationTenPassenger();
        Optional<Goal> goal = elevator.load(passengers);
        assertEquals(goal.get(), new Goal(Direction.UP, 1));
        assertTrue(elevator.getWorkLoad() <= elevator.getMaxLoad());
        assertTrue(elevator.isOccupied());
        assertTrue(elevator.countPassengers() + passengers.size() == 10);
        assertTrue(elevator.countPassengers() > 0);
        assertTrue(elevator.getPoints().contains(2));
    }

    @Test
    void move() {
        elevator.load(generationTenPassenger());
        elevator.move();
        assertTrue(elevator.getPosition() == 2);
    }

    @Test
    void unload() {
        elevator.load(generationTenPassenger());
        elevator.move();
        elevator.unload();
        assertTrue(elevator.getPoints().size() == 0);
        assertTrue(elevator.countPassengers() == 0);
    }


    @Test
    void liftDirection() {
        assertEquals(elevator.elevatorDirection(), Direction.STOP);
        elevator.load(generationTenPassenger());
        assertEquals(elevator.elevatorDirection(), Direction.UP);
    }


    @Test
    void onFloor() {
        elevator.load(generationTenPassenger());
        elevator.move();
        elevator.onFloor();
        assertTrue(elevator.getPoints().size() == 0);
        assertTrue(elevator.countPassengers() == 0);
    }
}
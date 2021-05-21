package building;

import building.floors.Floor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import passengers.Passenger;
import service.Dispatcher;
import service.Goal;
import service.Direction;
import statistic.StatisticsCollector;

import java.util.*;

@Slf4j
public class Elevator implements Runnable {

    private final int id;
    private boolean isInterrupted;
    private final Dispatcher dispatcher;//private final Queue<Goal> goals;//общая очередь вызовов лифта в здании
    private final int maxLoad;
    private final List<Floor> floors;
    private Direction requiredDirection;//указатель направления движения после загрузки пассажира, не является фактическим направлением движения
    Set<Passenger> passengers;
    private int position;
    private final int speedOpenDoors;
    private final int speedOfMovement;
    private SortedSet<Integer> points;
    private final StatisticsCollector collector;


    public Elevator(int id, int maxLoad, int speedOpenDoors, int speedOfMovement, List<Floor> floors, Dispatcher dispatcher, StatisticsCollector collector) {
        this.id = id;
        this.maxLoad = maxLoad;
        this.speedOpenDoors = speedOpenDoors;
        this.speedOfMovement = speedOfMovement;
        this.position = 1;
        this.passengers = new HashSet<>();
        this.floors = floors;
        this.requiredDirection = Direction.STOP;
        this.points = new TreeSet<>();
        this.dispatcher = dispatcher;
        this.collector = collector;
        collector.initElevator(id);
    }

    public int getWorkLoad() {
        return passengers.stream().map((passenger) -> passenger.getWeight()).reduce((weight, weightNextPassenger) -> weight + weightNextPassenger).orElse(0);
    }

    public int getMaxLoad() {
        return maxLoad;
    }

    public int getPosition() {
        return position;
    }

    public int countPassengers() {
        return passengers.size();
    }

    public Set<Integer> getPoints() {
        return Set.copyOf(points);
    }

    public void newStatisticEvent(Passenger passenger) {
        collector.event(passenger, id);
    }

    public Direction getRequiredDirection() {
        return requiredDirection;
    }

    // если в очереди остались пассажиры создается новая задача
    public Optional<Goal> load(Queue<Passenger> passengerQueue) {
        synchronized (passengerQueue) {
            while (loadOne(passengerQueue.peek())) {
                Passenger passenger = passengerQueue.poll();
                log.warn("в лифт №{} на {} этаже входит пассажир", id, position);
                points.add(passenger.getRequiredFloor());
            }
            if (passengerQueue.size() > 0) {
                return Optional.of(new Goal(passengerQueue.peek()));
            } else return Optional.empty();
        }
    }

    private boolean loadOne(Passenger passenger) {
        if (passenger == null) return false;
        else if (getWorkLoad() + passenger.getWeight() <= maxLoad) {
            passengers.add(passenger);
            return true;
        } else return false;
    }

    public void unload() {
        points.remove(getPosition());
        //passengers.stream().filter(passenger -> passenger.getRequiredFloor() == getPosition()).forEach(passenger -> unloadOne(passenger));
        Iterator<Passenger> iterator = passengers.iterator();
        while (iterator.hasNext()) {
            Passenger passenger = iterator.next();
            if (passenger.getRequiredFloor() == position) {
                iterator.remove();
                log.warn("пассажир покинул лифт №{} на {} этаже", id, position);
                newStatisticEvent(passenger);
            }
        }
    }


    public void moveUp() {
        position++;
    }

    public void moveDown() {
        position--;
    }

    public Direction elevatorDirection() {
        if (points.size() > 0) {
            if (points.last() > position) return Direction.UP;
            else if (points.last() < position) return Direction.DOWN;
            else return Direction.STOP;
        } else return Direction.STOP;
    }


    public void move() {
        try {
            Thread.sleep(speedOfMovement * 1000);
        } catch (InterruptedException e) {
            log.error("лифт застрял", e);
        }
        if (elevatorDirection().equals(Direction.DOWN)) moveDown();
        if (elevatorDirection().equals(Direction.UP)) moveUp();
    }

    @SneakyThrows
    public  void onFloor() {
        synchronized (dispatcher) {
            if (!isOccupied()) dispatcher.wait();
            if (points.contains(getPosition())) {
                log.info("лифт №{} приехал на {} этаж", id, position);
                openDoors();
                points.remove(getPosition());
                unload();
                Optional<Goal> goal = load(getCurrentFloor().getQuery(requiredDirection));
                closeDoors();
                if (goal.isPresent()) dispatcher.addGoal(goal.get());
            }
        }
    }

    private Floor getCurrentFloor() {
        for (Floor floor : floors) {
            if (floor.getNumber() == getPosition()) return floor;
        }
        return null;
    }

    public void callOnElevator(Goal goal) {
        points.add(goal.getFloorNumber());
        requiredDirection = goal.getDirection();
        log.info("лифт вызывается на {} этаж", goal.getFloorNumber());
    }


    public boolean isOccupied() {
        return points.size() > 0 ? true : false;
    }

    @Override
    public String toString() {
        return "Lift{" +
                " id=" + id +
                ", move=" + requiredDirection +
                ",count passengers=" + passengers.size() +
                ", position=" + position +
                ", points=" + points.size() +
                '}';
    }

    @SneakyThrows
    @Override
    public void run() {
        while (!isInterrupted()) {
            log.info("№" + id);
            onFloor();
            move();
        }
    }

    public boolean isInterrupted() {
        return isInterrupted;
    }

    public void setInterrupted(boolean interrupted) {
        isInterrupted = interrupted;
    }

    @SneakyThrows
    private void openDoors() {
        Thread.sleep(speedOpenDoors * 1000);
    }

    @SneakyThrows
    private void closeDoors() {
        Thread.sleep(speedOpenDoors * 1000);
    }
}


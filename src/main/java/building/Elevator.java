package building;

import building.floors.Floor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import passengers.Passenger;
import service.Goal;
import service.Direction;

import java.util.*;

@Slf4j
public class Elevator implements Runnable {

    private boolean isInterrupted;
    private final Queue<Goal> goals;
    private final int maxLoad;
    private final List<Floor> floors;
    private Direction direction;
    Set<Passenger> passengers;
    private int position;
    private final int speedOpenDoors;
    private final int speedOfMovement;
    private SortedSet<Integer> points;

    private int getWorkLoad() {
        return passengers.stream().map((passenger) -> passenger.getWeight()).reduce((weight, weightNextPassenger) -> weight + weightNextPassenger).orElse(0);
    }


    public Elevator(int maxLoad, int speedOpenDoors, int speedOfMovement, List<Floor> floors, Queue<Goal> goals) {
        this.maxLoad = maxLoad;
        this.speedOpenDoors = speedOpenDoors;
        this.speedOfMovement = speedOfMovement;
        this.position = 1;
        this.passengers = new HashSet<>();
        this.floors = floors;
        this.direction = Direction.STOP;
        this.points = new TreeSet<>();
        this.goals = goals;
    }

    public int getPosition() {
        return position;
    }

    public Optional<Goal> load(Queue<Passenger> passengerQueue) {
        synchronized (passengerQueue) {
            while (loadOne(passengerQueue.peek())) {
                Passenger passenger = passengerQueue.poll();
                log.warn("в лифт на {} этаже входит пассажир", position);
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
                log.warn("пассажир покинул лифт на {} этаже", position);
            }
        }
    }


    public void moveUp() {
        position++;
        log.info("лифт приехал на {} этаж", position);
    }

    public void moveDown() {
        position--;
        log.info("лифт приехал на {} этаж", position);
    }

    public Direction liftDirection() {
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
        if (liftDirection().equals(Direction.DOWN)) moveDown();
        if (liftDirection().equals(Direction.UP)) moveUp();
    }

    public void onFloor() {

        if (points.contains(getPosition())) {
            openDoors();
            points.remove(getPosition());
            unload();
            Optional<Goal> goal = load(getCurrentFloor().getQuery(direction));
            closeDoors();
            synchronized (goals) {
                if (goal.isPresent()) goals.add(goal.get());
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
        direction = goal.getDirection();
        log.info("лифт вызывается на {} этаж", goal.getFloorNumber());
    }


    public boolean isOccupied() {
        return points.size() > 0 ? true : false;
    }

    @Override
    public String toString() {
        return "Lift{" +
                ", move=" + direction +
                ",count passengers=" + passengers.size() +
                ", position=" + position +
                ", points=" + points.size() +
                '}';
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
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
        log.info("двери открываются");
    }

    @SneakyThrows
    private void closeDoors() {
        Thread.sleep(speedOpenDoors * 1000);
        log.info("двери закрываются");
    }
}


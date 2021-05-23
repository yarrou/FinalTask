package building;

import building.floors.EndFloor;
import building.floors.FirstFloor;
import building.floors.Floor;
import building.floors.StandardFloor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import passengers.Passenger;
import service.Dispatcher;
import service.Goal;
import statistic.StatisticsCollector;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class House implements Runnable {
    private final List<Floor> floors;
    private final Set<Elevator> elevators;
    private final StatisticsCollector collector;
    private final Dispatcher dispatcher;


    public House(int countFloors, int countLifts, int maxLoad, int doorsSpeed, int liftSpeed) {
        if (countFloors < 2 || countLifts < 1) {
            throw new IllegalArgumentException("невозможно создать дом с такими параметрами");
        }
        this.collector = new StatisticsCollector();
        this.dispatcher = new Dispatcher();
        this.floors = generateFloors(countFloors);
        this.elevators = generateLifts(countLifts, maxLoad, doorsSpeed, liftSpeed, collector);


    }

    public int countGoals() {
        return dispatcher.getGoals().size();
    }

    public Set<Elevator> getElevators() {
        return elevators;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public int countFloors() {
        return floors.size();
    }


    private List<Floor> generateFloors(int count) {
        List<Floor> genfloors = new ArrayList<>();
        genfloors.add(new FirstFloor());
        Stream.iterate(2, n -> n + 1).limit(count - 2).forEach(x -> genfloors.add(new StandardFloor(x)));
        genfloors.add(new EndFloor(count));
        genfloors.stream().forEach(x -> collector.initFloor(x.getNumber()));
        return genfloors;
    }


    private Set<Elevator> generateLifts(int count, int maxLoad, int doorsSpeed, int liftSpeed, StatisticsCollector collector) {
        return Stream.iterate(0, n -> n + 1).limit(count).map(n -> new Elevator(n + 1, maxLoad, doorsSpeed, liftSpeed, floors, dispatcher, collector)).collect(Collectors.toSet());
    }

    public void addPeople(Passenger passenger) {
        Goal goal = new Goal(passenger);
        Floor floor = floors.get(passenger.getFromFloor() - 1);
        if (floor.getQuery(goal.getDirection()).size() == 0) {
            synchronized (dispatcher) {
                dispatcher.addGoal(goal);
            }
        }
        floor.newPeople(passenger);
    }

    //метод проверяет есть ли задачи и свободные лифты, при наличии "будит" лифт и отправляет его по заявке
    public void scanner() {
        while (elevators.stream().filter(elevator -> !elevator.isOccupied()).count() > 0 && dispatcher.isThereGoal()) {
            for (Elevator elevator : elevators) {
                if (!elevator.isOccupied()) {
                    synchronized (dispatcher) {
                        dispatcher.notify();
                        elevator.callOnElevator(dispatcher.getGoal());
                        break;
                    }
                }
            }
        }

    }

    @SneakyThrows
    @Override
    public void run() {
        elevators.stream().forEach(elevator -> new Thread(elevator).start());
        while (true) {
            Thread.sleep(1000);
            scanner();
        }
    }
}


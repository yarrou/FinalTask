package building;

import building.floors.EndFloor;
import building.floors.FirstFloor;
import building.floors.Floor;
import building.floors.StandartFloor;
import lombok.extern.slf4j.Slf4j;
import passengers.Passenger;
import service.Goal;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class House implements Runnable{
    private final Queue<Goal> goals;
    private final List<Floor> floors;
    private final Set<Elevator> elevators;
    //private final GeneratorPassengers generator;

    public House(int countFloors, int countLifts, int maxLoad, int doorsSpeed, int liftSpeed) {
        this.floors = generateFloors(countFloors);
        this.elevators = generateLifts(countLifts, maxLoad, doorsSpeed, liftSpeed);
        this.goals= new LinkedList<>();
        //this.generator = new GeneratorPassengers(countFloors);
    }
    public List<Floor> getFloors(){
        return floors;
    }


    private List<Floor> generateFloors(int count) {
        List<Floor> genfloors = new ArrayList<>();
        genfloors.add(new FirstFloor());
        Stream.iterate(2, n -> n + 1).limit(count-2).forEach(x -> genfloors.add(new StandartFloor(x)));
        genfloors.add(new EndFloor(count));
        return genfloors;
    }


    private Set<Elevator> generateLifts(int count, int maxLoad, int doorsSpeed, int liftSpeed) {
        return Stream.iterate(0, n -> n + 1).limit(count).map(n->new Elevator(maxLoad, doorsSpeed, liftSpeed,floors,goals)).collect(Collectors.toSet());
    }
    public void addPeople(Passenger passenger){
        Goal goal = new Goal(passenger);
        Floor floor = floors.get(passenger.getFromFloor()-1);
        if (floor.getQuery(goal.getDirection()).size()==0){
            goals.add(goal);
        }
        floor.newPeople(passenger);
    }




    public void dispatcher(){
        while (elevators.stream().filter(elevator -> !elevator.isOccupied() ).count()>0&&goals.size()>0){
            for (Elevator elevator : elevators){
                if(!elevator.isOccupied()){
                    elevator.callOnElevator(goals.poll());
                    break;
                }
            }
        }

    }
    public void start1() throws InterruptedException {
        int i = 0;
        while (true){
            i++;
            Thread.sleep(1000);
            dispatcher();
            //if(i%3==0) addPeople(generator.getRandomPassenger());
            for (Elevator elevator : elevators){
                elevator.onFloor();
                elevator.move();
            }
        }
    }

    @Override
    public void run() {
        elevators.stream().forEach(elevator ->new Thread(elevator).start());
        while (true){
            dispatcher();
        }
    }
}


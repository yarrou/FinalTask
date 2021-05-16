package building;

import building.floors.EndFloor;
import building.floors.FirstFloor;
import building.floors.Floor;
import building.floors.StandartFloor;
import lombok.extern.slf4j.Slf4j;
import passengers.Passenger;
import service.GeneratorPassengers;
import service.Goal;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class House {
    private final Queue<Goal> goals;
    private final List<Floor> floors;
    private final Set<Lift> lifts;
    private final GeneratorPassengers generator;

    public House(int countFloors, int countLifts, int maxLoad, int doorsSpeed, int liftSpeed) {
        this.floors = generateFloors(countFloors);
        this.lifts = generateLifts(countLifts, maxLoad, doorsSpeed, liftSpeed);
        this.goals= new LinkedList<>();
        this.generator = new GeneratorPassengers(countFloors);
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


    private Set<Lift> generateLifts(int count, int maxLoad, int doorsSpeed, int liftSpeed) {
        return Stream.iterate(0, n -> n + 1).limit(count).map(n->new Lift(maxLoad, doorsSpeed, liftSpeed,floors,goals)).collect(Collectors.toSet());
    }
    public void addPeople(Passenger passenger){
        Goal goal = new Goal(passenger);
        Floor floor = floors.get(passenger.getFromFloor()-1);
        if (floor.getQuery(goal.getDirection()).size()==0){
            goals.add(goal);
        }
        floor.newPeople(passenger);
    }

    public void start() throws InterruptedException {
        int i = 0;
        while (true){
            i++;
            Thread.sleep(1000);
            dispatcher();
            if(i%3==0) addPeople(generator.getRandomPassenger());
            for (Lift lift:lifts){
                lift.onFloor();
                   lift.move();
            }
        }
    }


    public void dispatcher(){
        while (lifts.stream().filter(lift -> !lift.isOccupied() ).count()>0&&goals.size()>0){
            for (Lift lift:lifts){
                if(!lift.isOccupied()){
                    lift.summon(goals.poll());
                    break;
                }
            }
        }

    }
}


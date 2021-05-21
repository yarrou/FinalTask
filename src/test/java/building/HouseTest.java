package building;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passengers.Passenger;
import service.Direction;

import static org.junit.jupiter.api.Assertions.*;

class HouseTest {
    private House house;

    @BeforeEach
    private void init(){
        house = new House(10,1,250,1,3);
    }

    @Test
    void addPeople() {
        int countGoals = house.countGoals();
        house.addPeople(new Passenger(80,1,3));
        assertTrue(house.getFloors().get(0).getQuery(Direction.UP).size()>0);
        assertTrue(countGoals+1 == house.countGoals());
    }

    @Test
    void scanner() {
        house.addPeople(new Passenger(80,1,3));
        house.scanner();
        assertTrue(house.countGoals()==0);
    }
}
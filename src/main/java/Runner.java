import building.House;
import building.Lift;
import building.floors.Floor;
import passengers.Passenger;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        House house = new House(7,2 ,250,3,3);
        house.start();
    }

}

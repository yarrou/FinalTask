import building.House;
import service.GeneratorPassengers;

public class Runner {
    public static void main(String[] args) throws InterruptedException {
        House house = new House(7,2 ,250,3,3);
        GeneratorPassengers generator = new GeneratorPassengers(3,house);
        new Thread(house).start();
        new Thread(generator).start();
    }

}

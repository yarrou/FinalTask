package service;

import building.House;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import passengers.Passenger;

/* класс для генеграции пассажиров. Интенсивность генерации задается параметром generationIntensity при создании экземпляра
класса. Чем меньше парамет, чем чаще генерируются пассажиры
 */
@AllArgsConstructor
public class GeneratorPassengers implements Runnable {
    private final int generationIntensity;
    private final House house;

    public Passenger getRandomPassenger() {
        int maxFloor = house.countFloors();
        int currentFloor = getRandomNumberFloor(maxFloor);
        int requiredFloor = getRandomNumberFloor(maxFloor);
        while (currentFloor == requiredFloor) {
            requiredFloor = getRandomNumberFloor(maxFloor);
        }
        return new Passenger(getRandomWeight(), currentFloor, requiredFloor);
    }

    private int getRandomNumberFloor(int max) {
        return (int) ((Math.random() * (max)) + 1);
    }

    private int getRandomWeight() {
        return (int) ((Math.random() * (110 - 40)) + 40);
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            Thread.sleep(generationIntensity * 1000);
            house.addPeople(getRandomPassenger());
        }

    }
}

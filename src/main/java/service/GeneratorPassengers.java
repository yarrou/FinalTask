package service;

import lombok.AllArgsConstructor;
import passengers.Passenger;

@AllArgsConstructor
public class GeneratorPassengers {
    private final int maxFloor;
    public Passenger getRandomPassenger(){
        int currentFloor = getRandomNumberFloor(maxFloor);
        int requedFloor = getRandomNumberFloor(maxFloor);
        while (currentFloor==requedFloor){
            requedFloor=getRandomNumberFloor(maxFloor);
        }
        return new Passenger(getRandomWeight(),currentFloor,requedFloor);
    }
    private int getRandomNumberFloor( int max) {
        return (int) ((Math.random() * (max - 1)) + 1);
    }
    private int getRandomWeight() {
        return (int) ((Math.random() * (120 - 40)) + 40);
    }
}

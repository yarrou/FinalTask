package statistic;

import passengers.Passenger;

import java.util.Objects;

public class ElevatorStatistic {
    private final int number;
    private int countPassengers;
    private int totalWeight;

    public ElevatorStatistic(int number) {
        this.number = number;
    }

    public void addOne(Passenger passenger) {
        countPassengers += 1;
        totalWeight += passenger.getWeight();
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return
                "лифт №" + number +
                        " перевез " + countPassengers +
                        " пассажиров " +
                        "общей массой " + totalWeight
                ;
    }
}

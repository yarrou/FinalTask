package statistic;

import lombok.Getter;
import passengers.Passenger;

import java.util.Objects;

@Getter
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

    @Override
    public String toString() {
        return
                "лифт №" + number +
                        " перевез " + countPassengers +
                        " пассажиров " +
                        "общей массой " + totalWeight + " kg"
                ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElevatorStatistic that = (ElevatorStatistic) o;
        return number == that.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
}

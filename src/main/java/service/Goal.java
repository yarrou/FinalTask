package service;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import passengers.Passenger;


@Getter
@Setter @EqualsAndHashCode @AllArgsConstructor
public class Goal {
    private final Direction direction;
    private final int floorNumber;
    public Goal(Passenger passenger){
        this.floorNumber = passenger.getFromFloor();
        this.direction =passenger.getFromFloor()<passenger.getRequiredFloor()? Direction.UP: Direction.DOWN;
    }
}

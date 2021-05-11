package passengers;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Passenger {
    private final int weight;
    private final int fromFloor;
    private final int requiredFloor;
}

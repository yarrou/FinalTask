package samples;

import building.floors.EndFloor;
import building.floors.FirstFloor;
import building.floors.Floor;
import building.floors.StandardFloor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FloorsSample {
    public static List<Floor> getFloors(int count){
        List<Floor> genfloors = new ArrayList<>();
        genfloors.add(new FirstFloor());
        Stream.iterate(2, n -> n + 1).limit(count - 2).forEach(x -> genfloors.add(new StandardFloor(x)));
        genfloors.add(new EndFloor(count));
        return genfloors;
    }

}

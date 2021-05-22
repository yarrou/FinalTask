package service;

import building.House;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import passengers.Passenger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

class GeneratorPassengersTest {
    @Mock
    private House house;
    private GeneratorPassengers generatorPassengers;
    int maxFloor = 10;

    @BeforeEach
    public void init() {

        MockitoAnnotations.initMocks(this);
        when(house.countFloors()).thenReturn(maxFloor);
        generatorPassengers = new GeneratorPassengers(1, house);
    }

    @Test
    void getRandomPassenger() {
        Passenger passenger = generatorPassengers.getRandomPassenger();
        assertTrue(passenger.getRequiredFloor() <= maxFloor && passenger.getRequiredFloor() > 0);
        assertTrue(passenger.getRequiredFloor() != passenger.getFromFloor());
        assertTrue(0 < passenger.getFromFloor() && passenger.getFromFloor() < maxFloor + 1);
    }
}
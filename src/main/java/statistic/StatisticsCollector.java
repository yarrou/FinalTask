package statistic;

import lombok.SneakyThrows;
import passengers.Passenger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Charsets;


public class StatisticsCollector {
    private int totalPassengersCount;
    private List<ElevatorStatistic> elevatorsStatistic;
    private List<FloorStatistic> floorsStatistics;

    public StatisticsCollector() {
        this.elevatorsStatistic = new ArrayList<>();
        this.floorsStatistics = new ArrayList<>();
    }

    public List<ElevatorStatistic> getElevatorsStatisticListNow() {
        return List.copyOf(elevatorsStatistic);
    }

    public List<FloorStatistic> getFloorsStatisticsNow() {
        return List.copyOf(floorsStatistics);
    }

    @SneakyThrows
    public synchronized void event(Passenger passenger, int numberElevator) {
        totalPassengersCount++;
        elevatorsStatistic.stream().filter(e -> e.getNumber() == numberElevator).peek(e -> e.addOne(passenger)).collect(Collectors.toList());
        floorsStatistics.stream().filter(floorStatistic -> floorStatistic.getNumber() == passenger.getFromFloor()).peek(f -> f.addLeft()).collect(Collectors.toList());
        floorsStatistics.stream().filter(f -> f.getNumber() == passenger.getRequiredFloor()).peek(f -> f.addArrived()).collect(Collectors.toList());
        print();

    }

    private void print() throws IOException {
        List<String> toPrint = new ArrayList<>();
        String totalPassengersStatistic = "общее число перевезенных пассажиров " + totalPassengersCount;
        List<String> elevatorStatisticToString = elevatorsStatistic.stream().map(e -> e.toString()).collect(Collectors.toList());
        List<String> floorStatisticToString = floorsStatistics.stream().map(f -> f.toString()).collect(Collectors.toList());
        toPrint.add(totalPassengersStatistic);
        toPrint.addAll(elevatorStatisticToString);
        toPrint.addAll(floorStatisticToString);
        File file = new File("statistic.txt");
        Files.write(file.toPath(), toPrint, Charsets.UTF_8);
    }

    public void initElevator(int number) {
        ElevatorStatistic elevatorStatistic = new ElevatorStatistic(number);
        elevatorsStatistic.add(elevatorStatistic);
    }

    public void initFloor(int number) {
        FloorStatistic floorStatistic = new FloorStatistic(number);
        floorsStatistics.add(floorStatistic);
    }
}

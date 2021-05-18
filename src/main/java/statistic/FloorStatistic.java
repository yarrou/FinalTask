package statistic;

public class FloorStatistic {
    private final int number;
    private int countArrived;
    private int countLeft;

    public FloorStatistic(int number) {
        this.number = number;
    }

    public void addArrived() {
        countArrived++;
    }

    public void addLeft() {
        countLeft++;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "этаж №" +
                number +
                ": приехали " + countArrived +
                ", уехали " + countLeft;
    }
}

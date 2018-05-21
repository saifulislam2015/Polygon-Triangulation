import java.util.Comparator;

class Comparator1 implements Comparator<Event> {
    @Override
    public int compare(Event o1, Event o2) {
        if (o1.y == o2.y) {
            if (o1.x > o2.x) return 1;
            else if (o1.x < o2.x) return -1;
            else return 0;
        }
        return Double.compare(o2.y, o1.y);
    }
}
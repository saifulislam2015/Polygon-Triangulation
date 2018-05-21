import java.util.Comparator;

class Comparator2 implements Comparator<Edge> {
    @Override
    public int compare(Edge o1, Edge o2) {
        return Double.compare(o2.top.y,o1.top.y);
    }
}
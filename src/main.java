import java.io.IOException;

public class main {
    public static void main(String[] args) {
        Monotone m = null;
        try {
            m = new Monotone();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Triangulation t = new Triangulation(m.monotones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

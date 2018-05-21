
public class Event {
	public int id;
	public double x;
	public double y;
	public boolean leftChain;


	public Event(double x, double y, int id) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.x = x;
		this.y = y;
		this.leftChain = false;
	}
	
	/*@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "v" + id + " " + x + " " + y + " " + leftChain;
		return s;
	}*/

}


public class Edge {
	public int id;
	public Event start;
	public Event end;
	public Event helper;
	public Event top;
	public Event bottom;
	public Event left;
	
	public void setLeft() {
		if (start.x <= end.x) left = start;
		else left = end;
	}
	
	// return top most vertex of edge according to Y coordinate
	void setTop() {
		if (start.y >= end.y) {
			top = start;
			bottom = end;
		}
		else {
			top = end;
			bottom = start;
		}
			
	}
	
	public void setHelper(Event helper) {
		this.helper = helper;
	}
	
	public Edge(Event start, Event end, int id) {
		this.id = id;
		this.start = start;
		this.end = end;
		setTop();
		setLeft();
	}
	
	public Edge(Event start, Event end) {
		this.start = start;
		this.end = end;
		setTop();
		setLeft();
	}
	
	/*@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s = "e" + id + " " + start + " " + end + " h " + helper;
		return s;
	}*/

}

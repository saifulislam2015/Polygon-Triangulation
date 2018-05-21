import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Monotone {
	public int V;
	public ArrayList<Event> events = new ArrayList<>();	// sorted
	public ArrayList<Event> ccwevents = new ArrayList<>();
	public ArrayList<Edge> edges = new ArrayList<>();
	public ArrayList<Edge> T = new ArrayList<>();
	public ArrayList<Edge> D = new ArrayList<>();
	public ArrayList<ArrayList<Event>> monotones = new ArrayList<>();


	public Monotone() throws IOException {
		File file = new File("input1.txt");
		Scanner input = new Scanner(file);
		V = input.nextInt();
		for (int i=0;i<V;i++) {
			events.add(new Event(input.nextDouble(), input.nextDouble(),i+1));
			ccwevents.add(events.get(i));
		}
		for (int i=0;i<V;i++) {
			edges.add(new Edge(events.get(i),events.get((i+1)%V),(i+1)%(V+1)));
		}

		Collections.sort(events, new Comparator1());
		//for(int i=0;i<events.size();i++) System.out.println(events.get(i).getX()+","+events.get(i).getY());
		input.close();

		makeMonotone();
		computeMonotonePieces();
		
	}
	
	boolean eventCompare(Event e1, Event e2) {
		return ((e1.x == e2.x) && (e1.y == e2.y));
	}
	
	double ccw(Event e1, Event e2, Event e3) {
		return ((e2.x - e1.x) * (e3.y - e2.y) - (e2.y - e1.y) * (e3.x - e2.x));
	}
	
	int getVertexType(Event e) {
		Edge first = null;
		Edge second = null;

		for (int i=0;i<V;i++) {
			Edge temp = edges.get(i);
			if (eventCompare(temp.end,e)) first = temp;
			else if (eventCompare(temp.start,e)) second = temp;
		}

		if (e.y > first.start.y && e.y > second.end.y) {
			if (ccw(first.start, first.end, second.end) > 0) return  1;
			else return  3;
		}

		else if (e.y < first.start.y && e.y < second.end.y) {
			if (ccw(first.start, first.end, second.end) > 0) return  2;
			else return  4;
		}
		return 5;

	}

	
	int getLeftEdge(Event e) {

		//System.out.println("Left edge of: "+ e.x+" "+e.y);
		Collections.sort(T, new Comparator2());
		for(int i=0;i<T.size();i++){
			//System.out.println(T.get(i).top.x+" "+T.get(i).top.y+"  "+T.get(i).bottom.x+" "+T.get(i).bottom.y);
		}
		System.out.println();
		int size = T.size();
		Edge left = null;
		for (int i=0;i<size;i++) {
			Edge temp = T.get(i);
			//System.out.println(temp + " " + temp.getLeft());
			if (left == null) left = temp;
			else if ((temp.left.x < e.x) && (temp.left.x > left.left.x)
					&& (temp.start.y >= e.y) && (temp.end.y <= e.y))
				left = temp;
		}
		//System.out.println("LeftEdge Result: ");
		//System.out.println(left.top.x+" "+left.top.y+"  "+left.bottom.x+" "+left.bottom.y);

		return left.id;
	}

	
	public void makeMonotone() {
		for (int i=0;i<V;i++) {
			Event e = events.get(i);
			int s = getVertexType(e);

			if(s==1) {
                System.out.println(e.x+" "+e.y + " -- start" );
                edges.get(e.id-1).setHelper(e);
                T.add(edges.get(e.id-1));

				System.out.println();
				for(int j=0;j<T.size();j++){
					System.out.println(T.get(j).top.x+" "+T.get(j).top.y+"  "+T.get(j).bottom.x+" "+T.get(j).bottom.y);
				}
				System.out.println();
            }
			else if(s==2) {
                System.out.println(e.x+" "+e.y + " -- end" );

                Edge temp;
                if (e.id == 1) temp = edges.get(V-1);
                else temp = edges.get(e.id-2);

                if (temp.helper != null && getVertexType(temp.helper) == 4) {
                    D.add(new Edge(e, temp.helper));
                }

                for (int j=0;j<T.size();j++) {
                    if (T.get(j).id == e.id-1) {
                        T.remove(j);
                        break;
                    }
                }

				System.out.println();
				for(int j=0;j<T.size();j++){
					System.out.println(T.get(j).top.x+" "+T.get(j).top.y+"  "+T.get(j).bottom.x+" "+T.get(j).bottom.y);
				}
				System.out.println();
            }
			else if(s==3) {
                System.out.println(e.x+" "+e.y + " -- split" );
                int leftID = getLeftEdge(e);
                D.add(new Edge(e, edges.get(leftID-1).helper));
                edges.get(leftID-1).setHelper(e);
                edges.get(e.id-1).setHelper(e);
                T.add(edges.get(e.id-1));

				System.out.println();
				for(int j=0;j<T.size();j++){
					System.out.println(T.get(j).top.x+" "+T.get(j).top.y+"  "+T.get(j).bottom.x+" "+T.get(j).bottom.y);
				}
				System.out.println();
            }
			else if(s==4) {
                System.out.println(e.x+" "+e.y + " -- merge" );

                Edge temp;
                if (e.id == 1) temp = edges.get(V-1);
                else temp = edges.get(e.id-2);

                if (temp.helper != null && getVertexType(temp.helper) == 4) {
                    D.add(new Edge(e, temp.helper));
                }

                for (int j=0;j<T.size();j++) {
                    if (T.get(j).id == e.id-1) {
                        T.remove(j);
                        break;
                    }
                }


                int leftID = getLeftEdge(e);
                if (getVertexType(edges.get(leftID-1).helper) == 4)
                    D.add(new Edge(e, edges.get(leftID-1).helper));
                edges.get(leftID-1).setHelper(e);

				System.out.println();
				for(int j=0;j<T.size();j++){
					System.out.println(T.get(j).top.x+" "+T.get(j).top.y+"  "+T.get(j).bottom.x+" "+T.get(j).bottom.y);
				}
				System.out.println();
            }
			else {
                System.out.println(e.x+" "+e.y + " -- regular" );
                int nextID = e.id + 1, prevID = e.id - 1;
                if (prevID == 0) prevID = V;
                else if (nextID == V+1) nextID = 1;
                double prevY=0, nextY=0;
                for (int j=0;j<V;j++) {
                    if (events.get(j).id == prevID) prevY = events.get(j).y;
                    else if (events.get(j).id == nextID) nextY = events.get(j).y;
                }
                if (prevY >= nextY) {
                    Edge temp;
                    if (e.id == 1) temp = edges.get(V-1);
                    else temp = edges.get(e.id-2);

                    if (temp.helper != null && getVertexType(temp.helper) == 4) {
                        D.add(new Edge(e, temp.helper));
                    }

                    for (int j=0;j<T.size();j++) {
                        if (T.get(j).id == e.id-1) {
                            T.remove(j);
                            break;
                        }
                    }
                    //Edge f = edges.get(e.id-1);
                    //if(T.contains(f)) T.remove(f);
                    edges.get(e.id-1).setHelper(e);
                    T.add(edges.get(e.id-1));
                }
                else {
                    int leftID = getLeftEdge(e);
                    if (getVertexType(edges.get(leftID-1).helper) == 4)
                        D.add(new Edge(e, edges.get(leftID-1).helper));
                    edges.get(leftID-1).setHelper(e);

                }
				System.out.println();
				for(int j=0;j<T.size();j++){
					System.out.println(T.get(j).top.x+" "+T.get(j).top.y+"  "+T.get(j).bottom.x+" "+T.get(j).bottom.y);
				}
				System.out.println();
            }
		}

		//output
		PrintWriter out=null;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter("Graphics\\monotones.txt", false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.println(D.size());
		for (int i=0;i<D.size();i++){
			out.println(D.get(i).start.x + " " + D.get(i).start.y + " " + D.get(i).end.x + " " + D.get(i).end.y);
		}
		out.close();
		
	}
	
	private void getPiece(ArrayList<Event> P, ArrayList<Edge> Diag) {

		if (Diag.size() > 0) {
			Edge diag = Diag.get(0);
			ArrayList<Event> P1 = new ArrayList<>();
			ArrayList<Event> P2 = new ArrayList<>();
			int topindex = 0;
			int botindex = 0;
			
			for (int k=0;k<P.size();k++) {
				if (eventCompare(P.get(k), diag.top)) {
					topindex = k;
				}
				if (eventCompare(P.get(k), diag.bottom)) {
					botindex = k;
				}
			}

			System.out.println(P.get(topindex).x+" "+P.get(topindex).y);
			System.out.println(P.get(botindex).x+" "+P.get(botindex).y);

			int tempindex = topindex;
			while (tempindex != botindex) {				
				P1.add(P.get(tempindex));
				tempindex = (tempindex+1)%P.size();
			}
			P1.add(P.get(tempindex));
			tempindex = botindex;
			while (tempindex != topindex) {
				P2.add(P.get(tempindex));
				tempindex = (tempindex+1)%P.size();
			}
			P2.add(P.get(tempindex));

			Diag.remove(0);
			ArrayList<Edge> Diag1 = new ArrayList<>();
			ArrayList<Edge> Diag2 = new ArrayList<>();
			for (int k=0;k<Diag.size();k++) {
				Edge d = Diag.get(k);

				int count = 0;
				for (int j=0;j<P1.size();j++) {
					if (eventCompare(d.top, P1.get(j))) count ++;
					else if (eventCompare(d.bottom, P1.get(j))) count++;
					if (count == 2) {
						Diag1.add(d);
						break;
					}
				}

				count = 0;
				for (int j=0;j<P2.size();j++) {
					if (eventCompare(d.top, P2.get(j))) count ++;
					else if (eventCompare(d.bottom, P2.get(j))) count++;
					if (count == 2) {
						Diag2.add(d);
						break;
					}
				}
			}
			/*System.out.println("Diag: ");
			for(int i=0;i<Diag.size();i++)
				System.out.println(Diag.get(i).top.x+" "+Diag.get(i).top.y+"  "+Diag.get(i).bottom.x+" "+Diag.get(i).bottom.y);

			System.out.println();

			System.out.println("P1:");
			for(int i=0;i<P1.size();i++) System.out.println(P1.get(i).x+" "+P1.get(i).y);
			System.out.println();

			System.out.println("P2:");
			for(int i=0;i<P2.size();i++) System.out.println(P2.get(i).x+" "+P2.get(i).y);
			System.out.println();

			System.out.println("Diag1: ");
			for(int i=0;i<Diag1.size();i++)
				System.out.println(Diag1.get(i).top.x+" "+Diag1.get(i).top.y+"  "+Diag1.get(i).bottom.x+" "+Diag1.get(i).bottom.y);

			System.out.println();
			System.out.println("Diag2: ");
			for(int i=0;i<Diag2.size();i++)
				System.out.println(Diag2.get(i).top.x+" "+Diag2.get(i).top.y+"  "+Diag2.get(i).bottom.x+" "+Diag2.get(i).bottom.y);

			System.out.println();*/
			getPiece(P1,Diag1);
			getPiece(P2,Diag2);
			return;
		}
		if (Diag.size() == 0){
			monotones.add(P);
		}
		
	}
	
	public void computeMonotonePieces() {	
		Collections.sort(D, new Comparator2());
		int size = D.size();
		ArrayList<Edge> diag = new ArrayList<>();
		for (int i=0;i<size;i++) {
			//System.out.println(D.get(i));
			diag.add(D.get(i));
		}
		getPiece(ccwevents, diag);	
		
	}
}

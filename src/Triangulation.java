import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Stack;

public class Triangulation {

    ArrayList<ArrayList<Event>> monotones = new ArrayList<>();
    int d;

    public Triangulation(ArrayList<ArrayList<Event>> monotones) throws IOException {
        d = monotones.size();

        for (int i=0;i<d;i++) {
            ArrayList<Event> events = monotones.get(i);
            ArrayList<Event> newEvents = new ArrayList<>();
            for (int j=0;j<events.size();j++) {
                newEvents.add(new Event(events.get(j).x, events.get(j).y, j+1));
            }
            this.monotones.add(newEvents);
        }

        triangulate();
    }

    double ccw(Event e1, Event e2, Event e3) {
        return ((e2.x - e1.x) * (e3.y - e1.y) - (e2.y - e1.y) * (e3.x - e1.x));
    }

    public void Merge(ArrayList<Event> events){
        Collections.sort(events, new Comparator1());
        //for (int j=0;j<events.size();j++) System.out.println(events.get(j).x+" "+events.get(j).y);
        //System.out.println();
        int size = events.size();
        events.get(0).leftChain =(true);
        Event top = events.get(0);
        //System.out.println(top);
        events.get(size-1).leftChain =(true);
        Event bot = events.get(size-1);
        //System.out.println(bot);
        int nextID = top.id%size + 1;
        //System.out.println("botID: "+bot.id);
        //System.out.println("nextID: "+nextID);
        while(nextID != bot.id) {
            for (int j=0;j<size;j++) {
                //System.out.println("id: "+events.get(j).id);
                if (events.get(j).id == nextID) {
                    events.get(j).leftChain =(true);
                    break;
                }
            }
            nextID = nextID%size + 1;
            //System.out.println("nextID(Loop): "+nextID);
        }
    }

    void triangulate() {
        ArrayList<ArrayList<Edge>> diagonals = new ArrayList<>();
        for (int i=0;i<d;i++) {
            ArrayList<Event> events = monotones.get(i);
            Merge(events);
            int size = events.size();
            /*for(int j=0;j<events.size();j++){
                System.out.print(events.get(j).leftChain+" ");
            }
            System.out.println();*/

            // triangulation
            Stack<Event> stack = new Stack<>();
            ArrayList<Edge> D = new ArrayList<>();
            stack.push(events.get(0));
            stack.push(events.get(1));
            for (int j=2;j<size-1;j++) {
                Event e = events.get(j);
                //System.out.println("this " + e);
                if (e.leftChain != stack.peek().leftChain) {
                    Event s;
                    while(true) {
                        s = stack.pop();
                        if (stack.isEmpty()) break;
                        D.add(new Edge(e, s));
                    }
                    stack.push(events.get(j-1));
                    stack.push(e);
                }
                else {
                    Event last = stack.pop();
                    Event s = null;
                    while(true) {
                        s = stack.peek();
                        //System.out.println("top " + s);
                        if (s.leftChain == e.leftChain) {
                            Event next = null;
                            for (int k=0;k<size;k++) {
                                if (events.get(k).id == s.id%size + 1) {
                                    next = events.get(k);
                                    break;
                                }
                            }
                            //System.out.println("next " + next + ccw(s,next,e));
                            if (ccw(s,next,e)>0) {
                                D.add(new Edge(s, e));
                                last = s;
                                stack.pop();
                            }
                            else break;
                        }
                        else {
                            Event next = null;
                            for (int k=0;k<size;k++) {
                                int id = s.id%size - 1;
                                if (id == 0) id = size;
                                if (events.get(k).id == id) {
                                    next = events.get(k);
                                    break;
                                }
                            }
                            //System.out.println("next " + next + ccw(s,next,e));
                            if (ccw(s,next,e)<0) {
                                D.add(new Edge(s, e));
                                last = s;
                                stack.pop();
                            }
                            else break;
                        }

                        if (stack.isEmpty()) break;
                    }
                    stack.push(last);
                    stack.push(e);
                }

            }
            Event e = events.get(events.size()-1);
            if(!stack.empty()) stack.pop();
            while(true) {
                Event s = stack.pop();
                if (stack.isEmpty()) break;
                D.add(new Edge(e, s));
            }

            // print D
            //for (int k=0;k<D.size();k++) System.out.println(D.get(k));
            diagonals.add(D);
        }

       //output
        PrintWriter output=null;
        try {
            output = new PrintWriter(new BufferedWriter(new FileWriter("Graphics\\triangulation.txt", false)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int count = 0;
        for (int j=0;j<diagonals.size();j++) {
            count += diagonals.get(j).size();
        }

        output.println(count);
        for (int j=0;j<diagonals.size();j++) {
            ArrayList<Edge> D = diagonals.get(j);
            for (int i=0;i<D.size();i++)
                output.println(D.get(i).start.x + " " + D.get(i).start.y + " " + D.get(i).end.x + " " + D.get(i).end.y);
        }
        output.close();
    }

}

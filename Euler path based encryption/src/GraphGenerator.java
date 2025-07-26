// GraphGenerator.java
import java.util.*;

public class GraphGenerator {
    public static Graph randomEulerian(int V) {
        System.out.println("GraphGenerator: creating random Eulerian graph with V=" + V);
        Graph g = new Graph(V);
        List<Integer> verts = new ArrayList<>();
        for (int i = 0; i < V; i++) verts.add(i);
        Collections.shuffle(verts, new Random());
        for (int i = 0; i < V - 1; i++) {
            g.addEdge(verts.get(i), verts.get(i + 1));
        }
        g.addEdge(verts.get(V - 1), verts.get(0));
        System.out.println("GraphGenerator: done, E=" + g.getEdges().size());
        return g;
    }
}

// Graph.java
import java.util.*;

public class Graph {
    public static class Edge {
        public final int id, u, v;
        public boolean used = false;
        public Edge(int id, int u, int v) { this.id = id; this.u = u; this.v = v; }
        public int other(int x) { return x == u ? v : u; }
    }

    private final int V;
    private final List<Edge> edges = new ArrayList<>();
    private final List<List<Edge>> adj;

    public Graph(int V) {
        this.V = V;
        adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v) {
        int id = edges.size() + 1;
        Edge e = new Edge(id, u, v);
        edges.add(e);
        adj.get(u).add(e);
        adj.get(v).add(e);
    }

    public List<Edge> getEdges() { return edges; }
    public List<List<Edge>> getAdj() { return adj; }
    public int vertexCount() { return V; }
}

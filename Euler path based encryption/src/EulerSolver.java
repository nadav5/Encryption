// EulerSolver.java
import java.util.*;

public class EulerSolver {
    public List<Integer> solve(Graph g, int start) {
        System.out.println("EulerSolver: start DFS from vertex " + start);
        List<Integer> circuit = new ArrayList<>();
        dfs(start, g, circuit);
        Collections.reverse(circuit);
        System.out.println("EulerSolver: circuit found = " + circuit);
        return circuit;
    }

    private void dfs(int u, Graph g, List<Integer> out) {
        for (Graph.Edge e : g.getAdj().get(u)) {
            if (!e.used) {
                e.used = true;
                dfs(e.other(u), g, out);
                out.add(e.id);
            }
        }
    }
}

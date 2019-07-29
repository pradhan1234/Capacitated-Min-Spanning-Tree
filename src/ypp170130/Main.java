package ypp170130;

import java.util.*;

/**
 * Driver Code for computing Capacitated Minimum Weight Spanning Tree using
 * 1. Modified Kruskal's Algorithm
 * 2. Esau Williams Heuristic
 */
public class Main {

    static Set<Graph.Edge> mstEdges = new HashSet<>();

    public static int wmstModifiedKruskals(Graph g) {
        int wmst = 0;
        Graph.Edge[] edges = g.getEdgeArray();
        List<Graph.Edge> result = new ArrayList<>();
        Arrays.sort(edges);
        for (Graph.Edge e : edges) {
            if (result.size() == g.V() - 1) break;
            Graph.Vertex u, v;
            u = e.getFrom();
            v = e.getTo();
            if (u.union(v)) {
                wmst += e.weight;
                result.add(e);
            }
        }
        if (result.size() != g.V() - 1) {
            return -1;
        }
        System.out.println("Spanning Tree Edges:");
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i));
        }
        System.out.println("\nWeight: " + wmst);
        System.out.println("______________________________________________");
        return wmst;
    }

    public static int initEsauWilliams(Graph g) {

        // sort edges of each vertex
        for (Graph.AdjacencyList al : g.adjList) {
            List<Graph.Edge> edgeList = al.edges;
            Collections.sort(edgeList);
            al.edges = edgeList;
        }

        int wmst = 0;
        // connect each node to root, we get upper bound
        Graph.Vertex root = g.root;
        for (Graph.Edge e : g.adjList[root.getIndex()].edges) {
            // from is root, to is u
            Graph.Vertex u = e.getTo();
            u.elements.add(u);
            u.connectingLink = e;

            wmst += e.weight;
            e.s = Graph.Status.USED;
            mstEdges.add(e);
        }
        return wmst;
    }

    public static int tradeoff(Graph g, Graph.Vertex u, Graph.Edge[] selectedEdge) {
        // set selected edge e in param
        // min j cost(Nu, Nj) <-- c1
        int c1 = 0, c2;
        for (Graph.Edge e : g.adjList[u.getIndex()].edges) {
            if (e.s == Graph.Status.UNPROCESSED) {
                selectedEdge[0] = e;
                c1 = selectedEdge[0].weight;
                break;
            }
        }
        c2 = u.find().connectingLink.weight;
        return c1 - c2;
    }

    public static int esauWilliams(Graph g) {
        int wmst = initEsauWilliams(g);

        for (Graph.AdjacencyList al : g.adjList) {
            Graph.Vertex u = al.getVertex();
            if (u == g.root) continue;
            Graph.Edge[] e = new Graph.Edge[1];
            int t = tradeoff(g, u, e);
            System.out.println("tradeoff " + u + ":" + t + " edge:" + e[0]);
        }
        System.out.println("\nWeight: " + wmst);
        return wmst;
    }

    public static void main(String[] args) {
        // write your code here
        String s = "6 15    1 3 3   1 2 4   0 1 5   2 4 5   0 2 6   3 4 6   3 5 6   4 5 7   2 3 8    1 4 8   0 3 9  1 5 10  0 4 12  2 5 12  0 5 15";
//        Graph g1 = Graph.construct(new Scanner(s));
//        g1.printGraph();
//        System.out.println("\nModified Kruskals Algorithm\n");
//        wmstModifiedKruskals(g1);

        System.out.println("\nEsau Williams Algorithm");
        Graph g = Graph.construct(new Scanner(s));
        g.printGraph();
        esauWilliams(g);
    }
}

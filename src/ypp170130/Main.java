package ypp170130;

import org.omg.PortableServer.AdapterActivator;

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
            u.defaultLink = e;

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

    public static boolean allPositive(Graph g) {
        for(Graph.AdjacencyList al : g.adjList) {
            if(al.u == g.root) continue;
            if(al.u.tradeoff > 0) {
                continue;
            }
            return false;
        }
        return true;
    }

    public static int esauWilliams(Graph g) {
        int wmst = initEsauWilliams(g);
        System.out.println("\nWeight: " + wmst);
        // use a queue/linkedlist start by puttinng 1.. n inside it
        LinkedList<Graph.Vertex> q = new LinkedList<>();
        for (Graph.AdjacencyList al : g.adjList) {
            Graph.Vertex u = al.getVertex();
            if (u == g.root) continue;
            q.add(u);
        }

        int i = 1;
        while(true) {
            if(allPositive(g)) {
                break;
            }
            System.out.println("iteration:" + i++);
            int n = q.size();
            Graph.Vertex minU = null;
            Graph.Edge selectedE = null;
            int minTradeoff = Integer.MAX_VALUE;
            while(n>0) {
                Graph.Vertex u = q.remove();
                Graph.Edge[] e = new Graph.Edge[1];
                u.tradeoff = tradeoff(g, u, e);
                u.tradeoffEdge = e[0];
                System.out.println("tradeoff " + u + ": " + u.tradeoff + " edge:" + e[0]);
                n--;
            }

            for (Graph.AdjacencyList al : g.adjList) {
                Graph.Vertex u = al.getVertex();
                if (u == g.root) continue;
                if(u.tradeoff < minTradeoff) {
                    minTradeoff = u.tradeoff;
                    minU = u;
                    selectedE = u.tradeoffEdge;
                }
            }

            System.out.println("\nMin Tradeoff "+ minU.tradeoff + " edge " + selectedE);
            Graph.Vertex u, v;
            u = selectedE.getFrom();
            v = selectedE.getTo();
            Graph.Vertex[] updated = new Graph.Vertex[1];
            if(u.unionEW(v, updated)) {
                selectedE.s = Graph.Status.USED;
                q.addAll(u.find().elements);
                System.out.println("with 0: " + updated[0].defaultLink +" w " + updated[0].defaultLink.weight);
                wmst = wmst - updated[0].defaultLink.weight + selectedE.weight;
            } else {
                selectedE.s = Graph.Status.DISCARD;
                q.addAll(u.find().elements);
                q.addAll(v.find().elements);
            }


            System.out.println("\nWeight: " + wmst);
//            break;
        }
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

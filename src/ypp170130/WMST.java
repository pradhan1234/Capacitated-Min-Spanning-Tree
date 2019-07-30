package ypp170130;

import java.util.*;

/**
 * Driver Code for computing Capacitated Minimum Weight Spanning Tree using
 * 1. Modified Kruskal's Algorithm
 * 2. Esau Williams Heuristic
 */
public class WMST {

    // to keep track for edges in mst
    static Set<Graph.Edge> mstEdges = new HashSet<>();

    /**
     * Modified Kruskals Algorithm
     * Computes weight of the capacitated minimum spanning tree using modified kruskal's algorithm.
     *
     * @param g graph
     * @return weight of the minimum spanning tree
     */
    public static int wmstModifiedKruskals(Graph g) {
        int wmst = 0;
        Graph.Edge[] edges = g.getEdgeArray();
        List<Graph.Edge> result = new ArrayList<>();

        // sort the edges in ascending order
        Arrays.sort(edges);

        for (Graph.Edge e : edges) {
            // check if we have a tree
            if (result.size() == g.V() - 1) break;

            Graph.Vertex u, v;
            u = e.getFrom();
            v = e.getTo();
            // union function takes care of handling the constraints
            if (u.union(v)) {
                wmst += e.weight;
                result.add(e);
            }
        }
        // if constructing such a tree is infeasible given the constraints
        if (result.size() != g.V() - 1) {
            return -1;
        }
        // print the edges in mst
        System.out.println("Spanning Tree Edges:");
        for (int i = 0; i < result.size(); i++) {
            System.out.println(result.get(i));
        }
        System.out.println("\nWeight: " + wmst);
        System.out.println("______________________________________________");
        return wmst;
    }

    /**
     * initializes some fields of vertices for using Esau Williams heuristics
     *
     * @param g graph
     * @return weight of the minimum spanning tree
     */
    public static int initEsauWilliams(Graph g) {
        // sort edges of each vertex in graph
        for (Graph.AdjacencyList al : g.adjList) {
            List<Graph.Edge> edgeList = al.edges;
            Collections.sort(edgeList);
            al.edges = edgeList;
        }

        int wmst = 0;
        // connect each node to root, we get upper bound on weight of cmst
        Graph.Vertex root = g.root;
        for (Graph.Edge e : g.adjList[root.getIndex()].edges) {
            // neighbours of root
            Graph.Vertex u = e.getTo();
            // initially each vertex is a cluster
            u.elements.add(u);
            // cluster representative should know, which node in its cluster
            // is nearest to root, initially just use this edge
            u.connectingLink = e;
            // book keeping, for efficient computation of tradeoffs
            u.defaultLink = e;

            wmst += e.weight;
            e.s = Graph.Status.USED; // mark edge
            mstEdges.add(e);
        }
        return wmst;
    }

    /**
     * Computes the trade off value
     *
     * @param g            graph
     * @param u            vertex whose trade off we want to compute
     * @param selectedEdge wrapper for edge that have minimum cost and yet unprocessed
     * @return trade off value
     */
    public static int tradeoff(Graph g, Graph.Vertex u, Graph.Edge[] selectedEdge) {
        // heuristic
        // trade off = min_j cost(Nu, Nj) - cost(comp(Nu), root)
        int c1 = 0, c2;
        // select the next smallest unprocessed edge for computing tradeoff
        for (Graph.Edge e : g.adjList[u.getIndex()].edges) {
            if (e.s == Graph.Status.UNPROCESSED) {
                selectedEdge[0] = e;
                c1 = selectedEdge[0].weight;
                break;
            }
        }
        // find cost(comp(Nu), root)
        // book keeping of these information helps to compute this value easily
        // u.find() gives representative of cluster
        // representative knows edge that connects cluster to root
        c2 = u.find().connectingLink.weight;
        return c1 - c2;
    }

    /**
     * to determine if we can terminate the algortihm
     * false indicates, we still need to recompute tradeoff
     *
     * @param g graph
     * @return true if all tradeoffs are positive, false otherwise
     */
    public static boolean allPositive(Graph g) {
        for (Graph.AdjacencyList al : g.adjList) {
            if (al.u == g.root) continue;
            if (al.u.tradeoff > 0) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Esau Williams Heuristic to find cmst
     *
     * @param g graph
     * @return value of cmst
     */
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
        while (true) {
            if (allPositive(g)) {
                break;
            }
            System.out.println("iteration:" + i++);
            int n = q.size();
            Graph.Vertex minU = null;
            Graph.Edge selectedE = null;
            int minTradeoff = Integer.MAX_VALUE;
            while (n > 0) {
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
                if (u.tradeoff < minTradeoff) {
                    minTradeoff = u.tradeoff;
                    minU = u;
                    selectedE = u.tradeoffEdge;
                }
            }
            System.out.println("\nMin Tradeoff " + minU.tradeoff + " edge " + selectedE);
            Graph.Vertex u, v;
            u = selectedE.getFrom();
            v = selectedE.getTo();
            Graph.Vertex[] updated = new Graph.Vertex[1];
            if (u.unionEW(v, updated)) {
                selectedE.s = Graph.Status.USED;
                q.addAll(u.find().elements);
                System.out.println("with 0: " + updated[0].defaultLink + " w " + updated[0].defaultLink.weight);
                wmst = wmst - updated[0].defaultLink.weight + selectedE.weight;
                mstEdges.remove(updated[0].defaultLink);
                mstEdges.add(selectedE);
            } else {
                selectedE.s = Graph.Status.DISCARD;
                q.addAll(u.find().elements);
                q.addAll(v.find().elements);
            }
        }

        System.out.println("Edges");
        for (Graph.Edge e : mstEdges) {
            System.out.println(e);
        }
        System.out.println("\nWeight: " + wmst);
        return wmst;
    }

    public static void main(String[] args) {
        // write your code here
        String s = "6 15    1 3 3   1 2 4   0 1 5   2 4 5   0 2 6   3 4 6   3 5 6   4 5 7   2 3 8    1 4 8   0 3 9  1 5 10  0 4 12  2 5 12  0 5 15";
        String s2 = "7 21    0 1 5   0 2 6   0 3 9   0 4 10  0 5 11  0 6 15  1 2 9   1 3 6   1 4 6   1 5 8   1 6 17  2 3 7   2 4 9   2 5 8   2 6 12      3 4 10  3 5 5   3 6 11  4 5 14  4 6 9   5 6 8";
        Graph g1 = Graph.construct(new Scanner(s2));
        g1.getVertex(3).size = 2;
        g1.printGraph();
        System.out.println("\nModified Kruskals Algorithm\n");
        wmstModifiedKruskals(g1);

        System.out.println("\nEsau Williams Algorithm");
        Graph g = Graph.construct(new Scanner(s2));
        g.getVertex(3).size = 2;
        g.printGraph();
        esauWilliams(g);
    }
}

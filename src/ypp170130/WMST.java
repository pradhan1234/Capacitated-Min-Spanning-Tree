package ypp170130;

import java.util.*;

/**
 * author: yash pradhan (ypp170130)
 * <p>
 * WMST class for computing Capacitated Minimum Weight Spanning Tree using
 * 1. Modified Kruskal's Algorithm
 * 2. Esau Williams Heuristics
 */
public class WMST {

    private static boolean printSteps;
    // to keep track for edges in mst
    private static Set<Graph.Edge> mstEdges = new HashSet<>();

    /**
     * routine to print steps of algorithm
     *
     * @param s message
     */
    static void print(String s) {
        if (printSteps) System.out.print(s);
    }

    /**
     * Modified Kruskals Algorithm
     * Computes weight of the capacitated minimum spanning tree using modified kruskal's algorithm.
     *
     * @param g graph
     * @return weight of the minimum spanning tree
     */
    private static int wmstModifiedKruskals(Graph g) {
        int wmst = 0;
        Graph.Edge[] edges = g.getEdgeArray();
        List<Graph.Edge> result = new ArrayList<>();

        // sort the edges in ascending order
        Arrays.sort(edges);
        print("Sorted Edges\n");
        for (Graph.Edge e : edges) {
            print(e.toString() + "\n");
        }
        for (Graph.Edge e : edges) {
            // check if we have a tree
            if (result.size() == g.V() - 1) {
                print("\nspanning tree generated");
                break;
            }
            print("\n" + e.toString());

            Graph.Vertex u, v;
            u = e.getFrom();
            v = e.getTo();
            // union function takes care of handling the constraints
            if (u.union(v)) {
                print(": accept");
                wmst += e.getWeight();
                result.add(e);
            }
        }
        // if constructing such a tree is infeasible given the constraints
        if (result.size() != g.V() - 1) {
            print("\nspanning tree is infeasible for given graph and constraints");
            return -1;
        }
        // print the edges in mst
        System.out.println("\n\nResults:\nSpanning Tree Edges:");
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
    private static int initEsauWilliams(Graph g) {
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

            wmst += e.getWeight();
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
    private static int tradeoff(Graph g, Graph.Vertex u, Graph.Edge[] selectedEdge) {
        // heuristic
        // trade off = min_j cost(Nu, Nj) - cost(comp(Nu), root)
        int c1 = 0, c2;
        // select the next smallest unprocessed edge for computing tradeoff
        for (Graph.Edge e : g.adjList[u.getIndex()].edges) {
            if (e.s == Graph.Status.UNPROCESSED) {
                selectedEdge[0] = e;
                c1 = selectedEdge[0].getWeight();
                break;
            }
        }
        // find cost(comp(Nu), root)
        // book keeping of these information helps to compute this value easily
        // u.find() gives representative of cluster
        // representative knows edge that connects cluster to root
        c2 = u.find().connectingLink.getWeight();
        return c1 - c2;
    }

    /**
     * to determine if we can terminate the algortihm
     * false indicates, we still need to recompute tradeoff
     *
     * @param g graph
     * @return true if all tradeoffs are positive, false otherwise
     */
    private static boolean allPositive(Graph g) {
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
    private static int esauWilliams(Graph g) {
        int wmst = initEsauWilliams(g);
        // used to accumulate vertices that require re-computation
        // start by putting nodes 1 .. n
        LinkedList<Graph.Vertex> q = new LinkedList<>();
        for (Graph.AdjacencyList al : g.adjList) {
            Graph.Vertex u = al.getVertex();
            if (u == g.root) continue;
            q.add(u);
        }
        int i = 0;
        while (true) {
            // terminate when all trade-offs are >= 0
            if (allPositive(g)) {
                print("\n\nterminate algorithm\n\n");
                break;
            }
            //System.out.println("iteration:" + i++);
            int n = q.size();
            Graph.Edge selectedE = null;
            int minTradeoff = Integer.MAX_VALUE;
            // compute trade-off for n vertices that require (re)computation
            while (n > 0) {
                Graph.Vertex u = q.remove();
                Graph.Edge[] e = new Graph.Edge[1];
                u.tradeoff = tradeoff(g, u, e);
                u.tradeoffEdge = e[0];
                n--;
            }
            print("\n\n\niteration: " + ++i);
            // find minimum trade off
            for (Graph.AdjacencyList al : g.adjList) {
                Graph.Vertex u = al.getVertex();
                if (u == g.root) continue;
                if (u.tradeoff < minTradeoff) {
                    minTradeoff = u.tradeoff;
                    selectedE = u.tradeoffEdge;
                }
                print("\ntrade-off(" + u.getName() + "):  " + u.tradeoff);
            }
            print("\n\nminimum trade-off: " + minTradeoff);
            // determine whether to select this edge or not
            Graph.Vertex u, v;
            u = selectedE.getFrom();
            v = selectedE.getTo();
            Graph.Vertex[] updated = new Graph.Vertex[1];
            if (u.unionEW(v, updated)) {
                print("\nselect " + selectedE.toString());
                selectedE.s = Graph.Status.USED;
                q.addAll(u.find().elements); // add nodes for re-computation
                // update minimum spanning tree
                wmst = wmst - updated[0].defaultLink.getWeight() + selectedE.getWeight();
                mstEdges.remove(updated[0].defaultLink);
                mstEdges.add(selectedE);
            } else {
                print("\nreject " + selectedE.toString());
                selectedE.s = Graph.Status.DISCARD;
                // add nodes for re-computation
                q.addAll(u.find().elements);
                q.addAll(v.find().elements);
            }
        }

        System.out.println("Results:\nSpanning Tree Edges:");
        for (Graph.Edge e : mstEdges) {
            System.out.println(e);
        }
        System.out.println("\nWeight: " + wmst);
        return wmst;
    }

    /**
     * Driver Code
     * <p>
     * printSteps: set true if you want detailed execution
     * in following code it true for sample graph and then set it false.
     */
    public static void main(String[] args) {
        String s1 = "6 15 3   1 3 3   1 2 4   0 1 5   2 4 5   0 2 6   3 4 6   3 5 6   4 5 7   2 3 8    1 4 8   0 3 9  1 5 10  0 4 12  2 5 12  0 5 15";
        String s2 = "7 21 3   0 1 5   0 2 6   0 3 9   0 4 10  0 5 11  0 6 15  1 2 9   1 3 6   1 4 6   1 5 8   1 6 17  2 3 7   2 4 9   2 5 8   2 6 12      3 4 10  3 5 5   3 6 11  4 5 14  4 6 9   5 6 8";
        String s3 = "7 21 3   0 1 2   0 2 10   0 3 10   0 4 2  0 5 10  0 6 10  1 2 1   1 3 10   1 4 10   1 5 10   1 6 10   2 3 1   2 4 10   2 5 10   2 6 10      3 4 10  3 5 10   3 6 10  4 5 1  4 6 10   5 6 1";
        String s4 = "6 15 3   1 3 35   1 2 42   0 1 55   2 4 55   0 2 62   3 4 63   3 5 65   4 5 70   2 3 85    1 4 88   0 3 95  1 5 100  0 4 125  2 5 130  0 5 150";
        String[] strs = new String[]{s1, s2, s3, s4};

        printSteps = false;
        for (String s : strs) {
            Graph g1 = Graph.construct(new Scanner(s));
            g1.printGraph();
            System.out.println("\nModified Kruskals Algorithm\n");
            wmstModifiedKruskals(g1);

            System.out.println("\nEsau Williams Heuristic\n");
            Graph g2 = Graph.construct(new Scanner(s));
            esauWilliams(g2);

            printSteps = false;
        }
    }
}

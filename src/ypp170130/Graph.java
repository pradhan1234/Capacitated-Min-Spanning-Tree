package ypp170130;

import java.util.*;

/**
 * Graph contains Vertex, Edges and Adjacency List as inner classes
 * Uses Adjacency List representation for Graph
 */
public class Graph {

    static int W; // constraint
    Vertex root; // cenntral hub
    AdjacencyList[] adjList;
    int V;  // number of vertices
    int E;  // number of edges

    /**
     * Constructor: creates empty graph with n vertices, sets Vertex 0 as root
     *
     * @param n number of vertices
     */
    public Graph(int n) {
        init(n);
        root = this.getVertex(0);
        root.size = 0;
    }

    /**
     * To create a graph instance as provided in parameter
     *
     * @param in represents input graph
     * @return instance of Graph, created from `in`
     */
    public static Graph construct(Scanner in) {
        int V = in.nextInt();
        int E = in.nextInt();
        // call default constructor
        Graph g = new Graph(V);
        // set constraint
        W = 3;
        // add E edges
        for (int i = 0; i < E; i++) {
            int u, v, w;
            u = in.nextInt();
            v = in.nextInt();
            w = in.nextInt();
            g.addEdge(g.getVertex(u), g.getVertex(v), w, i);
        }
        return g;
    }

    /**
     * @param u Vertex whose edges would be returned
     * @return adjacency list of u
     */
    public AdjacencyList getAdjacencyList(Vertex u) {
        return adjList[u.getIndex()];
    }

    /**
     * V() provides count of vertices in graph
     *
     * @return number of vertices
     */
    public int V() {
        return V;
    }

    /**
     * E() provides count of edges in graph
     *
     * @return number of edges
     */
    public int E() {
        return E;
    }

    /**
     * addEdge adds an edge to graph
     *
     * @param u from vertex
     * @param v to vertex
     * @param w weight/cost
     * @param i index/label for edge (unique)
     */
    public void addEdge(Vertex u, Vertex v, int w, int i) {
        Edge e = new Edge(u, v, w, i);
        getAdjacencyList(u).edges.add(e);
        getAdjacencyList(v).edges.add(e);
        E++;
    }

    /**
     * getEdgeArray() return array of edges in graph
     * Used by modified kruskal's algorithm
     *
     * @return edge array containing all edges in graph
     */
    public Edge[] getEdgeArray() {
        Edge[] edges = new Edge[E()];
        int i = 0;
        for (AdjacencyList al : adjList) {
            Vertex u = al.getVertex();
            for (Edge e : al.edges) {
                Vertex v = e.getOther(u);
                // we dont want edges to appear twice
                if (u.getName() < v.getName()) { // trick
                    edges[i++] = e;
                }
            }
        }
        return edges;
    }

    /**
     * initializes graph with V vertices and 0 edges
     *
     * @param V number of vertices
     */
    void init(int V) {
        this.V = V;
        this.E = 0;
        adjList = new AdjacencyList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new AdjacencyList(i);
        }
    }

    /**
     * @param u index at which, vertex is returned
     * @return vertex at index u
     */
    public Vertex getVertex(int u) {
        return adjList[u].u;
    }

    /**
     * prints the graph
     */
    public void printGraph() {
        System.out.println("______________________________________________");
        System.out.println("Graph: n: " + V() + ", m: " + E());
        for (AdjacencyList al : adjList) {
            Vertex u = al.getVertex();
            System.out.print(u + "(" + u.size + ") : ");
            for (Edge e : al.edges) {
                System.out.print(" " + e);
            }
            System.out.println();
        }
        System.out.println("______________________________________________");
    }

    /**
     * Status markers for edge
     */
    public enum Status {
        USED, // included in tree
        DISCARD, // rejected
        UNPROCESSED // unvisited
    }

    /**
     * Represents Vertex in graph
     * Contains fields used by kruskals and esau-williams
     */
    public class Vertex {
        int label; // identifier for vertex
        int weight;
        // used in union find algorithm
        Vertex representative; // cluster leader
        boolean isAdjRoot; // set to true if it has connecting link
        int size; // size of cluster, cluster representative maintains this
        // used in esau williams
        Set<Vertex> elements = new HashSet<>(); // elements in cluster
        Edge connectingLink; // connecting link to root of this cluster
        int tradeoff; // tradeoff value
        Edge tradeoffEdge; // edge corresponding to tradeoff value
        Edge defaultLink; // initially this connected node to root

        /**
         * Constructor
         *
         * @param u identifier for vertex
         */
        public Vertex(int u) {
            label = u;
            // initially each node is independent
            representative = this;
            size = 1;
        }

        /**
         * Create vertex with custom weight
         *
         * @param u vertex label
         * @param w weight
         */
        public Vertex(int u, int w) {
            this(u);
            weight = w;
        }

        @Override
        public boolean equals(Object other) {
            Vertex otherVertex = (Vertex) other;
            if (otherVertex == null) {
                return false;
            }
            // label are unique identifier
            return this.label == otherVertex.label;
        }

        /**
         * recursively finds representative
         * incorporates path compression
         *
         * @return representative of this vertex
         */
        public Vertex find() {
            if (this != this.representative) {
                this.representative = this.representative.find();
            }
            return representative;
        }

        /**
         * does union of vertex 'this' and v if possible, return true if success
         * returns false otherwise
         *
         * @param v vertex with which union is to be performed
         * @return true if success, false otherwise
         */
        public boolean union(Vertex v) {
            Vertex u = this;
            Vertex repU = u.find();
            Vertex repV = v.find();

            // already in same cluster
            if (repU == repV) {
                return false;
            }
            // constraint violation
            if (repU.size + repV.size > W) {
                return false;
            }
            // handle connections with root
            if (u == root && !repV.isAdjRoot) {
                repV.isAdjRoot = true;
                return true;
            }
            if (v == root && !repU.isAdjRoot) {
                repU.isAdjRoot = true;
                return true;
            }
            if (u == root || v == root) {
                return false;
            }
            // smaller cluster unions under larger one
            // to maintain shorter trees for efficiency
            if (repU.size >= repV.size) {
                repU.size += repV.size;
                repV.representative = repU;
            } else {
                repV.size += repU.size;
                repU.representative = repV;
            }
            return true;
        }

        /**
         * Similar to union() but does additional work
         * Used byb esau williams
         *
         * @param v       vertex with which union is performed
         * @param updated wrapper to vertex whose tradeoff needs to be recomputed
         * @return true on success, false otherwise
         */
        public boolean unionEW(Vertex v, Vertex[] updated) {
            Vertex u = this;
            Vertex repU, repV;
            repU = u.find();
            repV = v.find();
            // already in same cluster
            if (repU == repV) {
                return false;
            }
            // constraint violation
            if (repU.size + repV.size > W) {
                return false;
            }
            // handle connection with root
            if (u == root && !repV.isAdjRoot) {
                repV.isAdjRoot = true;
                return true;
            }
            if (v == root && !repU.isAdjRoot) {
                repU.isAdjRoot = true;
                return true;
            }
            if (u == root || v == root) {
                return false;
            }
            // smaller cluster unions under larger one
            // to maintain shorter trees for efficiency
            if (repU.size >= repV.size) {
                repU.size += repV.size;
                repU.elements.addAll(repV.elements);
                // update connecting link if comp(v) has better edge
                if (repU.connectingLink.weight > repV.connectingLink.weight) {
                    repU.connectingLink = repV.connectingLink;
                }
                repV.representative = repU;
                updated[0] = v;
            } else {
                repV.size += repU.size;
                repV.elements.addAll(repU.elements);
                // update connecting link if comp(u) has better edge
                if (repV.connectingLink.weight > repU.connectingLink.weight) {
                    repV.connectingLink = repU.connectingLink;
                }
                repU.representative = repV;
                updated[0] = v; // is this correct?
            }
            return true;
        }

        /**
         * @return string representation of vertex
         */
        public String toString() {
            return Integer.toString(label);
        }

        /**
         * @return label is unique identifier
         */
        @Override
        public int hashCode() {
            return label;
        }

        /**
         * @return name/label of vertex
         */
        public int getName() {
            return label;
        }

        /**
         * @return index of vertex
         */
        public int getIndex() {
            return getName();
        }
    }

    /**
     * Edge Class: represents edge in graph
     */
    public class Edge implements Comparable<Edge> {
        Vertex from; // tail end of edge
        Vertex to; // head end of edge
        int weight; // cost
        int label; // identifier
        Status s; // used? discarded? unvisited?

        /**
         * @param u from vertex
         * @param v to vertex
         * @param w weight
         * @param e label
         */
        public Edge(Vertex u, Vertex v, int w, int e) {
            from = u;
            to = v;
            weight = w;
            label = e;
            s = Status.UNPROCESSED;
        }

        /**
         * @return from vertex of edge
         */
        public Vertex getFrom() {
            return from;
        }

        /**
         * @return to vertex of edge
         */
        public Vertex getTo() {
            return to;
        }

        /**
         * @return weight of edge
         */
        public int getWeight() {
            return weight;
        }

        /**
         * @return label of edge
         */
        public int getLabel() {
            return label;
        }

        /**
         * @param u vertex on one end of edge
         * @return vertex on other end of edge
         */
        public Vertex getOther(Vertex u) {
            if (from.equals(u)) {
                return to;
            } else {
                return from;
            }
        }

        /**
         * Edge is same if has same labels and connects same pair of vertices
         *
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            Edge edge = (Edge) o;
            return getLabel() == edge.getLabel() &&
                    Objects.equals(getFrom(), edge.getFrom()) &&
                    Objects.equals(getTo(), edge.getTo());
        }

        /**
         * @return label (unique)
         */
        @Override
        public int hashCode() {
            return label;
        }

        /**
         * For ordering of edges, follows natural ordering
         *
         * @param o other edge
         * @return 0 if equal, 1 is this is greater, -1 otherwise
         */
        @Override
        public int compareTo(Edge o) {
            if (weight > o.weight) {
                return 1;
            } else if (weight < o.weight) {
                return -1;
            } else {
                return 0;
            }
        }

        /**
         * @return string representation of edge
         */
        public String toString() {
            return "(" + from + "," + to + ") [" + weight + "]";
        }
    }

    /**
     * Used for representation of graph
     * Each instance has a Vertex and list of Edges
     */
    public class AdjacencyList {
        Vertex u;
        List<Edge> edges;

        /**
         * Constructor
         *
         * @param u vertex
         */
        public AdjacencyList(int u) {
            this.u = new Vertex(u);
            edges = new LinkedList<>();
        }

        /**
         * @return vertex corresponding to adjacency list
         */
        public Vertex getVertex() {
            return u;
        }


    }
}

package ypp170130;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Graph {

    static Vertex root;
    static int W;
    AdjacencyList[] adjList;
    int V;
    int E;

    public Graph(int n) {
        init(n);
    }

    public static Graph construct(Scanner in) {
        int V = in.nextInt();
        int E = in.nextInt();
        Graph g = new Graph(V);
        // this can be changed
        root = g.getVertex(0);
        root.size = 0;
        W = 3;
        for (int i = 0; i < E; i++) {
            int u, v, w;
            u = in.nextInt();
            v = in.nextInt();
            w = in.nextInt();
            g.addEdge(g.getVertex(u), g.getVertex(v), w, i);
        }
        return g;
    }

    public AdjacencyList getAdjacencyList(Vertex u) {
        return adjList[u.getIndex()];
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(Vertex u, Vertex v, int w, int i) {
        Edge e = new Edge(u, v, w, i);
        getAdjacencyList(u).edges.add(e);
        getAdjacencyList(v).edges.add(e);
        E++;
    }

    public Edge[] getEdgeArray() {
        Edge[] edges = new Edge[E()];
        int i = 0;
        for (AdjacencyList al : adjList) {
            Vertex u = al.getVertex();
            for (Edge e : al.edges) {
                Vertex v = e.getOther(u);
                if (u.getName() < v.getName()) {
                    edges[i++] = e;
                }
            }
        }
        return edges;
    }

    void init(int V) {
        this.V = V;
        this.E = 0;
        adjList = new AdjacencyList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new AdjacencyList(i);
        }
    }

    public Vertex getVertex(int u) {
        return adjList[u].u;
    }


    public void printGraph() {
        System.out.println("______________________________________________");
        System.out.println("Graph: n: " + V() + ", m: " + E());
        for (AdjacencyList al : adjList) {
            Vertex u = al.getVertex();
            System.out.print(u + " : ");
            for (Edge e : al.edges) {
                System.out.print(" " + e);
            }
            System.out.println();
        }
        System.out.println("______________________________________________");
    }

    // vertex
    // also add properties for MST
    public class Vertex {
        int label;
        int parent;
        int weight;
        // use this for kruskals, insert more fields as needed
        Vertex representative;
        boolean isAdjRoot;
        int size;

        public Vertex(int u) {
            label = u;
            representative = this;
            size = 1;
        }

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
            return this.label == otherVertex.label;
        }

        public Vertex find() {
            if (this != this.representative) {
                this.representative = this.representative.find();
            }
            return representative;
        }

        public boolean union(Vertex v) {
            Vertex u = this;
            Vertex repU = u.find();
            Vertex repV = v.find();

            if (repU == repV) {
                return false;
            }
            if (repU.size + repV.size > W) {
                return false;
            }
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
            if (repU.size >= repV.size) {
                repU.size += repV.size;
                repV.representative = repU;
            } else {
                repV.size += repU.size;
                repU.representative = repV;
            }
            return true;
        }

        public String toString() {
            return Integer.toString(label);
        }

        @Override
        public int hashCode() {
            return label;
        }

        public int getName() {
            return label;
        }

        public int getIndex() {
            return getName();
        }
    }

    // edge comparable
    // also add properties for MST
    public class Edge implements Comparable<Edge> {
        Vertex from;
        Vertex to;
        int weight;
        int label;

        public Edge(Vertex u, Vertex v, int w, int e) {
            from = u;
            to = v;
            weight = w;
            label = e;
        }

        public Vertex getFrom() {
            return from;
        }

        public Vertex getTo() {
            return to;
        }

        public int getWeight() {
            return weight;
        }

        public int getLabel() {
            return label;
        }

        public Vertex getOther(Vertex u) {
            if (from.equals(u)) {
                return to;
            } else {
                return from;
            }
        }

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

        @Override
        public int hashCode() {
            return label;
        }

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

        public String toString() {
            return "(" + from + "," + to + ") [" + weight + "]";
        }
    }

    public class AdjacencyList {
        Vertex u;
        List<Edge> edges;

        public AdjacencyList(int u) {
            this.u = new Vertex(u);
            edges = new LinkedList<>();
        }

        public Vertex getVertex() {
            return u;
        }
    }
}

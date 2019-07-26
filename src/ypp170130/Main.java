package ypp170130;

import java.util.Scanner;

/**
 * Driver Code for computing Capacitated Minimum Weight Spanning Tree using
 * 1. Modified Kruskal's Algorithm
 * 2. Esau Williams Heuristic
 */
public class Main {

    public static void main(String[] args) {
        // write your code here
        String s = "6 15    1 3 3   1 2 4   0 1 5   2 4 5   0 2 6   3 4 6   3 5 6   4 5 7   2 3 8    1 4 8   0 3 9  1 5 10  0 4 12  2 5 12  0 5 15";
        Graph g = Graph.construct(new Scanner(s));
        g.printGraph();
    }
}

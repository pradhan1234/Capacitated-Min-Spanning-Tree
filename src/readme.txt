Implementation of CMST Problem

Homework 6: 

	Capacitated Minimum Spanning Tree using:

	1. The modified Kruskal’s algorithm
	2. The Esau-Williams Algorithm

Programming Language: Java

@Authors:
Yash Pradhan: ypp170130

Instructions to compile and execute code:

The folder with name: ypp170130 contains java source files "Graph.java" and "WMST.java"

Steps for running code from cmd prompt:

NOTE: while executing from command prompt, the pwd should be the 'src' directory containing the directory ypp170130. Driver code is inside WMST.java

1. Compile
> javac ypp170130/WMST.java

2. Run
> java ypp170130/WMST


Output:

The steps are printed for the first graph, and then only the results are printed for other 3.

YPRADHAN-M-W3Z6:src ypradhan$ java ypp170130/WMST
______________________________________________
Graph: n: 6, m: 15
0(0) :  (0,1) [5] (0,2) [6] (0,3) [9] (0,4) [12] (0,5) [15]
1(1) :  (1,3) [3] (1,2) [4] (0,1) [5] (1,4) [8] (1,5) [10]
2(1) :  (1,2) [4] (2,4) [5] (0,2) [6] (2,3) [8] (2,5) [12]
3(1) :  (1,3) [3] (3,4) [6] (3,5) [6] (2,3) [8] (0,3) [9]
4(1) :  (2,4) [5] (3,4) [6] (4,5) [7] (1,4) [8] (0,4) [12]
5(1) :  (3,5) [6] (4,5) [7] (1,5) [10] (2,5) [12] (0,5) [15]
______________________________________________

Modified Kruskals Algorithm

Sorted Edges
(1,3) [3]
(1,2) [4]
(0,1) [5]
(2,4) [5]
(0,2) [6]
(3,4) [6]
(3,5) [6]
(4,5) [7]
(1,4) [8]
(2,3) [8]
(0,3) [9]
(1,5) [10]
(0,4) [12]
(2,5) [12]
(0,5) [15]

(1,3) [3]: accept
(1,2) [4]: accept
(0,1) [5]: accept
(2,4) [5]: constraint violation
(0,2) [6]: reject, already connected
(3,4) [6]: constraint violation
(3,5) [6]: constraint violation
(4,5) [7]: accept
(1,4) [8]: constraint violation
(2,3) [8]: reject, already connected
(0,3) [9]: reject, already connected
(1,5) [10]: constraint violation
(0,4) [12]: accept
spanning tree generated

Results:
Spanning Tree Edges:
(1,3) [3]
(1,2) [4]
(0,1) [5]
(4,5) [7]
(0,4) [12]

Weight: 31
______________________________________________

Esau Williams Heuristic




iteration: 1
trade-off(1):  -2
trade-off(2):  -2
trade-off(3):  -6
trade-off(4):  -7
trade-off(5):  -9

minimum trade-off: -9
select (3,5) [6]


iteration: 2
trade-off(1):  -2
trade-off(2):  -2
trade-off(3):  -6
trade-off(4):  -7
trade-off(5):  -2

minimum trade-off: -7
select (2,4) [5]


iteration: 3
trade-off(1):  -2
trade-off(2):  -2
trade-off(3):  -6
trade-off(4):  0
trade-off(5):  -2

minimum trade-off: -6
select (1,3) [3]


iteration: 4
trade-off(1):  -1
trade-off(2):  -2
trade-off(3):  1
trade-off(4):  0
trade-off(5):  2

minimum trade-off: -2
constraint violation
reject (1,2) [4]


iteration: 5
trade-off(1):  3
trade-off(2):  2
trade-off(3):  1
trade-off(4):  0
trade-off(5):  2

minimum trade-off: 0
constraint violation
reject (3,4) [6]


iteration: 6
trade-off(1):  3
trade-off(2):  2
trade-off(3):  3
trade-off(4):  1
trade-off(5):  2

minimum trade-off: 1
constraint violation
reject (4,5) [7]

terminate algorithm

Results:
Spanning Tree Edges:
(1,3) [3]
(0,1) [5]
(2,4) [5]
(0,2) [6]
(3,5) [6]

Weight: 25
______________________________________________
Graph: n: 7, m: 21
0(0) :  (0,1) [5] (0,2) [6] (0,3) [9] (0,4) [10] (0,5) [11] (0,6) [15]
1(1) :  (0,1) [5] (1,2) [9] (1,3) [6] (1,4) [6] (1,5) [8] (1,6) [17]
2(1) :  (0,2) [6] (1,2) [9] (2,3) [7] (2,4) [9] (2,5) [8] (2,6) [12]
3(1) :  (0,3) [9] (1,3) [6] (2,3) [7] (3,4) [10] (3,5) [5] (3,6) [11]
4(1) :  (0,4) [10] (1,4) [6] (2,4) [9] (3,4) [10] (4,5) [14] (4,6) [9]
5(1) :  (0,5) [11] (1,5) [8] (2,5) [8] (3,5) [5] (4,5) [14] (5,6) [8]
6(1) :  (0,6) [15] (1,6) [17] (2,6) [12] (3,6) [11] (4,6) [9] (5,6) [8]
______________________________________________

Modified Kruskals Algorithm



Results:
Spanning Tree Edges:
(0,1) [5]
(3,5) [5]
(0,2) [6]
(1,3) [6]
(2,4) [9]
(4,6) [9]

Weight: 40
______________________________________________

Esau Williams Heuristic

Results:
Spanning Tree Edges:
(0,1) [5]
(3,5) [5]
(0,2) [6]
(0,3) [9]
(5,6) [8]
(1,4) [6]

Weight: 39
______________________________________________
Graph: n: 7, m: 21
0(0) :  (0,1) [2] (0,2) [10] (0,3) [10] (0,4) [2] (0,5) [10] (0,6) [10]
1(1) :  (0,1) [2] (1,2) [1] (1,3) [10] (1,4) [10] (1,5) [10] (1,6) [10]
2(1) :  (0,2) [10] (1,2) [1] (2,3) [1] (2,4) [10] (2,5) [10] (2,6) [10]
3(1) :  (0,3) [10] (1,3) [10] (2,3) [1] (3,4) [10] (3,5) [10] (3,6) [10]
4(1) :  (0,4) [2] (1,4) [10] (2,4) [10] (3,4) [10] (4,5) [1] (4,6) [10]
5(1) :  (0,5) [10] (1,5) [10] (2,5) [10] (3,5) [10] (4,5) [1] (5,6) [1]
6(1) :  (0,6) [10] (1,6) [10] (2,6) [10] (3,6) [10] (4,6) [10] (5,6) [1]
______________________________________________

Modified Kruskals Algorithm



Results:
Spanning Tree Edges:
(1,2) [1]
(2,3) [1]
(4,5) [1]
(5,6) [1]
(0,1) [2]
(0,4) [2]

Weight: 8
______________________________________________

Esau Williams Heuristic

Results:
Spanning Tree Edges:
(0,1) [2]
(4,5) [1]
(0,4) [2]
(5,6) [1]
(1,2) [1]
(2,3) [1]

Weight: 8
______________________________________________
Graph: n: 6, m: 15
0(0) :  (0,1) [55] (0,2) [62] (0,3) [95] (0,4) [125] (0,5) [150]
1(1) :  (1,3) [35] (1,2) [42] (0,1) [55] (1,4) [88] (1,5) [100]
2(1) :  (1,2) [42] (2,4) [55] (0,2) [62] (2,3) [85] (2,5) [130]
3(1) :  (1,3) [35] (3,4) [63] (3,5) [65] (2,3) [85] (0,3) [95]
4(1) :  (2,4) [55] (3,4) [63] (4,5) [70] (1,4) [88] (0,4) [125]
5(1) :  (3,5) [65] (4,5) [70] (1,5) [100] (2,5) [130] (0,5) [150]
______________________________________________

Modified Kruskals Algorithm



Results:
Spanning Tree Edges:
(1,3) [35]
(1,2) [42]
(0,1) [55]
(4,5) [70]
(0,4) [125]

Weight: 327
______________________________________________

Esau Williams Heuristic

Results:
Spanning Tree Edges:
(1,3) [35]
(0,1) [55]
(2,4) [55]
(0,2) [62]
(3,5) [65]

Weight: 272

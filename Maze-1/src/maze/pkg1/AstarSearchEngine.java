package maze.pkg1;

import java.awt.Dimension;
import java.util.*;

public class AstarSearchEngine extends AbstractSearchEngine {

    ArrayList<Dimension> L = new ArrayList<Dimension>(), T = new ArrayList<Dimension>();
    PriorityQueue<Node> openList = new PriorityQueue<>();
    Map<Dimension, Node> closedList = new HashMap<>();
    Dimension predecessor[][];

    public AstarSearchEngine(int width, int height) {
        super(width, height);

        predecessor = new Dimension[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                predecessor[i][j] = null;

        // Create the starting node
        Node startNode = new Node(startLoc, 0, heuristic(startLoc));
        openList.add(startNode);

        doAStar();

        // Calculate the shortest path from the predecessor array
        maxDepth = 0;
        if (!isSearching) {
            searchPath[maxDepth++] = goalLoc;
            for (int i = 0; i < 100; i++) {
                searchPath[maxDepth] = predecessor[searchPath[maxDepth - 1].width][searchPath[maxDepth - 1].height];
                maxDepth++;
                if (equals(searchPath[maxDepth - 1], startLoc)) break;  // Back to starting node
            }
        }
    }

    private void doAStar() {

        while (!openList.isEmpty()) {

            Node currentNode = openList.poll();
            closedList.put(currentNode.location, currentNode);

            if (equals(currentNode.location, goalLoc)) {
                System.out.println("Goal found: (" + currentNode.location.width + ", " + currentNode.location.height + ")");
                isSearching = false;
                break;
            }

            Dimension[] connected = getPossibleMoves(currentNode.location);
            for (int i = 0; i < 4; i++) {

                if (connected[i] != null && !closedList.containsKey(connected[i])) {
                    int gCost = currentNode.gCost + 1; // Assuming equal cost for each move
                    int hCost = heuristic(connected[i]);
                    int fCost = gCost + hCost;

                    Node neighborNode = new Node(connected[i], gCost, hCost);
                    if (!openList.contains(neighborNode) || fCost < openList.peek().fCost) {
                        predecessor[connected[i].width][connected[i].height] = currentNode.location;
                        openList.add(neighborNode);
                    }
                }
            }
        }
    }

    // Implement your heuristic function here
    private int heuristic(Dimension location) {
        // Example: Manhattan distance
        return Math.abs(location.width - goalLoc.width) + Math.abs(location.height - goalLoc.height);
    }

    class Node implements Comparable<Node> {
        Dimension location;
        int gCost;
        int hCost;
        int fCost;

        public Node(Dimension location, int gCost, int hCost) {
            this.location = location;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.fCost, other.fCost);
        }
    }
}
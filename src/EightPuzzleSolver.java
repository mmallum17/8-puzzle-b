/**
 * The EightPuzzleSolver implements the A* search algorithm to solve an 8-puzzle.
 * The heuristic function used is the sum of manhattan distances for each tile.
 *
 * File: EightPuzzleSolver.java
 * Author: Marcus Mallum
 * Date: 9/11/2018
 */

import java.util.LinkedList;
import java.util.Scanner;

public class EightPuzzleSolver {
    private static Node goalNode = null;
    static final String GOAL_STATE = "012345678";

    // Possible results from A* search algorithm
    private static final int SOLUTION = 0;
    private static final int FAILURE = 1;

    // Possible actions for a tile
    private static final int DOWN = 0;
    private static final int RIGHT = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;

    // States that have already been explored
    private static LinkedList<String> exploredStates = new LinkedList<String>();

    /**
     * Method: main
     * Purpose: Controls execution of the program
     * Parameters: String[] args
     * Return value: void
     */
    public static void main(String[] args) {
        // Get the initial state and validate it
        String initialState = getInitialState();
        boolean validInitialState = validateInitialState(initialState);

        // If initial state is valid, solve the puzzle. Else, end program
        if(validInitialState) {
            System.out.println("Goal State is " + GOAL_STATE);

            int result = aStarSearch(initialState);

            // If result is solvable, print out the path. Else, display "No solution"
            if(result == SOLUTION) {
                System.out.print("Solution: ");
                displayPath(goalNode);
            }
            else {
                System.out.println("No Solution");
            }
        }
        else {
            System.out.println("Input for the initial state is invalid");
        }
    }

   /**
    * Method: getInitialState
    * Purpose: Prompts the user for the initial state and returns it as a string
    * Parameters: void
    * Return value: String, initial state
    */
    private static String getInitialState() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter in the initial state (Ex: '013254687'): ");
        String input = scanner.nextLine();

        return input;
    }

   /**
    * Method: validateInitialState
    * Purpose: Determines whether the initial state is valid
    * Parameters: String, initial state
    * Return value: boolean, true if initial state is valid, otherwise false
    */
    private static boolean validateInitialState(String initialState) {
        boolean valid = true;

        // If incorrect length, initial state is invalid
        if(initialState.length() != 9) {
            valid = false;
        }
        else {
            for(int i = 0; i <= 8; i++) {
                // If a tile is missing, the initial state is invalid
                if(!initialState.contains(Integer.toString(i))) {
                    valid = false;
                }
            }
        }
        return valid;
    }

   /**
    * Method: aStarSearch
    * Purpose: Implementation of A* search algorithm.
    * Parameters: String, initial state
    * Return value: int, SOLUTION or FAILURE
    */
    private static int aStarSearch(String initialState) {
        // Initialize initial state and the result
        Node node = new Node(initialState, null, -1, 0);
        int result = -1;

        // Add the initial state to the fringe
        LinkedList<Node> fringe = new LinkedList<Node>();
        fringe.add(node);

        // Loop until puzzle has been determined to have a SOLUTION or FAILURE
        while(result == -1) {
            // If the fringe is empty, puzzle has no solution
            if(fringe.isEmpty()) {
                result = FAILURE;
            }
            node = removeOptimalNode(fringe);   // Get node from the fringe with the least cost
            exploredStates.add(node.state);     // Add node to states that have been explored
            // If node has the goal state, puzzle has been solved
            if(node.state.equals(GOAL_STATE)) {
                goalNode = node;
                result = SOLUTION;
            }
            // Add all successors of the node to the fringe
            LinkedList<Node> successors = getSuccessors(node);
            fringe.addAll(successors);
        }

        return result;
    }

   /**
    * Method: removeOptimalNode
    * Purpose: Return the node from the fringe with the least cost
    * Parameters: LinkedList, the fringe
    * Return value: Node, node from the fringe with the least cost
    */
    private static Node removeOptimalNode(LinkedList<Node> fringe) {
        // Get first node from the fringe
        Node optimalNode = fringe.getFirst();

        // Find the node from the fringe with the least cost
        for(int i = 1; i < fringe.size(); i++) {
            Node temp = fringe.get(i);
            if(temp.cost < optimalNode.cost) {
                optimalNode = temp;
            }
        }

        // Remove node from fringe and return it
        fringe.remove(optimalNode);
        return optimalNode;
    }

   /**
    * Method: getSuccessors
    * Purpose: Returns a linked list of successors for a given node
    * Parameters: Node, current node
    * Return value: LinkedList, all successors of the provided node
    */
    private static LinkedList<Node> getSuccessors(Node node) {
        // Get state of the current node
        String state = node.state;
        LinkedList<Node> successors = new LinkedList<Node>();

        // Get the position of the blank space
        int blankSpace = state.indexOf('0');

        // Check possible actions. If action is possible, add a successor
        // Check down
        if(blankSpace >= 3 && blankSpace <= 8) {
            String newState = swap(state, blankSpace, blankSpace - 3);
            if(!exploredStates.contains(newState)) {
                successors.add(new Node(newState, node, DOWN, node.depth + 1));
            }
        }
        // Check right
        if(blankSpace % 3 != 0) {
            String newState = swap(state, blankSpace, blankSpace - 1);
            if(!exploredStates.contains(newState)) {
                successors.add(new Node(newState, node, RIGHT, node.depth + 1));
            }
        }
        // Check left
        if(blankSpace % 3 != 2) {
            String newState = swap(state, blankSpace, blankSpace + 1);
            if(!exploredStates.contains(newState)) {
                successors.add(new Node(newState, node, LEFT, node.depth + 1));
            }
        }
        // Check up
        if(blankSpace >= 0 && blankSpace <= 5) {
            String newState = swap(state, blankSpace, blankSpace + 3);
            if(!exploredStates.contains(newState)) {
                successors.add(new Node(newState, node, UP, node.depth + 1));
            }
        }

        return successors;
    }

   /**
    * Method: swap
    * Purpose: Swap two characters in a string
    * Parameters: String, string to modify
    *             int, index of first character to swap
    *             int, index of second character to swap
    * Return value: String, string with the 2 characters swapped
    */
    private static String swap(String str, int i, int j)
    {
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(i, str.charAt(j));
        sb.setCharAt(j, str.charAt(i));
        return sb.toString();
    }

   /**
    * Method: displayPath
    * Purpose: Display the final path of the puzzle
    * Parameters: Node, the goal node from the puzzle
    * Return value: void
    */
    private static void displayPath(Node goalNode) {
        StringBuilder sb = new StringBuilder();
        Node node = goalNode;

        // Iterate from the goal node up to the root, tracking the action from child to parent
        while(node.parentNode != null) {
            switch(node.action){
                case DOWN:
                    sb.append('D');
                    break;
                case RIGHT:
                    sb.append('R');
                    break;
                case LEFT:
                    sb.append('L');
                    break;
                case UP:
                    sb.append('U');
                    break;
            }
            node = node.parentNode;
        }

        // Since iteration was done from goal node to root, the path needs to be reversed
        sb = sb.reverse();

        // Print out the path
        for(int i = 0; i < sb.length(); i++) {
            System.out.print(sb.charAt(i));
            if(i < sb.length() - 1) {
                System.out.print('-');
            }
        }
        System.out.println();
    }
}

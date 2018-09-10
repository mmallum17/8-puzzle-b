import java.util.LinkedList;

/**
 * Created by Marcus on 9/9/2018.
 */
public class EightPuzzleSolver {

    private static Node goalNode = null;
    static final String GOAL_STATE = "012345678";
    private static final int SOLUTION = 0;
    private static final int FAILURE = 1;
    private static final int DOWN = 0;
    private static final int RIGHT = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;
    private static LinkedList<String> exploredStates = new LinkedList<String>();

    public static void main(String[] args) {
        String initialState = "142358670";
        int result = aStarSearch(initialState);
        System.out.println(result);
        if(result == SOLUTION) {
            displayPath(goalNode);
        }
//        if(goalNode != null) {
//            System.out.println(goalNode.state);
//        }
    }

    private static int aStarSearch(String initialState) {
        Node node = new Node(initialState, null, -1, 0);
        int result = -1;
        LinkedList<Node> fringe = new LinkedList<Node>();
        fringe.add(node);

        while(result == -1) {
            if(fringe.isEmpty()) {
                result = FAILURE;
            }
            node = removeOptimalNode(fringe);
            exploredStates.add(node.state);
            if(node.state.equals(GOAL_STATE)) {
                goalNode = node;
                result = SOLUTION;
            }
            LinkedList<Node> successors = getSuccessors(node);
            fringe.addAll(successors);
        }

        return result;
    }

    private static Node removeOptimalNode(LinkedList<Node> fringe) {
        Node optimalNode = fringe.getFirst();
        for(int i = 1; i < fringe.size(); i++) {
            Node temp = fringe.get(i);
            if(temp.cost < optimalNode.cost) {
                optimalNode = temp;
            }
        }
        fringe.remove(optimalNode);

        return optimalNode;
    }

    private static LinkedList<Node> getSuccessors(Node node) {
        String state = node.state;
        LinkedList<Node> successors = new LinkedList<Node>();

        int blankSpace = state.indexOf('0');
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

    private static String swap(String str, int i, int j)
    {
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(i, str.charAt(j));
        sb.setCharAt(j, str.charAt(i));
        return sb.toString();
    }

    private static void displayPath(Node goalNode) {
        StringBuilder sb = new StringBuilder();
        Node node = goalNode;

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

        sb = sb.reverse();
        for(int i = 0; i < sb.length(); i++) {
            System.out.print(sb.charAt(i));
            if(i < sb.length() - 1) {
                System.out.print('-');
            }
        }
    }
}

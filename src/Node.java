/**
 * Created by Marcus on 9/9/2018.
 */
public class Node {
    String state;
    Node parentNode;
    int action;
    int depth;
    int cost;

    public Node(String state, Node parentNode, int action, int depth) {
        this.state = state;
        this.parentNode = parentNode;
        this.action = action;
        this.depth = depth;
        setCost(depth);
    }

    private void setCost(int depth) {
        cost = 0;

        for(int i = 1; i <= 8; i++) {
            int currentCol = state.charAt(i) % 3;
            int currentRow = state.charAt(i) / 3;
            int goalCol = EightPuzzleSolver.GOAL_STATE.charAt(i) % 3;
            int goalRow = EightPuzzleSolver.GOAL_STATE.charAt(i) / 3;

            int manhattanDist = Math.abs(goalCol - currentCol) + Math.abs(goalRow - currentRow);
            cost += manhattanDist;
        }

        cost += depth;
    }
}

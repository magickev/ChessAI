package Utils;

import Board.Board;

public abstract class Node {
    public Board b;
    int heuristicVal = -666;

    public abstract Node newNode(Board b, boolean whitesTurn);

    public boolean isWhitesTurn() {
        return whitesTurn;
    }

    boolean whitesTurn;

    public Node(Board b, boolean whitesTurn) {
        this.b = b;
        this.whitesTurn = whitesTurn;
    }

    public abstract int getHeuristicVal(float weight);
}

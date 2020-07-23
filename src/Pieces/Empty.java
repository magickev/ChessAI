package Pieces;

import Board.Board;
import Utils.Coordinate;

import java.util.LinkedList;
import java.util.Objects;

public class Empty extends Piece {
    @Override
    public LinkedList<Coordinate> getAllMoves(Board b) {
        return null;
    }

    @Override
    public boolean canMoveOntoHere(Piece p) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return isEmpty == piece.isEmpty;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isEmpty);
    }

    @Override
    public String getStringRep() {
        return stringRep + "  ";
    }

    @Override
    public boolean areOppositePieces(Piece p) {
        return false;
    }

    public Empty()
    {
        super(false, "E");
        isEmpty = true;
        pieceVal = 0;
    }
}

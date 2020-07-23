package Pieces;

import Board.Board;
import Utils.Coordinate;

import java.util.LinkedList;
import java.util.Objects;

public abstract class Piece implements Cloneable{
    protected int pieceVal;
    public boolean white;
    protected String stringRep;
    protected boolean isDead = false;
    protected boolean isEmpty = false;
    public boolean inOriginalSpot = true;//to know when to move twice on pawns also stuff with castling
    public int x;
    public int y;

    public Coordinate pieceLoc(){
        return new Coordinate(x, y);
    }

    public abstract LinkedList<Coordinate> getAllMoves(Board b);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return pieceVal == piece.pieceVal &&
                white == piece.white &&
                isDead == piece.isDead &&
                isEmpty == piece.isEmpty &&
                x == piece.x &&
                y == piece.y &&
                stringRep.equals(piece.stringRep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceVal, white, stringRep, isDead, isEmpty, x, y);
    }

    public int getPieceVal(){
        return pieceVal;
    }

    public boolean canMoveOntoHere(Piece p){//TODO have to do something for castling here if i want
        if(p.areOppositePieces(this) || p.isEmpty)
            return true;
        return false;
    }

    public boolean isValidMove(Board b, Coordinate postMove){
        LinkedList<Coordinate> validMoves = getAllMoves(b);
        return validMoves.contains(postMove);
    }

    public void kill(Board b){
        isDead = true;
    }

    public boolean isWhite(){
        return white;
    }

    public String getStringRep(){
        if(white)
            return "W" + stringRep + " ";
        return "B" + stringRep + " ";
    }

    Piece(boolean white, String stringRep) {
        this.white = white;
        this.stringRep = stringRep;
    }

    public Piece(int pieceVal, boolean white, String stringRep, boolean isDead, boolean isEmpty, boolean inOriginalSpot) {
        this.pieceVal = pieceVal;
        this.white = white;
        this.stringRep = stringRep;
        this.isDead = isDead;
        this.isEmpty = isEmpty;
        this.inOriginalSpot = inOriginalSpot;
    }

    public boolean isEmpty(){
        return isEmpty;
    }

    public boolean areOppositePieces(Piece p){
        if(((p.white && !this.white) || (!p.white && this.white)))
            return true;
        return false;
    }

    public Object clone(){
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

}

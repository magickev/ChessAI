package Pieces;

import Board.Board;
import Utils.Coordinate;

import java.util.LinkedList;

public class Knight extends Piece {


    public Knight(boolean white) {
        super(white, "KN");
        pieceVal = 3;
    }


    @Override
    public LinkedList<Coordinate> getAllMoves(Board b) {
        LinkedList<Coordinate> allMoves = new LinkedList<>();

        //Coordinate pieceLoc = b.getPieceLoc(this);
        Coordinate pieceLoc = this.pieceLoc();

        //vertical moves (move x by +-2, move y by +-1)
        for(int x = pieceLoc.x - 2; x <= pieceLoc.x + 2; x+=4){
            for(int y = pieceLoc.y - 1; y <= pieceLoc.y + 1; y+=2){
                Piece destPiece = b.getPieceAt(x, y);
                if(destPiece != null){
                    boolean canMove = destPiece.canMoveOntoHere(this);
                    if(canMove)
                        allMoves.add(new Coordinate(x, y));
                }
            }
        }

        //horizontal moves (move y by +- 2, move x by +-1)
        for(int y = pieceLoc.y - 2; y <= pieceLoc.y + 2; y+=4){
            for(int x = pieceLoc.x - 1; x <= pieceLoc.x + 1; x+=2){
                Piece destPiece = b.getPieceAt(x, y);
                if(destPiece != null){
                    boolean canMove = destPiece.canMoveOntoHere(this);
                    if (canMove)
                        allMoves.add(new Coordinate(x, y));
                }
            }
        }
        return allMoves;
    }

    @Override
    public String getStringRep() {
        String s = super.getStringRep();
        return s.substring(0, s.length() - 1);
    }
}

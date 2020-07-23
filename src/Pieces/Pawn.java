package Pieces;

import Board.Board;
import Utils.Coordinate;

import java.util.LinkedList;

public class Pawn extends Piece {



    public Pawn(boolean white) {
        super(white, "P");
        pieceVal = 1;
    }


    @Override
    public LinkedList<Coordinate> getAllMoves(Board b) {
        LinkedList<Coordinate> allMoves = new LinkedList<>();
        //Coordinate pieceLoc = b.getPieceLoc(this);
        Coordinate pieceLoc = this.pieceLoc();
        if(inOriginalSpot){//check for moves 2 in front of pawn
            int xLoc;
            int xLocForOneAhead;
            if(white){//x-2
                xLoc = pieceLoc.x - 2;
                xLocForOneAhead = pieceLoc.x - 1;
            }
            else{//x+2
                xLoc = pieceLoc.x + 2;
                xLocForOneAhead = pieceLoc.x + 1;
            }
            Piece twoPlacesAhead = b.getPieceAt(xLoc, pieceLoc.y);
            Piece onePlaceAhead = b.getPieceAt(xLocForOneAhead, pieceLoc.y);
            if(twoPlacesAhead != null && twoPlacesAhead.isEmpty() && onePlaceAhead.isEmpty)
                allMoves.add(new Coordinate(xLoc, pieceLoc.y));
        }

        //check for 1 move in front of pawn
        int xLoc;
        if(white)//x-2
            xLoc = pieceLoc.x - 1;
        else//x+2
            xLoc = pieceLoc.x + 1;
        Piece onePlaceAhead = b.getPieceAt(xLoc, pieceLoc.y);
        if(onePlaceAhead != null && onePlaceAhead.isEmpty())
            allMoves.add(new Coordinate(xLoc, pieceLoc.y));

        //ATTACKING ENEMY
        //check for diagonal moves(only if enemy is diagonal)
        //Check left(use same x vals from last)
        Piece oneDiagonal = b.getPieceAt(xLoc, pieceLoc.y - 1);
        if(oneDiagonal != null && oneDiagonal.areOppositePieces(this))
            allMoves.add(new Coordinate(xLoc, pieceLoc.y - 1));

        //check right
        oneDiagonal = b.getPieceAt(xLoc, pieceLoc.y + 1);
        if(oneDiagonal != null && oneDiagonal.areOppositePieces(this))
            allMoves.add(new Coordinate(xLoc, pieceLoc.y + 1));

        return allMoves;
    }
}

package Pieces;

import Board.Board;
import Utils.Coordinate;
import Utils.DirectionalPieces;

import java.util.LinkedList;

public class Rook extends Piece {



    public Rook(boolean white) {
        super(white, "R");
        pieceVal = 5;
    }

    @Override
    public LinkedList<Coordinate> getAllMoves(Board b) {
        LinkedList<Coordinate> allMoves = new LinkedList<>();

        //Coordinate pieceLoc = b.getPieceLoc(this);
        Coordinate pieceLoc = this.pieceLoc();

        DirectionalPieces nearPieces = b.getClosestStraightPieces(pieceLoc);


        //checks vertical
        for(int x = nearPieces.north.x; x <= nearPieces.south.x; x++){
            Piece destPiece = b.getBoard()[x][pieceLoc.y];
            boolean canMove = destPiece.canMoveOntoHere(this);
            if(canMove)
                allMoves.add(new Coordinate(x, pieceLoc.y));
        }


        //checks horizontal
        for(int y = nearPieces.west.y; y <= nearPieces.east.y; y++){
            Piece destPiece = b.getBoard()[pieceLoc.x][y];
            boolean canMove = destPiece.canMoveOntoHere(this);
            if(canMove)
                allMoves.add(new Coordinate(pieceLoc.x, y));//TODO change to y (keep now just for easy debugging of -666 bug)
        }

        return allMoves;

    }
}

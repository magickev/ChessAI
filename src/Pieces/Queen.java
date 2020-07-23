package Pieces;

import Board.Board;
import Utils.Coordinate;
import Utils.DirectionalPieces;

import java.util.LinkedList;

public class Queen extends Piece {
    public Queen(boolean white) {
        super(white, "Q");
        pieceVal = 9;
    }


    @Override
    public LinkedList<Coordinate> getAllMoves(Board b) {

        LinkedList<Coordinate> allMoves = new LinkedList<>();
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
                allMoves.add(new Coordinate(pieceLoc.x, y));
        }


        nearPieces = b.getClosestDiagonalPieces(pieceLoc);
        //checks top right down to bottom left (diagonally)
        for(int x = nearPieces.TR.x, y = nearPieces.TR.y; x <= nearPieces.BL.x && y >= nearPieces.BL.y; x++, y--){
            Piece destPiece = b.getBoard()[x][y];
            boolean canMove = destPiece.canMoveOntoHere(this);
            if(canMove)
                allMoves.add(new Coordinate(x, y));
        }

        //checks top left down to bottom right(diagonally)
        for(int x = nearPieces.TL.x, y = nearPieces.TL.y; x <= nearPieces.BR.x && y <= nearPieces.BR.y; x++, y++){
            Piece destPiece = b.getBoard()[x][y];
            boolean canMove = destPiece.canMoveOntoHere(this);
            if(canMove)
                allMoves.add(new Coordinate(x, y));
        }

        return allMoves;
    }
}

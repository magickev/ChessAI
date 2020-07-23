package Pieces;

import Board.Board;
import Utils.Coordinate;

import java.util.LinkedList;

public class King extends Piece {
    public King(boolean white) {
        super(white, "K");
        pieceVal = 1000;
    }


    @Override
    public LinkedList<Coordinate> getAllMoves(Board b) {
        LinkedList<Coordinate> allMoves = new LinkedList<>();

        //Coordinate pieceLoc = b.getPieceLoc(this);
        Coordinate pieceLoc = this.pieceLoc();

        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                Piece destPiece = b.getPieceAt(pieceLoc.x + i, pieceLoc.y + j);
                if(destPiece != null && canMoveOntoHere(destPiece))
                    allMoves.add(new Coordinate(pieceLoc.x + i, pieceLoc.y + j));
            }
        }

        return allMoves;
    }

    @Override
    public void kill(Board b) {
        b.setKingDead(this);
        super.kill(b);

    }
}

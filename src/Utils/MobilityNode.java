package Utils;

import Board.Board;
import Pieces.Piece;

import java.util.LinkedList;

public class MobilityNode extends Node {


    @Override
    public Node newNode(Board b, boolean whitesTurn) {
        return new MobilityNode(b, whitesTurn);
    }

    public MobilityNode(Board b, boolean whitesTurn) {
        super(b, whitesTurn);
    }

    @Override
    public int getHeuristicVal(float weight) {
        if(heuristicVal != -666)
            return heuristicVal;
        //need to calculate it - white minus black - large values favor white
        //need to get mobility score (white mobility - black mobility)
        int blackTotal = 0;
        int whiteTotal = 0;

        int blackMobility = 0;
        int whiteMobility = 0;
        //int Total = 0;
        for(Piece[] pArr: b.getBoard()){
            for(Piece p: pArr){
                if(p.isEmpty()) continue; //just in case?
                int pieceMovesSize = p.getAllMoves(b).size();
                if(p.isWhite()) {
                    whiteTotal += p.getPieceVal();
                    whiteMobility += pieceMovesSize;
                }
                if(!p.isWhite()) {
                    blackTotal += p.getPieceVal();
                    blackMobility += pieceMovesSize;
                }
            }
        }
        int material =  whiteTotal - blackTotal;

        int mobility = whiteMobility - blackMobility;
        //System.out.println("mobility:" + mobility + "material" + material);
        return Math.round(material + (((float)mobility)*weight));
    }
}

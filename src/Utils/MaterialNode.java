package Utils;

import Board.Board;
import Pieces.Piece;

public class MaterialNode extends Node {
    @Override
    public Node newNode(Board b, boolean whitesTurn) {
        return new MaterialNode(b, whitesTurn);
    }

    public MaterialNode(Board b, boolean whitesTurn) {
        super(b, whitesTurn);
    }

    @Override
    public int getHeuristicVal(float weight) {//
        if(heuristicVal != -666)
            return heuristicVal;
        //need to calculate it - white minus black - large values favor white
        int blackTotal = 0;
        int whiteTotal = 0;
        //int Total = 0;
        for(Piece[] pArr: b.getBoard()){
            for(Piece p: pArr){
                //System.out.println(p.getPieceVal());
                if(p.isWhite())
                    whiteTotal += p.getPieceVal();
                if(!p.isWhite())
                    blackTotal += p.getPieceVal();
            }
        }
        return whiteTotal - blackTotal;
    }
}

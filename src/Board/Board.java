package Board;

import Pieces.*;
import Utils.Coordinate;
import Utils.DirectionalPieces;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

import static Constants.Constants.*;

public class Board{

    public Board() {
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                board[i][j].x = i;
                board[i][j].y = j;
            }
        }
    }

    public Board(LinkedList<Piece> deadPieces, Piece[][] board) {
        this.deadPieces = deadPieces;
        this.board = board.clone();
    }


    public boolean isKingDead() {
        if(kingDead == null)
            return false;
        return true;
    }

    public void setKingDead(Piece kingDead) {
        this.kingDead = kingDead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if (!this.board[i][j].equals(board1.board[i][j]))
                    return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(board);
    }

    public Piece getDeadKing(){
        return kingDead;
    }

    public LinkedList<Piece> deadPieces = new LinkedList<>();
    Piece kingDead = null;
    Piece[][] board = new Piece [][]{
            {new Rook(false), new Knight(false), new Bishop(false), new Queen(false), new King(false), new Bishop(false), new Knight(false), new Rook(false)},
            {new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false), new Pawn(false)},
            {new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
            {new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
            {new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
            {new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty(), new Empty()},
            {new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true), new Pawn(true)},
            {new Rook(true), new Knight(true), new Bishop(true), new Queen(true), new King(true), new Bishop(true), new Knight(true), new Rook(true)},

    };
    public void printBoard(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                System.out.print(board[i][j].getStringRep() + " ");
            }
            System.out.println("");
        }
    }

    public boolean onlyKingsOrPawnsLeft(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                Piece p = board[i][j];
                if(!(p instanceof King) && !(p instanceof Pawn) && (!p.isEmpty()))
                    return false;
            }
        }
        return true;
    }

    public boolean setBoard(int x, int y, Piece p){//TODO error checking
        board[x][y] = p;
        p.x = x;
        p.y = y;
        return true;
    }

    public Board movePiece(Coordinate pre, Coordinate post){
        Piece prePiece = board[pre.x][pre.y];

        if(!prePiece.isValidMove(this, post))
            return null;

        Board newBoard = this.clone();

        Piece newprePiece = newBoard.getPieceAt(pre.x, pre.y);
        Piece newpostPiece = newBoard.getPieceAt(post.x, post.y);

        newprePiece.inOriginalSpot = false;

        newBoard.setBoard(pre.x, pre.y, new Empty());
        newBoard.setBoard(post.x, post.y, newprePiece);

        if(!newpostPiece.isEmpty()){
            newpostPiece.kill(newBoard);
            newBoard.deadPieces.add(newpostPiece);
            newpostPiece.x = -666;
            newpostPiece.y = -666;
        }

        return newBoard;
    }

    public HashMap<Piece, LinkedList<Coordinate>> getAllBoardMoves(boolean whitesMove){
        HashMap<Piece, LinkedList<Coordinate>> allMoves = new HashMap<>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                Piece p = board[i][j];
                if((!p.isEmpty()) && ((whitesMove && p.isWhite()) || (!whitesMove) && (!p.isWhite())))
                    allMoves.put(p, p.getAllMoves(this));
            }
        }
        return allMoves;
    }


    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPieceAt(int x, int y){//helps with out of bounds checking
        if(x < 0 || x > 7 || y < 0 || y > 7)
            return null;
        return board[x][y];
    }


    public DirectionalPieces getClosestStraightPieces(Coordinate middlePieceLoc){//if it goes out of bounds, returns original location
        Piece p = board[middlePieceLoc.x][middlePieceLoc.y];
        DirectionalPieces ret = new DirectionalPieces();

        int i = middlePieceLoc.x;
        while(i > 0){
            i--;
            if(!board[i][middlePieceLoc.y].isEmpty())//if theres a piece
                break;
        }
        ret.north = new Coordinate(i, middlePieceLoc.y);

        i = middlePieceLoc.x;
        while(i < 7){
            i++;
            if(!board[i][middlePieceLoc.y].isEmpty())//if theres a piece
                break;
        }
        ret.south = new Coordinate(i, middlePieceLoc.y);

        i = middlePieceLoc.y;
        while(i > 0){
            i--;
            if(!board[middlePieceLoc.x][i].isEmpty())//if theres a piece
                break;
        }
        ret.west = new Coordinate(middlePieceLoc.x, i);

        i = middlePieceLoc.x;
        while(i < 7){
            i++;
            if(!board[middlePieceLoc.x][i].isEmpty())//if theres a piece
                break;
        }
        ret.east = new Coordinate(middlePieceLoc.x, i);
        return ret;
    }

    public DirectionalPieces getClosestDiagonalPieces(Coordinate middlePieceLoc){
        DirectionalPieces ret = new DirectionalPieces();
        //get top right (sub in x, add in y)
        int x = middlePieceLoc.x;
        int y = middlePieceLoc.y;
        while(x > 0 && y < 7){
            x--;
            y++;
            if(!board[x][y].isEmpty())//if theres a piece
                break;
        }
        ret.TR = new Coordinate(x, y);

        //get top left (sub in x, sub in y)
        x = middlePieceLoc.x;
        y = middlePieceLoc.y;
        while(x > 0 && y > 0){
            x--;
            y--;
            if(!board[x][y].isEmpty())//if theres a piece
                break;
        }
        ret.TL = new Coordinate(x, y);

        //get bottom right(add in x, add in y)
        x = middlePieceLoc.x;
        y = middlePieceLoc.y;
        while(x < 7 && y < 7){
            x++;
            y++;
            if(!board[x][y].isEmpty())//if theres a piece
                break;
        }
        ret.BR = new Coordinate(x, y);

        //get bottom left(add in x, sub in y)
        x = middlePieceLoc.x;
        y = middlePieceLoc.y;
        while(x < 7 && y > 0){
            x++;
            y--;
            if(!board[x][y].isEmpty())//if theres a piece
                break;
        }
        ret.BL = new Coordinate(x, y);
        return ret;
    }

    public Board clone(){//SHOULD DEEP COPY

        LinkedList<Piece> newdeadPieces = new LinkedList<>();
        for(Piece p: deadPieces){
            newdeadPieces.add((Piece)p.clone());
        }
        Piece[][] newBoard = new Piece[8][8];
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                newBoard[i][j] = (Piece)board[i][j].clone();
            }
        }
        return new Board(newdeadPieces, newBoard);

    }

}

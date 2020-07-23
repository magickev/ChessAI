import Board.Board;
import Pieces.Piece;
import Utils.Coordinate;
import Utils.MaterialNode;
import Utils.MobilityNode;
import Utils.Node;

import java.util.*;

class PieceAndVal{
    Piece p;
    int val;
    Coordinate c;

    public PieceAndVal(Piece p, int val, Coordinate c) {
        this.p = p;
        this.val = val;
        this.c = c;
    }
}

public class Main {
    static float mobilityWeight = (float)0.35;
    static long nodesVisited = 0;
    static HashMap<Board, Integer> prevMoves;
    //looked at https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning for pseudocode
    public static PieceAndVal alphaBeta(Node node, int depth, int alpha, int beta) {
        nodesVisited++;
        if(depth == 0 || node.b.isKingDead())
            return new PieceAndVal(null, node.getHeuristicVal(mobilityWeight), null);

        if(node.isWhitesTurn()) {//maximizing player
            int maxEval = Integer.MIN_VALUE;
            Piece maxPiece = null;
            Coordinate maxPieceMove = null;

            HashMap<Piece, LinkedList<Coordinate>> allList = node.b.getAllBoardMoves(node.isWhitesTurn());
            outer:
            for(Piece p: allList.keySet()){
                LinkedList<Coordinate> coordList = allList.get(p);
                for(Coordinate moveHere: coordList){
                    Board newBoard = node.b.movePiece(p.pieceLoc(), moveHere);
                    PieceAndVal tmpPieceAndVal = alphaBeta(node.newNode(newBoard, (!node.isWhitesTurn())), depth - 1, alpha, beta);
                    if(maxEval < tmpPieceAndVal.val){
                        maxEval = tmpPieceAndVal.val;
                        maxPiece = p;
                        maxPieceMove = moveHere;
                    }
                    alpha = Math.max(alpha, tmpPieceAndVal.val);
                    if(beta <= alpha)
                        break outer;
                }
            }
            return new PieceAndVal(maxPiece, maxEval, maxPieceMove);
        }

        else{//minimizing player
            int minEval = Integer.MAX_VALUE;
            Piece minPiece = null;
            Coordinate minPieceMove = null;

            HashMap<Piece, LinkedList<Coordinate>> allList = node.b.getAllBoardMoves(node.isWhitesTurn());
            outer:
            for(Piece p: allList.keySet()){
                LinkedList<Coordinate> coordList = allList.get(p);
                for(Coordinate moveHere: coordList){
                    Board newBoard = node.b.movePiece(p.pieceLoc(), moveHere);
                    PieceAndVal tmpPieceAndVal = alphaBeta(node.newNode(newBoard, (!node.isWhitesTurn())), depth - 1, alpha, beta);
                    if(minEval > tmpPieceAndVal.val){
                        minEval = tmpPieceAndVal.val;
                        minPiece = p;
                        minPieceMove = moveHere;
                    }
                    beta = Math.min(beta, tmpPieceAndVal.val);
                    if(beta <= alpha)
                        break outer;
                }
            }
            return new PieceAndVal(minPiece, minEval, minPieceMove);
        }
    }

    public static boolean hasRepeatedThreeTimes(Board currBoard){
        if(prevMoves.containsKey(currBoard) && prevMoves.get(currBoard) >= 2)
            return true;
        else
            return false;
    }

    //looked at https://en.wikipedia.org/wiki/Minimax for pseudocode
    public static PieceAndVal minimax(Node node, int depth){
        nodesVisited++;
        if(depth == 0 || node.b.isKingDead())
            return new PieceAndVal(null, node.getHeuristicVal(mobilityWeight), null);

        if(node.isWhitesTurn()) {//maximizing player
            int maxEval = Integer.MIN_VALUE;
            Piece maxPiece = null;
            Coordinate maxPieceMove = null;

            HashMap<Piece, LinkedList<Coordinate>> allList = node.b.getAllBoardMoves(node.isWhitesTurn());
            for(Piece p: allList.keySet()){
                LinkedList<Coordinate> coordList = allList.get(p);
                for(Coordinate moveHere: coordList){
                    Board newBoard = node.b.movePiece(p.pieceLoc(), moveHere);
                    PieceAndVal tmpPieceAndVal = minimax(node.newNode(newBoard, (!node.isWhitesTurn())), depth - 1);
                    if(maxEval < tmpPieceAndVal.val && (!hasRepeatedThreeTimes(newBoard))){
                        maxEval = tmpPieceAndVal.val;
                        maxPiece = p;
                        maxPieceMove = moveHere;
                    }
                }
            }
            return new PieceAndVal(maxPiece, maxEval, maxPieceMove);
        }

        else{//minimizing player
            int minEval = Integer.MAX_VALUE;
            Piece minPiece = null;
            Coordinate minPieceMove = null;

            HashMap<Piece, LinkedList<Coordinate>> allList = node.b.getAllBoardMoves(node.isWhitesTurn());
            for(Piece p: allList.keySet()){
                LinkedList<Coordinate> coordList = allList.get(p);
                for(Coordinate moveHere: coordList){
                    Board newBoard = node.b.movePiece(p.pieceLoc(), moveHere);
                    PieceAndVal tmpPieceAndVal = minimax(node.newNode(newBoard, (!node.isWhitesTurn())), depth - 1);
                    if(minEval > tmpPieceAndVal.val && (!hasRepeatedThreeTimes(newBoard))){
                        minEval = tmpPieceAndVal.val;
                        minPiece = p;
                        minPieceMove = moveHere;
                    }
                }
            }
            return new PieceAndVal(minPiece, minEval, minPieceMove);
        }
    }

    static Board doRandomMoves(Random r, int randomMovesToDo){//starts with whitesturn and empty board
        Board b = new Board();

        boolean whitesTurn = true;
        for(int i = 0; i < randomMovesToDo; i++){
            HashMap<Piece, LinkedList<Coordinate>> allList = b.getAllBoardMoves(whitesTurn);
            List<Piece> keysAsArray = new ArrayList<>(allList.keySet());

            LinkedList<Coordinate> randMoveList = null;

            Piece randPiece = null;

            while(randMoveList == null || randMoveList.size() == 0){
                randPiece = keysAsArray.get(r.nextInt(keysAsArray.size()));
                randMoveList = allList.get(randPiece);
            }

            Coordinate randMove = randMoveList.get(r.nextInt(randMoveList.size()));
            b = b.movePiece(randPiece.pieceLoc(), randMove);
            whitesTurn = !whitesTurn;
        }
        return b;
    }

    public static void main(String[] args) {
        boolean testing = false;//turn on when testing table of different winrates
        int whiteAvgTimeTaken = 0;
        int blackAvgTimeTaken = 0;
        int stalemateTimes = 100;//set these for number of stale moves before program calls it a stalemate

        boolean printResults = true;
        //HashMap<Float, String> mobWeightResults = new HashMap<>(); use this to test best weight

        boolean userInput = false;
        if(args[0].equals("UserInputTrue"))
             userInput = true;

        int numGamesToPlay;
        if(userInput)
            numGamesToPlay = 1;
        else{
            numGamesToPlay = Integer.parseInt(args[6]);
        }

        int whiteMovesDone = 0;
        int blackMovesDone = 0;
        int whiteNodesGenerated = 0;
        int blackNodesGenerated = 0;

        int blackWin = 0;
        int whiteWin = 0;
        int tie = 0;
        int i = 0;

        int firstDepthLimit = -1;
        int secondDepthLimit = -1;
        int depthLimitPlayingAgainstHuman = -1;

        if(!userInput && !testing) {
            firstDepthLimit = Integer.parseInt(args[1]);
            secondDepthLimit = Integer.parseInt(args[4]);
            printResults = Boolean.parseBoolean(args[8]);
        }
        else if(userInput){
            depthLimitPlayingAgainstHuman = Integer.parseInt(args[2]);
        }

        mainOuter:
        while (i < numGamesToPlay) {

            //Use this to test best weight
            /*
            if(i != 0 && i % 200 == 0){
                String s = "Black won: " + blackWin + "White won: " + whiteWin + "Ties: " + tie;
                mobWeightResults.put(mobilityWeight, s);
                mobilityWeight += (float)0.025;
                blackWin = 0;
                whiteWin = 0;
                tie = 0;

                for(float f: mobWeightResults.keySet()){
                    String sd = mobWeightResults.get(f);
                    System.out.println("for float weight:" + f + " " + sd);
                }
            }

             */

            Random trueRandom = new Random();//Seed of 2, alphabeta 4 minimax 3 epic fail //seed of 1, stalemate fail
            Board b = new Board();


            if(!userInput) {
                int randomMoveCount = Integer.parseInt(args[7]);
                b = doRandomMoves(trueRandom, randomMoveCount);
                MaterialNode mt = new MaterialNode(b, true);
                if (b.isKingDead() && Math.abs(mt.getHeuristicVal((float) 0.5)) > 5) {
                    continue;
                }
                if(printResults)
                    b.printBoard();
            }





            boolean whitesTurn = true;
            boolean userPlayingWhite = false;
            if(userInput && args.length >= 4 && args[3].equals("white"))
                userPlayingWhite = true;

            int prevdeadPiecesSize = 0;
            int timesDeadPiecesTheSame = 0;

            prevMoves = new HashMap<Board, Integer>();
            while (true) {
                long startTime = System.currentTimeMillis();
                PieceAndVal pav = null;
                long beforegen = nodesVisited;
                if(!userInput) {
                    if (whitesTurn) {
                        Node n = null;
                        if(args[2].equals("mobility"))
                            n = new MobilityNode(b, whitesTurn);
                        if(args[2].equals("material"))
                            n = new MaterialNode(b, whitesTurn);

                        if (args[0].equals("alphabeta"))
                            pav = alphaBeta(n, firstDepthLimit, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        if (args[0].equals("minimax"))
                            pav = minimax(n, firstDepthLimit);
                        whiteMovesDone++;
                        whiteNodesGenerated += nodesVisited - beforegen;
                    } else {
                        Node n = null;
                        if(args[5].equals("mobility"))
                            n = new MobilityNode(b, whitesTurn);
                        if(args[5].equals("material"))
                            n = new MaterialNode(b, whitesTurn);

                        if (args[3].equals("alphabeta"))
                            pav = alphaBeta(n, secondDepthLimit, Integer.MIN_VALUE, Integer.MAX_VALUE);
                        if (args[3].equals("minimax"))
                            pav = minimax(n, secondDepthLimit);
                        blackMovesDone++;
                        blackNodesGenerated += nodesVisited - beforegen;
                    }
                }
                else{
                    if (whitesTurn) {
                        if(userPlayingWhite){
                            System.out.print("input Piece x:");
                            Scanner sc = new Scanner(System.in);
                            int Piecex = sc.nextInt();
                            System.out.print("input Piece y:");
                            int Piecey = sc.nextInt();

                            System.out.print("input where piece to go x:");
                            int x = sc.nextInt();
                            System.out.print("input where piece to go y:");
                            int y = sc.nextInt();
                            pav = new PieceAndVal(b.getPieceAt(Piecex, Piecey), -1, new Coordinate(x, y));
                        }
                        else {
                            if (args[1].equals("alphabeta"))
                                pav = alphaBeta(new MaterialNode(b, whitesTurn), depthLimitPlayingAgainstHuman, Integer.MIN_VALUE, Integer.MAX_VALUE);
                            if (args[1].equals("minimax"))
                                pav = minimax(new MaterialNode(b, whitesTurn), depthLimitPlayingAgainstHuman);
                            whiteMovesDone++;
                            whiteNodesGenerated += nodesVisited - beforegen;
                        }
                    } else {
                        if(userPlayingWhite){
                            if (args[1].equals("alphabeta"))
                                pav = alphaBeta(new MaterialNode(b, whitesTurn), depthLimitPlayingAgainstHuman, Integer.MIN_VALUE, Integer.MAX_VALUE);
                            if (args[1].equals("minimax"))
                                pav = minimax(new MaterialNode(b, whitesTurn), depthLimitPlayingAgainstHuman);
                            whiteMovesDone++;
                            whiteNodesGenerated += nodesVisited - beforegen;
                        }
                        else{
                            System.out.print("input Piece x:");
                            Scanner sc = new Scanner(System.in);
                            int Piecex = sc.nextInt();
                            System.out.print("input Piece y:");
                            int Piecey = sc.nextInt();

                            System.out.print("input where piece to go x:");
                            int x = sc.nextInt();
                            System.out.print("input where piece to go y:");
                            int y = sc.nextInt();
                            pav = new PieceAndVal(b.getPieceAt(Piecex, Piecey), -1, new Coordinate(x, y));
                        }
                    }
                }
                if(whitesTurn)
                    whiteAvgTimeTaken += (System.currentTimeMillis() - startTime);
                else
                    blackAvgTimeTaken += (System.currentTimeMillis() - startTime);
                if (pav.p == null || pav.c == null) {
                    System.out.println("stalemate");
                    tie++;
                    break;
                }
                if(printResults) {
                    System.out.println("Move piece" + pav.p.getStringRep() + "from " + pav.p.x + "," + pav.p.y +
                            " to " + pav.c.x + "," + pav.c.y + "with a minimax val of " + pav.val);
                    System.out.println("nodes generated:" + nodesVisited);
                }
                b = b.movePiece(pav.p.pieceLoc(), pav.c);
                if(prevMoves.containsKey(b)){
                    int numSeen = prevMoves.get(b);
                    prevMoves.put(b, numSeen + 1);
                }
                else{
                    prevMoves.put(b, 1);
                }

                if(printResults) {
                    System.out.println(prevMoves.size());
                    b.printBoard();
                }
                whitesTurn = !whitesTurn;

                if (b.isKingDead()){
                    if(b.getDeadKing().isWhite())
                        blackWin++;
                    else
                        whiteWin++;
                    System.out.println("Black won: " + blackWin);
                    System.out.println("White won: " + whiteWin);
                    System.out.println("Ties: " + tie);
                    break;
                }
                if (prevdeadPiecesSize == b.deadPieces.size())
                    timesDeadPiecesTheSame++;
                else
                    timesDeadPiecesTheSame = 0;
                if (timesDeadPiecesTheSame >= stalemateTimes) {
                    System.out.println("stalemate");
                    tie++;
                    break;
                }
                prevdeadPiecesSize = b.deadPieces.size();
            }

            i++;
        }
        System.out.println("Black won: " + blackWin);
        System.out.println("White won: " + whiteWin);
        System.out.println("Ties: " + tie);
        System.out.println("black nodes generated per move (avg): " + blackNodesGenerated/blackMovesDone);
        System.out.println("white nodes generated per move (avg): " + whiteNodesGenerated/whiteMovesDone);
        System.out.println("black took an avg of " + blackAvgTimeTaken/blackMovesDone + "milliseconds");
        System.out.println("white took an avg of " + whiteAvgTimeTaken/whiteMovesDone + "milliseconds");

        //Use this to print out mobility weight checking
        /*
        for(float f: mobWeightResults.keySet()){
            String s = mobWeightResults.get(f);
            System.out.println("for float weight:" + f + " " + s);
        }

         */
    }
}

package Engine;

import Game.Board;
import Util.Util;
import Display.Display;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Engine {

    Board originalBoard;

    public Engine(Board board) {
        this.originalBoard = board;
    }

    public boolean turn;
    public int startingTurns = -1;

    public boolean showImage = false;

    public ArrayList<ArrayList<ArrayList<Integer>>> evaluateMoves(final Board board, int turnsLeft) {

        if (startingTurns == -1) {
            startingTurns = turnsLeft;
        }

        if (turnsLeft == 0) {
            ArrayList<ArrayList<ArrayList<Integer>>> out = new ArrayList<ArrayList<ArrayList<Integer>>>();
            out.add(new ArrayList<ArrayList<Integer>>());
            out.get(0).add(new ArrayList<Integer>());
            out.get(0).get(0).add(board.evaluatePosition(turn) - board.evaluatePosition(!turn));
            return out;
        } else {

            ArrayList<ArrayList<ArrayList<Integer>>> moves = board.getPossibleMovesFast();

            int bestMove = 0;
            ArrayList<ArrayList<ArrayList<Integer>>> maxScoreAndMoves = null;

            if (moves.size() == 0) {
                maxScoreAndMoves = new ArrayList<ArrayList<ArrayList<Integer>>>();
                if ((startingTurns - turnsLeft) % 2 == 0) {
                    maxScoreAndMoves.add(new ArrayList<ArrayList<Integer>>());
                    maxScoreAndMoves.get(0).add(new ArrayList<Integer>());
                    maxScoreAndMoves.get(0).get(0).add(1000);
                } else {
                    maxScoreAndMoves.add(new ArrayList<ArrayList<Integer>>());
                    maxScoreAndMoves.get(0).add(new ArrayList<Integer>());
                    maxScoreAndMoves.get(0).get(0).add(-1000);
                }
                return maxScoreAndMoves;
            }

            for (int i = 0; i < moves.size(); i++) {

                Board copy = new Board(board.whitePieces, board.blackPieces, board.turn);
                copy.moveNoCheck(moves.get(i).get(0), moves.get(i).get(1));
                copy.turn = !copy.turn;

                ArrayList<ArrayList<ArrayList<Integer>>> scoreAndMoves = evaluateMoves(copy, turnsLeft - 1);

//                    if(!showImage && turnsLeft == 1 && scoreAndMoves.get(0).get(0).get(0) < 0) {
//                        new Display(copy);
//                        System.out.println(moves.get(i).get(0).get(1) + "," + moves.get(i).get(0).get(2) + "," + moves.get(i).get(1).get(1) + "," + moves.get(i).get(1).get(2));
//                        System.out.println(scoreAndMoves.get(0).get(0).get(0));
//                        showImage = !showImage;
//                    }

                if (maxScoreAndMoves == null) {
                    maxScoreAndMoves = scoreAndMoves;
                    bestMove = i;
                }
                if ((startingTurns - turnsLeft) % 2 == 0) {
                    if (scoreAndMoves.get(0).get(0).get(0) < maxScoreAndMoves.get(0).get(0).get(0) || scoreAndMoves.get(0).get(0).get(0) == maxScoreAndMoves.get(0).get(0).get(0) && Math.abs(3 - moves.get(i).get(1).get(1)) < Math.abs(3 - moves.get(bestMove).get(1).get(1))) {

                        maxScoreAndMoves = scoreAndMoves;
                        bestMove = i;
                    }
                } else {
                    if (scoreAndMoves.get(0).get(0).get(0) > maxScoreAndMoves.get(0).get(0).get(0) || scoreAndMoves.get(0).get(0).get(0) == maxScoreAndMoves.get(0).get(0).get(0) && Math.abs(3 - moves.get(i).get(1).get(1)) < Math.abs(3 - moves.get(bestMove).get(1).get(1))) {
                        maxScoreAndMoves = scoreAndMoves;
                        bestMove = i;
                    }
                }

            }

            maxScoreAndMoves.add(1, moves.get(bestMove));

            return maxScoreAndMoves;
        }

    }

}

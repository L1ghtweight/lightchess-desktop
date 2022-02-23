package lightweight.lightchess.logic;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Logic {
    public static String prevFrom = new String(), prevTo = new String();
    public static List<Move> getLegalMovesFromSquare(Board b, String pos) {
        List<Move> allLegalMoves = b.legalMoves();
        List<Move> legalFromSquare = new ArrayList<>();
        for(Move move : allLegalMoves) {
            if(move.getFrom().toString().toLowerCase(Locale.ROOT).equals(pos)) {
                legalFromSquare.add(move);
            }
        }
        return legalFromSquare;
    }

    public static boolean makeMove(Board board, Move move) {

        if(isLegal(board, move)) {
            board.doMove(move);
            prevFrom = move.getFrom().toString().toLowerCase();
            prevTo = move.getTo().toString().toLowerCase();
            return true;
        }
        return false;
    }

    public static boolean makeMove(Board board, String move) {
        if(isLegal(board, move)) {
            board.doMove(move);
            prevFrom = move.substring(0,2);
            prevTo = move.substring(2,4);
            return true;
        }
        return false;
    }

    public static boolean isLegal(Board b, Move m) {
        List<Move> legalMoves = b.legalMoves();
        for(Move move : legalMoves) {
            if(move.toString().equals(m.toString())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isLegal(Board b, String m) {
        List<Move> legalMoves = b.legalMoves();
        for(Move move : legalMoves) {
            if(move.toString().equals(m)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCheckmate(Board board) {
        return board.isMated();
    }

    public static boolean isCheck(Board board) {
        return board.isKingAttacked();
    }

//    public static Side getSideToMove(Board board) {
//        return board.getSideToMove();
//    }

    public static void main(String[] args) {
        Board b = new Board();
        makeMove(b, "d1d4");
        System.out.println(b.toString());
    }
}

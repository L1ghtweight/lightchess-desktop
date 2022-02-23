package lightweight.lightchess.logic;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Logic {
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

    public static boolean isLegal(Board b, Move m) {
        List<Move> legalMoves = b.legalMoves();
        for(Move move : legalMoves) {
            if(move.toString().equals(m.toString())) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Board b = new Board();
        String FEN = "rnbqkbnr/1ppppQp1/7p/p7/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 4";
        b.loadFromFen(FEN);
        System.out.println(b.toString());
    }
}

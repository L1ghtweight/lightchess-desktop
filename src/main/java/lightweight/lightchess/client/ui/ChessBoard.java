package lightweight.lightchess.client.ui;

import com.github.bhlangonijr.chesslib.Board;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;


public class ChessBoard extends GridPane {
    private int length;
    private Color color1, color2;
    private Board gameBoard;
    Character[] pieceNotations = new Character[] {'p', 'r', 'n', 'b', 'k', 'q', 'P', 'R', 'N', 'B', 'K', 'Q'};
    HashMap<Character, Image> pieceMap = new HashMap<>();
    public boolean isBlack = false;

    public ChessBoard(int length, Color color1, Color color2, Board gameBoard) {
        this.length = length;
        this.color1 = color1;
        this.color2 = color2;
        this.gameBoard = gameBoard;

        for(Character c:pieceNotations) {
            String path = "/lightweight/lightchess/png_pieces/" + c + ".png";
            System.out.println(path + " " + getClass().getResourceAsStream(path));
            pieceMap.put(c, new Image(getClass().getResourceAsStream("/lightweight/lightchess/png_pieces/bN.png")));
        }

        updateBoard(gameBoard);
    }

    public void updateBoard(Board gameboard) {
        this.gameBoard = gameboard;
        String boardString = gameboard.toString();
        if(isBlack)
            boardString = gameboard.toStringFromBlackViewPoint();

        this.getChildren().clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle r = new Rectangle(0, 0, length / 8, length / 8);
                r.setFill((i + j) % 2 == 0 ? color1 : color2);
                this.add(r, i, j, 1, 1);
            }
        }

        for (int i = 0; i < 72; i++) {
            int x = i%9, y = i/9;
            if (boardString.charAt(i) != '.' && boardString.charAt(i) != '\n') {
                ImageView newPiece = new ImageView(pieceMap.get(boardString.charAt(i)));
                newPiece.setFitHeight(length/8);
                newPiece.setFitWidth(length/8);
                this.add(newPiece, x, y, 1, 1);
            }
        }
    }

    public void rotate() {
        isBlack = true;
    }
}

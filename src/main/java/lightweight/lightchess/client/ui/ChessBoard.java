package lightweight.lightchess.client.ui;

import com.github.bhlangonijr.chesslib.Board;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Character.*;
import static java.lang.Math.round;


public class ChessBoard extends Group {
    private int length;
    private Color color1, color2;
    private Board gameBoard;
    Character[] pieceNotations = new Character[] {'p', 'r', 'n', 'b', 'k', 'q', 'P', 'R', 'N', 'B', 'K', 'Q'};
    GridPane Grid = new GridPane();
    ArrayList<Piece> Pieces = new ArrayList<Piece>();
    HashMap<Character, Image> pieceMap = new HashMap<>();
    public boolean isBlack = false;

    public ChessBoard(int length, Color color1, Color color2, Board gameBoard) {
        super();
        this.length = length;
        this.color1 = color1;
        this.color2 = color2;
        this.gameBoard = gameBoard;

        for(Character c:pieceNotations) {
            String fileName;
            fileName = (isUpperCase(c) ? "w" + toLowerCase(c) : "b" + toLowerCase(c));

            String path = "/lightweight/lightchess/png_pieces/" + fileName + ".png";
            pieceMap.put(c, new Image(getClass().getResourceAsStream(path)));
        }


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle r = new Rectangle(0, 0, length / 8, length / 8);
                r.setFill((i + j) % 2 == 0 ? color1 : color2);
                Grid.add(r, i, j, 1, 1);
            }
        }

        this.getChildren().add(Grid);
        updateBoard(gameBoard);
    }

    public void updateBoard(Board gameboard) {

        this.gameBoard = gameboard;
        String boardString = gameboard.toString();
        if(isBlack)
            boardString = gameboard.toStringFromBlackViewPoint();

        for (int i = 0; i < 72; i++) {
            int x = i%9, y = i/9;
            if (boardString.charAt(i) != '.' && boardString.charAt(i) != '\n') {
                Piece newPiece = new Piece(pieceMap.get(boardString.charAt(i)), length, x, y);
                Pieces.add(newPiece);
                this.getChildren().add(newPiece);
                if(isBlack && isLowerCase(boardString.charAt(i))) {
                    newPiece.setOnMousePressed(event -> pressed(event, newPiece));
                    newPiece.setOnMouseDragged(event -> dragged(event, newPiece));
                    newPiece.setOnMouseReleased(event -> released(event, newPiece));
                }
                else if(!isBlack && isUpperCase(boardString.charAt(i))) {
                    newPiece.setOnMousePressed(event -> pressed(event, newPiece));
                    newPiece.setOnMouseDragged(event -> dragged(event, newPiece));
                    newPiece.setOnMouseReleased(event -> released(event, newPiece));
                }
            }
        }

    }

    public void rotate() {
        isBlack = true;
    }

    public void pressed(MouseEvent event, Piece p) {
        System.out.println("Pressed");;
    }

    public void dragged(MouseEvent event, Piece p) {
        p.setX(event.getX() - p.getFitWidth()/2);
        p.setY(event.getY() - p.getFitHeight()/2);
    }

    public void released(MouseEvent event, Piece p) {
        double nx = p.getX(), ny = p.getY();
        int snapX = (int) ((length/8) * (round(nx/(length/8)))), snapY = (int) ((length/8) * (round(ny/(length/8))));
        p.posX = (int) round(nx/(length/8));
        p.posY = (int) round(ny/(length/8));
        p.setX(snapX);
        p.setY(snapY);
    }
}

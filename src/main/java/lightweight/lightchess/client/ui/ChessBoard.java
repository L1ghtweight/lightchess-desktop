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
import lightweight.lightchess.client.net.ClientNet;
import lightweight.lightchess.logic.Logic;
import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Character.*;
import static java.lang.Math.log;
import static java.lang.Math.round;


public class ChessBoard extends Group {
    public ClientNet clientnet;
    private int length;
    private Color color1, color2;
    public Board gameBoard;
    private Logic logic;
    Character[] pieceNotations = new Character[] {'p', 'r', 'n', 'b', 'k', 'q', 'P', 'R', 'N', 'B', 'K', 'Q'};
    GridPane Grid = new GridPane();
    ArrayList<Piece> Pieces = new ArrayList<Piece>();
    HashMap<Character, Image> pieceMap = new HashMap<>();
    public boolean isBlack = false;

    public ChessBoard(int length, Color color1, Color color2, Board gameBoard, Logic logic) {
        super();
        this.length = length;
        this.color1 = color1;
        this.color2 = color2;
        this.gameBoard = gameBoard;
        this.logic = logic;

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

        for(Piece p:Pieces) {
            this.getChildren().remove(p);
        }

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
        updateBoard(gameBoard);
    }

    public String moveFromPos(int oldX, int oldY, int newX, int newY) {
        if(!isBlack) {
            oldY = (8 - oldY - 1);
            newY = (8 - newY - 1);
        }
        else {
            oldX = (8 - oldX - 1);
            newX = (8 - newX - 1);
        }

        char m1 = (char) (oldX + 'a');
        char m2 = (char) (oldY + '1');
        char m3 = (char) (newX + 'a');
        char m4 = (char) (newY + '1');

        String move = "";
        move += m1; move += m2; move += m3; move += m4;
        return move;
    }

    public void pressed(MouseEvent event, Piece p) {
    }

    public void dragged(MouseEvent event, Piece p) {
        p.setX(event.getX() - p.getFitWidth()/2);
        p.setY(event.getY() - p.getFitHeight()/2);
    }

    public void released(MouseEvent event, Piece p) {
        double nx = p.getX(), ny = p.getY();
        int snapX = (int) ((length/8) * (round(nx/(length/8)))), snapY = (int) ((length/8) * (round(ny/(length/8))));
        int newX = (int) round(nx/(length/8));
        int newY = (int) round(ny/(length/8));
        String move = moveFromPos(p.posX, p.posY, newX, newY);


        System.out.println(move);
        if(logic.isLegal(gameBoard, move)) {
            p.posX = newX; p.posY = newY;
            this.gameBoard.doMove(move);
            updateBoard(this.gameBoard);
            if(logic.isCheckmate(gameBoard)){
                System.out.println("You win");
                clientnet.sendMsg("You Lose");
            }
            clientnet.sendGameBoard(this.gameBoard);
            String boardString = gameBoard.toString();
        }
        else {
            p.setX(p.posX * length/8);
            p.setY(p.posY * length/8);
        }
    }
}

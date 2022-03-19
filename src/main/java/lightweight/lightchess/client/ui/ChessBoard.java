package lightweight.lightchess.client.ui;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import lightweight.lightchess.client.net.ClientNet;
import lightweight.lightchess.logic.Logic;
import lightweight.lightchess.net.CommandTypes;
import lightweight.lightchess.net.Data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    ArrayList<Circle> highlightedSquares = new ArrayList<Circle>();
    HashMap<Character, Image> pieceMap = new HashMap<>();
    Boolean selected = false;
    Rectangle selectedSquare = new Rectangle(0, 0, length/8, length/8);
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

        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Boolean highlighted = false;
                Boolean squareContainsPiece = false;
                int newX = getCoordinate(event.getX()), newY = getCoordinate(event.getY());
                removeMoveHighlights();
                for(Piece p: Pieces) {
                    if(newX == p.posX && newY == p.posY && p.isSelected == true) {
                        p.isSelected = false;
                        break;
                    }
                    else if(newX == p.posX && newY == p.posY && p.isSelected == false) {
                        p.isSelected = true;
                        addMoveHighlights(p);
                        break;
                    }
                }
            }
        });
    }


    public void removeMoveHighlights() {
        for(Circle c: highlightedSquares) {
            this.getChildren().remove(c);
        }
        highlightedSquares.clear();
    }

    public void addMoveHighlights(Piece p) {
        List<Move> possibleMoves = logic.getLegalMovesFromSquare(gameBoard, getSquare(p.posX, p.posY));
        for(Move m: possibleMoves) {
            String move = m.toString();
            int nx = coordFromMoveX(move), ny = coordFromMoveY(move);
            Circle c = new Circle(nx * length/8 + length/16, ny * length/ 8 + length/16, length/48);
            c.setFill(new Color(0.5, 1, 0.5, 0.5));
            highlightedSquares.add(c);
            this.getChildren().add(c);
        }
    }

    public int getCoordinate(double X) {
        return (int) (X/(length/8));
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

    public String getSquare(int x, int y) {
        int nx = x, ny = y;

        if(!isBlack)
            ny = (8 - y - 1);
        else
            nx = (8 - x - 1);

        char m1 = (char) (nx + 'a');
        char m2 = (char) (ny + '1');

        String square = "";
        square += m1; square += m2;
        return square;
    }

    public int coordFromMoveX(String move) {
        int x = move.charAt(2) - 'a';
        if(isBlack)
            x = (8 - x - 1);
        return x;
    }

    public int coordFromMoveY(String move) {
        int y =  move.charAt(3) - '1';
        if(!isBlack)
            y = (8 - y - 1);
        return y;
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
        for(Circle c: highlightedSquares) {
            this.getChildren().remove(c);
            highlightedSquares.remove(c);
        }
    }

    public void dragged(MouseEvent event, Piece p) {
        p.setX(event.getX() - p.getFitWidth()/2);
        p.setY(event.getY() - p.getFitHeight()/2);
    }

    public void movePiece(Board gameBoard, String move, Piece p, int newX, int newY) {
        if(logic.isLegal(gameBoard, move)) {
            p.posX = newX; p.posY = newY;
            this.gameBoard.doMove(move);
            updateBoard(this.gameBoard);
            if(logic.isCheckmate(gameBoard)){
                System.out.println("You win");
                clientnet.sendMsg("You Lose");
            }
            clientnet.sendGameBoard(this.gameBoard);
        }
        else {
            p.setX(p.posX * length/8);
            p.setY(p.posY * length/8);
        }
    }

    public void released(MouseEvent event, Piece p) {
        int newX = getCoordinate(event.getX()), newY = getCoordinate(event.getY());
        String move = moveFromPos(p.posX, p.posY, newX, newY);
        movePiece(gameBoard, move, p, newX, newY);
    }
}

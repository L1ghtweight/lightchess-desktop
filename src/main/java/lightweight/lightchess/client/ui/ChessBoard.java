package lightweight.lightchess.client.ui;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Side;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lightweight.lightchess.client.net.ClientNet;
import lightweight.lightchess.logic.Logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Character.*;
import static java.lang.Math.round;


public class ChessBoard extends Group {
    public ClientNet clientnet;
    public int length;
    private Color color1, color2;
    public Board gameBoard;
    private Logic logic;
    Character[] pieceNotations = new Character[] {'p', 'r', 'n', 'b', 'k', 'q', 'P', 'R', 'N', 'B', 'K', 'Q'};
    GridPane Grid = new GridPane();
    ArrayList<Piece> Pieces = new ArrayList<Piece>();
    ArrayList<String> moveList = new ArrayList<>();
    HashMap<Character, Image> pieceMap = new HashMap<>();
    public boolean isBlack = false;
    public Clock playerClock, opponentClock;
    public boolean inTournament = false;

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

    public void updateClocks() throws IOException {
        if( (gameBoard.getSideToMove() == Side.BLACK && isBlack) || (gameBoard.getSideToMove() == Side.WHITE && !isBlack))
        {
            playerClock.isMyMove = true;
            opponentClock.isMyMove = false;
            playerClock.tick();
        }
        else
        {
            playerClock.isMyMove = false;
            opponentClock.isMyMove = true;
            opponentClock.tick();
        }
    }

    public void setClocks(String timeString) {
        System.out.println("Time String: " + timeString);
        this.playerClock = new Clock(timeString);
        this.opponentClock = new Clock(timeString);
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
                if(isBlack)
                    newPiece.color = false;
                Pieces.add(newPiece);
                this.getChildren().add(newPiece);
                if(isBlack && isLowerCase(boardString.charAt(i))) {
                    newPiece.setOnMouseDragged(event -> dragged(event, newPiece));
                    newPiece.setOnMouseReleased(event -> released(event, newPiece));
                }
                else if(!isBlack && isUpperCase(boardString.charAt(i))) {
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

    public void dragged(MouseEvent event, Piece p) {
        p.setX(event.getX() - p.getFitWidth()/2);
        p.setY(event.getY() - p.getFitHeight()/2);
    }

    public int getEloChange(int factor, Double ra, Double rb) {
        Double diff = (ra - rb);
        Double pwr  = 1 + Math.pow(10, diff / 400);
        Double exp_sc = 1 / (1 + pwr);
        Double newElo = Double.parseDouble(clientnet.userInfo.get("elo")) + 40 * (factor - exp_sc);
        return (int) round(newElo);
    }

    public void gameLost()
    {
        clientnet.fetchScoreBoard();
        if(inTournament) {
            try {
                clientnet.main.changeTournamentGameStatus("Not Ready");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("You Lose");
        clientnet.main.showDialog("Defeat");
        clientnet.updateELO(getEloChange(-1, Double.parseDouble(clientnet.userInfo.get("elo")), Double.parseDouble(clientnet.requested_userInfo.get("elo"))));
        clientnet.endMatch("0");
        try {
            clientnet.main.changeTournamentGameStatus("Not Reaady");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gameWon()
    {
        clientnet.fetchScoreBoard();
        if(inTournament) {
            try {
                clientnet.main.changeTournamentGameStatus("Not Ready");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("You win");
        clientnet.main.showDialog("Victory");
        System.out.println("New elo: " + getEloChange(+1, Double.parseDouble(clientnet.userInfo.get("elo")), Double.parseDouble(clientnet.requested_userInfo.get("elo"))));
        clientnet.updateELO(getEloChange(+1, Double.parseDouble(clientnet.userInfo.get("elo")), Double.parseDouble(clientnet.requested_userInfo.get("elo"))));
        clientnet.endMatch("2");
        try {
            clientnet.main.changeTournamentGameStatus("Not Reaady");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleOpponnentsMove(String gameboard_fen, String move){
        Board board_new = new Board();
        board_new.loadFromFen(gameboard_fen);
        moveList.add(move);
        updateBoard(board_new);
        System.out.println(move);

        if(logic.isCheckmate(gameBoard)){
            gameLost();
        }
    }

    public void movePiece(Board gameBoard, String move, Piece p, int newX, int newY) {

        if(logic.isLegal(gameBoard, move)) {
            moveList.add(move);
            System.out.println(move);
            p.posX = newX; p.posY = newY;
            this.gameBoard.doMove(move);
            updateBoard(this.gameBoard);
            clientnet.sendGameBoard(this.gameBoard.getFen(), move);
            if(logic.isCheckmate(gameBoard)){
                gameWon();
            }
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

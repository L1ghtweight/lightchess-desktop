package lightweight.lightchess.client.ui;

import com.github.bhlangonijr.chesslib.Board;
import de.codecentric.centerdevice.javafxsvg.SvgDescriptor;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoader;
import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import lightweight.lightchess.logic.Logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;


public class UIBoard extends GridPane {
    private int length;
    private Color color1, color2;
    private Board gameboard;
    HashMap<Character, Integer> pieceMap = new HashMap<>();
    ImageView[][] boardImages = new ImageView[8][8];
    FileInputStream[] pieceInputs = new FileInputStream[12];
    Image[] pieceImages = new Image[12];
    Image transparentImage = null;

    public UIBoard(int length, Color color1, Color color2, Board gameboard) {
        this.length = length;
        this.color1 = color1;
        this.color2 = color2;
        this.gameboard = gameboard;

        FileInputStream transparentInput = null;

        try {
            pieceInputs[0] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/bN.png");
            pieceInputs[1] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/bB.png");
            pieceInputs[2] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/bP.png");
            pieceInputs[3] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/bK.png");
            pieceInputs[4] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/bQ.png");
            pieceInputs[5] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/bR.png");
            pieceInputs[6] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/wN.png");
            pieceInputs[7] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/wB.png");
            pieceInputs[8] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/wP.png");
            pieceInputs[9] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/wK.png");
            pieceInputs[10] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/wQ.png");
            pieceInputs[11] = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/wR.png");
            transparentInput = new FileInputStream("src/main/resources/lightweight/lightchess/png_pieces/transparent.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        pieceMap.put('n', 0);
        pieceMap.put('b', 1);
        pieceMap.put('p', 2);
        pieceMap.put('k', 3);
        pieceMap.put('q', 4);
        pieceMap.put('r', 5);
        pieceMap.put('N', 6);
        pieceMap.put('B', 7);
        pieceMap.put('P', 8);
        pieceMap.put('K', 9);
        pieceMap.put('Q', 10);
        pieceMap.put('R', 11);


        for (int i = 0; i < 12; i++)
            pieceImages[i] = new Image(pieceInputs[i]);

        transparentImage = new Image(transparentInput);

        updateBoard(gameboard);
    }

    public void updateBoard(Board gameboard) {
        this.gameboard = gameboard;
        String boardString = gameboard.toString();

        this.getChildren().clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle r = new Rectangle(0, 0, length / 8, length / 8);
                r.setFill((i + j) % 2 == 0 ? color1 : color2);
                this.add(r, i, j, 1, 1);
            }
        }

        for (int i = 0; i < 72; i++) {
            if (boardString.charAt(i) != '.' && boardString.charAt(i) != '\n') {
                boardImages[i/9][i%9] = new ImageView(pieceImages[pieceMap.get(boardString.charAt(i))]);
                boardImages[i/9][i%9].setFitHeight(length/8);
                boardImages[i/9][i%9].setFitWidth(length/8);
                this.add(boardImages[i/9][i%9], i%9, i/9, 1, 1);
            }
            else if(i/9 < 8 && i%9 < 8) {
                System.out.println(boardString);
                this.getChildren().remove(boardImages[i/9][i%9]);
                boardImages[i/9][i%9] = new ImageView(transparentImage);
                boardImages[i/9][i%9].setFitHeight(length/8);
                boardImages[i/9][i%9].setFitWidth(length/8);
                this.add(boardImages[i/9][i%9], i%9, i/9, 1, 1);
            }
        }
    }
}

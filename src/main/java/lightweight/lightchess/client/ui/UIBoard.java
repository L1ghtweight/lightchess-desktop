package lightweight.lightchess.client.ui;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UIBoard extends GridPane {
    private int length;
    private Color color1, color2;

    public UIBoard(int length, Color color1, Color color2) {
        this.length = length;
        this.color1 = color1;
        this.color2 = color2;

        ClassLoader classLoader = getClass().getClassLoader();
        FileInputStream input = null;
        try {
            input = new FileInputStream("/home/ssshanto/Projects/lightchess/src/main/resources/lightweight/lightchess/knight.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Image image = new Image(input);
        ImageView knight = new ImageView(image);

        knight.setFitHeight(length/8 * 0.8);
        knight.setFitWidth(length/8 * 0.8);

        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                Rectangle r = new Rectangle(0, 0, length/8, length/8);
                r.setFill((i+j) % 2 == 0 ? color1 : color2);
                this.add(r, i, j, 1, 1);
            }
        }

        this.add(knight, 0, 0, 1, 1);
    }
}

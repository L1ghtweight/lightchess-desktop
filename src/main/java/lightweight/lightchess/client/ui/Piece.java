package lightweight.lightchess.client.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static java.lang.Math.round;

public class Piece extends ImageView {
    public int posX;
    public int posY;
    public Boolean isSelected = false;

    public Piece(Image image, double boardLength, int x, int y) {
        super(image);
        posX = x;
        posY = y;
        this.setX(x * boardLength/8);
        this.setY(y * boardLength/8);
        this.setFitHeight(boardLength/8);
        this.setFitWidth(boardLength/8);
    }
}

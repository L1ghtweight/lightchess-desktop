package lightweight.lightchess.client.ui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import static java.lang.Math.round;

public class Piece extends ImageView {
    private int posX;
    private int posY;

    public Piece(Image image, double boardLength, int x, int y) {
        super(image);
        posX = x;
        posY = y;
        this.setX(x * boardLength/8);
        this.setY(y * boardLength/8);
        this.setFitHeight(boardLength/8);
        this.setFitWidth(boardLength/8);

        setOnMousePressed(event -> {

        });

        setOnMouseDragged(event -> {
            this.setX(event.getX() - this.getFitWidth()/2);
            this.setY(event.getY() - this.getFitHeight()/2);
        });

        setOnMouseReleased(event -> {
            double nx = this.getX(), ny = this.getY();
            int snapX = (int) ((boardLength/8) * (round(nx/(boardLength/8)))), snapY = (int) ((boardLength/8) * (round(ny/(boardLength/8))));
            this.posX = (int) round(nx/(boardLength/8));
            this.posY = (int) round(ny/(boardLength/8));
            this.setX(snapX);
            this.setY(snapY);
        });
    }
}

package me.lordbaljeet.projectchess.module;

import javafx.scene.layout.Pane;
import me.lordbaljeet.projectchess.module.Pieces.Piece;

public class Square extends Pane {
    private Piece piece;
    private Position position;

    boolean isGreen;

    boolean highlighted = false;

    public Square(boolean isGreen, Piece piece, Position position) {
        this.piece = piece;
        this.position = position;
        this.setStyle("-fx-background-color: #ebecd0");
        if(isGreen) {
            this.setStyle("-fx-background-color: #779556");
        }
        this.getChildren().add(0,piece);
    }

    public Square(boolean isGreen, Position position) {
        this.isGreen = isGreen;
        this.position = position;
        this.setStyle("-fx-background-color: #ebecd0");
        if(isGreen) {
            this.setStyle("-fx-background-color: #779556");
        }
        this.setOnMousePressed(e-> {
            if(e.isSecondaryButtonDown()) {
                if (highlighted) {
                    unhighlight();
                } else {
                    highlight();
                }
            }
        });
    }

    public void highlight() {

        this.setStyle("-fx-background-color: #f78863");
        if(isGreen) {
            this.setStyle("-fx-background-color: #ba6549");
        }
        highlighted = true;
    }

    public void unhighlight() {
        this.setStyle("-fx-background-color: #ebecd0");
        if(isGreen) {
            this.setStyle("-fx-background-color: #779556");
        }
        highlighted = false;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
        if(piece != null) {
            this.getChildren().add(piece);
        }
    }

    public Position getPosition() {
        return position;
    }

    public boolean isFree() {
        return this.piece == null;
    }

    public boolean isGreen() {
        return isGreen;
    }

    public void clear() {
        getChildren().clear();
        piece = null;
    }

    @Override
    public String toString() {
        return "Square" + position;
    }
}

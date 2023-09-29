package me.lordbaljeet.projectchess.module.Pieces;

import javafx.scene.image.Image;
import me.lordbaljeet.projectchess.module.PlayerColor;
import me.lordbaljeet.projectchess.module.Square;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece{

    public Rook(PlayerColor color, double cellDimensions) {
        super(color, cellDimensions);
        super.setPieceImage(getRookImage(color));
    }

    @Override
    public List<Square> possibleMoves(Square square) {
        List<Square> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(possibleVerticalHorizontalMoves(square));
        return possibleMoves;
    }

    private Image getRookImage(PlayerColor color) {

        Image pawn;

        if(color.equals(PlayerColor.BLACK)) {
            pawn = new Image("/black_rook.png");
        }else {
            pawn = new Image("/white_rook.png");
        }

        return pawn;
    }

    @Override
    public String toString() {
        return super.toString()+" Rook";
    }
}

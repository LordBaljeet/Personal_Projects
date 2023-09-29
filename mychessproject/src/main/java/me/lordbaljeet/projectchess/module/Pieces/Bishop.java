package me.lordbaljeet.projectchess.module.Pieces;

import javafx.scene.image.Image;
import me.lordbaljeet.projectchess.module.PlayerColor;
import me.lordbaljeet.projectchess.module.Square;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece{

    public Bishop(PlayerColor color, double cellDimensions) {
        super(color, cellDimensions);
        super.setPieceImage(getBishopImage(color));
    }

    @Override
    public List<Square> possibleMoves(Square square) {
        List<Square> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(possibleDiagonalMoves(square));
        return possibleMoves;
    }

    private Image getBishopImage(PlayerColor color) {

        Image pawn;

        if(color.equals(PlayerColor.BLACK)) {
            pawn = new Image("/black_bishop.png");
        }else {
            pawn = new Image("/white_bishop.png");
        }

        return pawn;
    }

    @Override
    public String toString() {
        return super.toString() + " Bishop";
    }
}

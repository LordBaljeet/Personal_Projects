package me.lordbaljeet.projectchess.module.Pieces;

import javafx.scene.image.Image;
import me.lordbaljeet.projectchess.module.PlayerColor;
import me.lordbaljeet.projectchess.module.Square;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece{

    public Queen(PlayerColor color, double cellDimensions) {
        super(color, cellDimensions);
        super.setPieceImage(getQueenImage(color));
    }

    @Override
    public List<Square> possibleMoves(Square square) {
        List<Square> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(possibleDiagonalMoves(square));
        possibleMoves.addAll(possibleVerticalHorizontalMoves(square));
        return possibleMoves;
    }

    private Image getQueenImage(PlayerColor color) {

        Image pawn;

        if(color.equals(PlayerColor.BLACK)) {
            pawn = new Image("/black_queen.png");
        }else {
            pawn = new Image("/white_queen.png");
        }

        return pawn;
    }

    @Override
    public String toString() {
        return super.toString() + " Queen";
    }
}

package me.lordbaljeet.projectchess.module.Pieces;

import javafx.scene.image.Image;
import me.lordbaljeet.projectchess.module.*;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece{

    public King(PlayerColor color, double cellDimensions) {
        super(color,cellDimensions);
        super.setPieceImage(getKingImage(color));
    }

    @Override
    public List<Square> possibleMoves(Square start) {
        List<Square> possibleMoves = new ArrayList<>();

        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.N));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.W));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.S));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.E));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.NE));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.NW));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.SE));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.SW));

        return possibleMoves;
    }

    private List<Square> getPossibleSquareDestination(Position pos, Direction dir) {
        List<Square> possibleMoves = new ArrayList<>();
        Board board = (Board) getParent().getParent();
        PlayerColor color = getColor();

        Position position = pos.next(dir);
        if(board.contains(position)) {
            Square square = board.getSquareAt(position);
            if(square.isFree() || board.containsOppositeColor(square, color)) {
                possibleMoves.add(square);
            }
        }
        return possibleMoves;
    }

    private Image getKingImage(PlayerColor color) {

        Image pawn;

        if(color.equals(PlayerColor.BLACK)) {
            pawn = new Image("/black_king.png");
        }else {
            pawn = new Image("/white_king.png");
        }

        return pawn;
    }

    @Override
    public String toString() {
        return super.toString() + " King";
    }
}

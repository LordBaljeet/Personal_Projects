package me.lordbaljeet.projectchess.module.Pieces;

import com.almasb.fxgl.core.Disposable;
import javafx.scene.image.Image;
import me.lordbaljeet.projectchess.module.*;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece{

    public Knight(PlayerColor color, double cellDimensions) {
        super(color, cellDimensions);
        super.setPieceImage(getKnightImage(color));
    }

    @Override
    public List<Square> possibleMoves(Square start) {
        List<Square> possibleMoves = new ArrayList<>();

        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.N, Direction.NE));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.E, Direction.NE));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.N, Direction.NW));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.W, Direction.NW));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.S, Direction.SE));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.E, Direction.SE));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.S, Direction.SW));
        possibleMoves.addAll(getPossibleSquareDestination(start.getPosition(), Direction.W, Direction.SW));

        return possibleMoves;
    }

    private List<Square> getPossibleSquareDestination(Position pos, Direction dir1, Direction dir2) {
        List<Square> possibleMoves = new ArrayList<>();
        Board board = (Board) getParent().getParent();
        PlayerColor color = getColor();

        Position position = pos.next(dir1).next(dir2);
        if(board.contains(position)) {
            Square square = board.getSquareAt(position);
            if(square.isFree() || board.containsOppositeColor(square, color)) {
                 possibleMoves.add(square);
            }
        }
        return possibleMoves;
    }

    private Image getKnightImage(PlayerColor color) {

        Image pawn;

        if(color.equals(PlayerColor.BLACK)) {
            pawn = new Image("/black_knight.png");
        }else {
            pawn = new Image("/white_knight.png");
        }

        return pawn;
    }

    @Override
    public String toString() {
        return super.toString()+ " Knight";
    }
}

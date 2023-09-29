package me.lordbaljeet.projectchess.module.Pieces;

import javafx.scene.image.Image;
import me.lordbaljeet.projectchess.module.*;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece{

    public Pawn(PlayerColor color, double cellDimensions) {

        super(color, cellDimensions);
        super.setPieceImage(getPawnImage(color));

    }

    @Override
    public List<Square> possibleMoves(Square startSquare) {

        List<Square> possibleMoves = new ArrayList<>();
        Board board = (Board) startSquare.getParent();
        PlayerColor color = getColor();

        //WHITE PAWNS POSSIBLE MOVES DIRECTION
        Position n_Square = startSquare.getPosition().next(Direction.N);
        Position nn_Square = n_Square.next(Direction.N);
        Position ne_Square = startSquare.getPosition().next(Direction.NE);
        Position nw_Square = startSquare.getPosition().next(Direction.NW);

        //BLACK PAWNS POSSIBLE MOVES DIRECTION
        Position s_Square = startSquare.getPosition().next(Direction.S);
        Position ss_Square = s_Square.next(Direction.S);
        Position se_Square = startSquare.getPosition().next(Direction.SE);
        Position sw_Square = startSquare.getPosition().next(Direction.SW);

        //Positions for enPassant
        Position e_Square = startSquare.getPosition().next(Direction.E);
        Position w_Square = startSquare.getPosition().next(Direction.W);

        if(color.equals(PlayerColor.WHITE))
        {
            if(board.contains(n_Square))
            {
                Square n_square = board.getSquareAt(n_Square);

                if(n_square.isFree())
                {
                    possibleMoves.add(n_square);

                    if(board.getInitialPawnRow(color) == startSquare.getPosition().getY())
                    {
                        Square nn_square = board.getSquareAt(nn_Square);
                        if(nn_square.isFree())
                        {
                            possibleMoves.add(nn_square);
                        }
                    }
                }
            }
            if(board.contains(ne_Square))
            {
                Square ne_square = board.getSquareAt(ne_Square);

                if(board.containsOppositeColor(ne_square, color))
                {
                    possibleMoves.add(ne_square);
                }

            }
            if(board.contains(nw_Square))
            {
                Square nw_square = board.getSquareAt(nw_Square);

                if(board.containsOppositeColor(nw_square, color)){
                    possibleMoves.add(nw_square);
                }
            }
        }
        else
        {
            if(board.contains(s_Square))
            {
                Square s_square = board.getSquareAt(s_Square);

                if(s_square.isFree())
                {
                    possibleMoves.add(s_square);

                    if(board.getInitialPawnRow(color) == startSquare.getPosition().getY())
                    {
                        Square ss_square = board.getSquareAt(ss_Square);
                        if(ss_square.isFree())
                        {
                            possibleMoves.add(ss_square);
                        }
                    }
                }
            }
            if(board.contains(se_Square))
            {
                Square se_square = board.getSquareAt(se_Square);

                if(board.containsOppositeColor(se_square, color))
                {
                    possibleMoves.add(se_square);
                }
            }
            if(board.contains(sw_Square))
            {
                Square sw_square = board.getSquareAt(sw_Square);

                if(board.containsOppositeColor(sw_square, color)){
                    possibleMoves.add(sw_square);
                }
            }
        }
        return possibleMoves;
    }

    @Override
    public List<Square> getCapturePositions(Square start) {
        List<Square> captureSquares = new ArrayList<>();
        Board board = (Board) start.getParent();
        PlayerColor color = getColor();

        Position ne_Square = start.getPosition().next(Direction.NE);
        Position nw_Square = start.getPosition().next(Direction.NW);

        Position se_Square = start.getPosition().next(Direction.SE);
        Position sw_Square = start.getPosition().next(Direction.SW);

        if(board.contains(ne_Square))
        {
            Square ne_square = board.getSquareAt(ne_Square);

            if(board.containsOppositeColor(ne_square, color))
            {
                captureSquares.add(ne_square);
            }

        }
        if(board.contains(nw_Square))
        {
            Square nw_square = board.getSquareAt(nw_Square);

            if(board.containsOppositeColor(nw_square, color))
            {
                captureSquares.add(nw_square);
            }

        }
        if(board.contains(se_Square))
        {
            Square se_square = board.getSquareAt(se_Square);

            if(board.containsOppositeColor(se_square, color))
            {
                captureSquares.add(se_square);
            }

        }
        if(board.contains(sw_Square))
        {
            Square sw_square = board.getSquareAt(sw_Square);

            if(board.containsOppositeColor(sw_square, color))
            {
                captureSquares.add(sw_square);
            }

        }
        return captureSquares;
    }

    private Image getPawnImage(PlayerColor color) {

        Image pawn;

        if(color.equals(PlayerColor.BLACK)) {
            pawn = new Image("/black_pawn.png");
        }else {
            pawn = new Image("/white_pawn.png");
        }

        return pawn;
    }

    @Override
    public String toString() {
        return super.toString() + " Pawn";
    }
}

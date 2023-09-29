package me.lordbaljeet.projectchess.module.Pieces;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import me.lordbaljeet.projectchess.Config;
import me.lordbaljeet.projectchess.module.*;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece extends ImageView {

    private Image img;
    private PlayerColor color;
    public Square initialSquare;
    private double mouseOffsetFromNodeZeroX;
    private double mouseOffsetFromNodeZeroY;
    private Board board;
    public List<Square> validMoves = new ArrayList<>();

    public Piece(PlayerColor color, double squareDimension) {
        this.color = color;
        setPieceImageDimensions(squareDimension);
    }

    private void setPieceImageDimensions(double squareDimension) {
        this.setFitHeight(squareDimension);
        this.setFitWidth(squareDimension);
        this.setPreserveRatio(true);
        this.setSmooth(true);
    }

    public void setBoard(Board board) {
        this.board = board;
        this.initialSquare = board.getPieceSquare(this);
    }

    public void setPieceImage(Image img) {
        this.img = img;
        this.setImage(img);
    }

    public PlayerColor getColor() {
        return color;
    }

    public Square getInitialSquare() {
        return initialSquare;
    }

    public abstract List<Square> possibleMoves(Square square);

    public List<Square> getCapturePositions(Square square) {
        return possibleMoves(square);
    }

    List<Square> possibleDiagonalMoves(Square start) {
        List<Square> possibleMoves = new ArrayList<>();
        Board board = (Board) start.getParent();
        Square NE_Square = start;
        Square NW_Square = start;
        Square SE_Square = start;
        Square SW_Square = start;
        while(board.contains(NE_Square.getPosition().next(Direction.NE))) {
            NE_Square = board.getSquareAt(NE_Square.getPosition().next(Direction.NE));
            if(NE_Square.isFree() || board.containsOppositeColor(NE_Square, color)) {
                possibleMoves.add(NE_Square);
            }
            if(NE_Square.getPiece() != null) {
                break;
            }
        }while(board.contains(NW_Square.getPosition().next(Direction.NW))) {
            NW_Square = board.getSquareAt(NW_Square.getPosition().next(Direction.NW));
            if(NW_Square.isFree() || board.containsOppositeColor(NW_Square, color)) {
                possibleMoves.add(NW_Square);
            }
            if(NW_Square.getPiece() != null) {
                break;
            }
        }while(board.contains(SE_Square.getPosition().next(Direction.SE))) {
            SE_Square = board.getSquareAt(SE_Square.getPosition().next(Direction.SE));
            if(SE_Square.isFree() || board.containsOppositeColor(SE_Square, color)) {
                possibleMoves.add(SE_Square);
            }
            if(SE_Square.getPiece() != null) {
                break;
            }
        }while(board.contains(SW_Square.getPosition().next(Direction.SW))) {
            SW_Square = board.getSquareAt(SW_Square.getPosition().next(Direction.SW));
            if(SW_Square.isFree() || board.containsOppositeColor(SW_Square, color)) {
                possibleMoves.add(SW_Square);
            }
            if(SW_Square.getPiece() != null) {
                break;
            }
        }
        return possibleMoves;
    }
    List<Square> possibleVerticalHorizontalMoves(Square start) {
        List<Square> possibleMoves = new ArrayList<>();
        Board board = (Board) start.getParent();
        Square N_Square = start;
        Square S_Square = start;
        Square E_Square = start;
        Square W_Square = start;

        while(board.contains(N_Square.getPosition().next(Direction.N))) {
            N_Square = board.getSquareAt(N_Square.getPosition().next(Direction.N));
            if(N_Square.isFree() || board.containsOppositeColor(N_Square, color)) {
                possibleMoves.add(N_Square);
            }
            if(N_Square.getPiece() != null) {
                break;
            }
        }while(board.contains(W_Square.getPosition().next(Direction.W))) {
            W_Square = board.getSquareAt(W_Square.getPosition().next(Direction.W));
            if(W_Square.isFree() || board.containsOppositeColor(W_Square, color)) {
                possibleMoves.add(W_Square);
            }
            if(W_Square.getPiece() != null) {
                break;
            }
        }while(board.contains(E_Square.getPosition().next(Direction.E))) {
            E_Square = board.getSquareAt(E_Square.getPosition().next(Direction.E));
            if(E_Square.isFree() || board.containsOppositeColor(E_Square, color)) {
                possibleMoves.add(E_Square);
            }
            if(E_Square.getPiece() != null) {
                break;
            }
        }while(board.contains(S_Square.getPosition().next(Direction.S))) {
            S_Square = board.getSquareAt(S_Square.getPosition().next(Direction.S));
            if(S_Square.isFree() || board.containsOppositeColor(S_Square, color)) {
                possibleMoves.add(S_Square);
            }
            if(S_Square.getPiece() != null) {
                break;
            }
        }

        return possibleMoves;
    }

    @Override
    public String toString() {
        return color.equals(PlayerColor.BLACK) ? "Black": "White";
    }

    public void pressed(MouseEvent event, Player currentPlayer) {

        if(isTurn(currentPlayer)) {
            highlightPossibleMoves();
            mouseOffsetFromNodeZeroX = event.getSceneX() - this.getTranslateX();
            mouseOffsetFromNodeZeroY = event.getSceneY() - this.getTranslateY();
            initialSquare = (Square)getParent();
            initialSquare.toFront();
        }
        else event.consume();
    }

    public void dragged(MouseEvent event, Player currentPlayer) {
        if(isTurn(currentPlayer)) {
            this.setTranslateX(event.getSceneX() - mouseOffsetFromNodeZeroX );
            this.setTranslateY(event.getSceneY() - mouseOffsetFromNodeZeroY);
        }
        else event.consume();
    }

    public boolean move(Square start, Square destination) {

        List<Square> possibleMoves = possibleMoves(start);
        unhighlightPossibleMoves();

        if(destination != start && possibleMoves.contains(destination)) {
            destination.clear();
            if(this instanceof Pawn &&(destination.getPosition().getY() == 0 || destination.getPosition().getY() == 7)) {
                Queen queen = new Queen(this.color, Config.PANEL_SIZE);
                destination.setPiece(queen);
            }else {
                destination.setPiece(this);
            }
            initialSquare.clear();
            initialSquare = destination;
            this.setTranslateX(this.getX());
            this.setTranslateY(this.getY());
            return true;
        }
        this.setTranslateX(this.getX());
        this.setTranslateY(this.getY());
        return false;
    }

    private void highlightPossibleMoves() {
        for (Square s :
                validMoves){
                s.highlight();
        }
    }
    private void unhighlightPossibleMoves() {
        for (Square s :
                possibleMoves(this.getInitialSquare())) {
                s.unhighlight();
        }
    }

    private boolean isTurn(Player currentPlayer) {
        return currentPlayer.getColor() == color;
    }

}
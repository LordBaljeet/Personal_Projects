package me.lordbaljeet.projectchess.module;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import me.lordbaljeet.projectchess.Config;
import me.lordbaljeet.projectchess.module.Pieces.*;

import java.util.ArrayList;
import java.util.List;

public class Board extends GridPane{

    private final int panelSize = (int) Config.PANEL_SIZE;
    private final Square[][] squares;

    private final Game game;
    public Board(Game game) {
        this.game = game;
        squares = new Square[8][8];
        createBoard();
    }

    private void createBoard() {
        while(this.getColumnCount() < 8 && this.getRowCount() < 8) {
            this.getColumnConstraints().add(new ColumnConstraints(panelSize));
            this.getRowConstraints().add(new RowConstraints(panelSize));
        }
        boolean green = false;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Square square = new Square(green, new Position(x,y));
                squares[x][y] = square;
                green = !green;
                this.add(square, x, y);
            }
            green = !green;
        }
    }

    public int getPanelSize() {
        return panelSize;
    }

    public boolean contains(Position pos) {
        return (pos.getX() >= 0 && pos.getX() <= 7 && pos.getY() >= 0 && pos.getY() <= 7);
    }

    public void setPiece(Piece piece, Position position) {
        if (!contains(position)) {
            throw new IllegalArgumentException("The given position isn't part of the board :" + position);
        }
        squares[position.getX()][position.getY()].clear();
        squares[position.getX()][position.getY()].setPiece(piece);
    }

    public int getInitialPawnRow(PlayerColor color) {
        return color == PlayerColor.BLACK ? 1 : 6;
    }

    public boolean isFree(Square square) {
        if (!contains(square.getPosition())) {
            throw new IllegalArgumentException("The given position isn't part of the board :" + square.getPosition());
        }
        return square.isFree();
    }

    public Square getPieceSquare(Piece piece) {

        for (int row = 0; row < squares.length; row++) {
            for (int column = 0; column < squares[row].length; column++) {
                Position pos = new Position(row,column);
                Square s = getSquareAt(pos);
                if (!isFree(s) && s.getPiece().equals(piece)) {
                    return s;
                }
            }
        }
        return null;
    }

    public boolean containsOppositeColor(Square pos, PlayerColor col) {
        if (!contains(pos.getPosition())) {
            throw new IllegalArgumentException("The given position isn't part of the board :" + pos);
        }
        return (!pos.isFree() && (col.opposite() == pos.getPiece().getColor()));
    }

    public List<Square> getPositionsOccupiedBy(Player player) {
        List<Square> positionsOccupied = new ArrayList<>();
        for (int row = 0; row < squares.length; row++) {
            for (int column = 0; column < squares[row].length; column++) {
                Position pos = new Position(row, column);
                Square square = getSquareAt(pos);
                if (!isFree(square) && !containsOppositeColor(square, player.getColor())) {
                    positionsOccupied.add(square);
                }
            }
        }
        return positionsOccupied;

    }

    public void givePiecesBoard() {
        for (Square[] x:
        squares){
            for (Square s :
                    x) {
                if (s.getPiece() != null) {
                    s.getPiece().setBoard(this);
                }
            }
        }
    }



    public Square getSquareAt(Position pos) {
        return squares[pos.getX()][pos.getY()];
    }
}
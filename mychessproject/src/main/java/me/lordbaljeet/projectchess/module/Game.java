package me.lordbaljeet.projectchess.module;

import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import me.lordbaljeet.projectchess.Config;
import me.lordbaljeet.projectchess.module.Pieces.*;
import me.lordbaljeet.projectchess.view.UserMovesInterface;
import me.lordbaljeet.projectchess.view.UserTimeInterface;

import java.util.ArrayList;
import java.util.List;

public class Game extends AnchorPane {

    private final Board board;
    private final Player white;
    private final Player black;
    private Player winner = null;
    private Player currentPlayer;
    private King wk;
    private King bk;
    private Square checkedKingSquare;
    private GameState state;
    private UserTimeInterface UTI;
    private UserMovesInterface UMI;
    public Game() {
        this.board = new Board(this);
        this.white = new Player(PlayerColor.WHITE);
        this.black = new Player(PlayerColor.BLACK);
        this.UTI = new UserTimeInterface(this);
        this.UMI = new UserMovesInterface(this);
        this.getChildren().add(UTI);
//        this.getChildren().add(UMI);
        this.setOnMousePressed(this::piecePressedHandler);
        this.setOnMouseDragged(this::pieceDraggedHandler);
        this.setOnMouseReleased(this::pieceReleasedHandler);
    }

    private void piecePressedHandler(MouseEvent e) {
        if(winner != null) {
            e.consume();
            return;
        }
        if(e.getTarget() instanceof Piece piece && state != GameState.CHECKMATE && state != GameState.STALEMATE) {
            setPieceValidMoves(piece, piece.getInitialSquare());
            piece.pressed(e, currentPlayer);
            if(!piece.validMoves.isEmpty()) {
                piece.setCursor(Cursor.OPEN_HAND);
            }
        }

    }
    private void pieceDraggedHandler(MouseEvent e) {
        if(winner != null) {
            e.consume();
            return;
        }
        if(e.getTarget() instanceof Piece piece && state != GameState.CHECKMATE && state != GameState.STALEMATE) {
            piece.dragged(e, currentPlayer);
            piece.setCursor(Cursor.CLOSED_HAND);
        }

    }
    private void pieceReleasedHandler(MouseEvent e) {
        if(winner != null) {
            e.consume();
            return;
        }
        if(e.getTarget() instanceof Piece piece && state != GameState.CHECKMATE && state != GameState.STALEMATE) {
            double panel_size = Config.PANEL_SIZE;
            Square destination = board.getSquareAt(
                    new Position((int) (e.getSceneX() / panel_size), (int) (e.getSceneY() / panel_size))
            );
            if(isTurn(piece)) {
                move(piece.getInitialSquare(), destination);
            }
        }
        if(state == GameState.CHECKMATE) {
            winner = getOpponent(currentPlayer);
        }
        setWinScreen();
    }
    private void setPieceValidMoves(Piece piece, Square start) {
        piece.validMoves = new ArrayList<>();
        for (Square move :
                piece.possibleMoves(start)) {
            if(isValidMove(start, move, piece)) piece.validMoves.add(move);
        }
    }
    public void move(Square start, Square destination) {

        Piece piece = start.getPiece();

        if(isValidMove(start, destination, piece)) {
            piece.move(start, destination);
            piece.setCursor(Cursor.DEFAULT);

            if(state.equals(GameState.CHECK) && !isChecked(currentPlayer)) {
                state = GameState.PLAY;
                checkedKingSquare.unhighlight();
            }
            if(start != destination) {
                currentPlayer.setHasPlayed(true);
                getOpponent(currentPlayer).setHasPlayed(false);
                switchTurn();
                currentPlayer.setHasPlayed(false);
                getOpponent(currentPlayer).setHasPlayed(true);
            };
            adjustTimers();

            if(isChecked(currentPlayer)) {
                state = GameState.CHECK;
                checkedKingSquare = getKingSquare(currentPlayer);
                checkedKingSquare.highlight();
                if(getAllValidMoves(currentPlayer).isEmpty()) state = GameState.CHECKMATE;
            }
            else if(getAllValidMoves(currentPlayer).isEmpty() || (board.getPositionsOccupiedBy(currentPlayer).size() == 1
                    && board.getPositionsOccupiedBy(getOpponent(currentPlayer)).size() == 1)) {
                state = GameState.STALEMATE;
            }

        }

    }
    public Board getBoard() {
        return board;
    }

    public void start() {
        this.currentPlayer = white;
        currentPlayer.setHasPlayed(false);
        getOpponent(currentPlayer).setHasPlayed(true);
        adjustTimers();
        this.state = GameState.PLAY;
        for (int i = 0; i < board.getColumnCount(); i++) {
            board.setPiece(new Pawn(PlayerColor.WHITE, board.getPanelSize()), new Position(i,6));
            board.setPiece(new Pawn(PlayerColor.BLACK, board.getPanelSize()), new Position(i,1));
        }
        King wk = new King(PlayerColor.WHITE, board.getPanelSize());
        King bk = new King(PlayerColor.BLACK, board.getPanelSize());
        this.wk = wk;
        this.bk = bk;
        board.setPiece(bk, new Position(4,0));
        board.setPiece(wk, new Position(4,7));

        board.setPiece(new Queen(PlayerColor.BLACK, board.getPanelSize()), new Position(3,0));
        board.setPiece(new Queen(PlayerColor.WHITE, board.getPanelSize()), new Position(3,7));

        board.setPiece(new Bishop(PlayerColor.BLACK, board.getPanelSize()), new Position(2,0));
        board.setPiece(new Bishop(PlayerColor.WHITE, board.getPanelSize()), new Position(2,7));
        board.setPiece(new Bishop(PlayerColor.BLACK, board.getPanelSize()), new Position(5,0));
        board.setPiece(new Bishop(PlayerColor.WHITE, board.getPanelSize()), new Position(5,7));

        board.setPiece(new Knight(PlayerColor.BLACK, board.getPanelSize()), new Position(1,0));
        board.setPiece(new Knight(PlayerColor.WHITE, board.getPanelSize()), new Position(1,7));
        board.setPiece(new Knight(PlayerColor.BLACK, board.getPanelSize()), new Position(6,0));
        board.setPiece(new Knight(PlayerColor.WHITE, board.getPanelSize()), new Position(6,7));

        board.setPiece(new Rook(PlayerColor.BLACK, board.getPanelSize()), new Position(0,0));
        board.setPiece(new Rook(PlayerColor.WHITE, board.getPanelSize()), new Position(0,7));
        board.setPiece(new Rook(PlayerColor.BLACK, board.getPanelSize()), new Position(7,0));
        board.setPiece(new Rook(PlayerColor.WHITE, board.getPanelSize()), new Position(7,7));
        board.givePiecesBoard();

        setWinScreen();
    }

    private void switchTurn() {
        currentPlayer = getOpponent(currentPlayer);
    }

    private boolean isTurn(Piece piece) {
        return piece.getColor() == currentPlayer.getColor();
    }

    private Player getOpponent(Player player) {
        return player == white ? black: white;
    }

    private boolean isChecked(Player player) {
        King playerKing = player == white? wk: bk;
        Square kingSquare = board.getPieceSquare(playerKing);
        for (Square s :
                board.getPositionsOccupiedBy(getOpponent(player))) {
            Piece p = s.getPiece();
            for (Square r :
                    p.getCapturePositions(s)) {
                if (r == kingSquare) return true;
            }
        }
        return false;
    }

    private void adjustTimers() {
        if(currentPlayer.hasPlayed()) {
            UTI.pausePlayer(currentPlayer);
            UTI.resumePlayer(getOpponent(currentPlayer));
        }
        else if(!currentPlayer.hasPlayed()) {
            UTI.resumePlayer(currentPlayer);
            UTI.pausePlayer(getOpponent(currentPlayer));
        }
    }

    private void stopTimers() {
        UTI.pausePlayer(currentPlayer);
        UTI.pausePlayer(getOpponent(currentPlayer));
    }

    private boolean isValidMove(Square start, Square destination, Piece piece) {
        Player player = piece.getColor() == PlayerColor.WHITE ? white: black;
        Piece capturedPiece  = destination.getPiece();
        if(start == destination) {
            return true;
        }
        boolean moved = piece.move(start,destination);
        boolean isValid = false;
        if(moved && !isChecked(player)) {
            isValid = true;
        }
        start.clear();
        start.setPiece(piece);
        destination.clear();
        destination.setPiece(capturedPiece);
        piece.initialSquare = start;
        return isValid;
    }
    private List<Square> getAllValidMoves(Player player) {
        List<Square> validMoves = new ArrayList<>();
        for (Square playerSquare :
                board.getPositionsOccupiedBy(player)) {
            for (Square possibleMove :
                    playerSquare.getPiece().possibleMoves(playerSquare)) {
                if (isValidMove(playerSquare, possibleMove, playerSquare.getPiece())) {
                    validMoves.add(possibleMove);
                }
            }
        }
        return validMoves;
    }
    private Square getKingSquare(Player player) {
        King checkedKing = player == white? wk:bk;
        return board.getPieceSquare(checkedKing);
    }

    public void setWinScreen() {
        if(winner != null || state == GameState.STALEMATE || timeElapsed()) {
            stopTimers();
            BorderPane screen = new BorderPane();
            Label winnerLabel = new Label();
            if(winner != null) {
                winnerLabel.setText("The winner is " + winner);
            }else {
                winnerLabel.setText("Its a Tie!");
            }
            winnerLabel.setFont(Font.font("Verdana", 30));
            winnerLabel.setTextFill(Color.RED);
            screen.setCenter(winnerLabel);
            screen.setPrefSize(Config.PANEL_SIZE*8,Config.PANEL_SIZE*8);
            screen.setStyle("-fx-background-color: rgba(255,255,255,0.7)");
            this.getChildren().add(screen);
            screen.toFront();
        }
    }

    private boolean timeElapsed() {
        return currentPlayer.timeElapsed() || getOpponent(currentPlayer).timeElapsed();
    }
}
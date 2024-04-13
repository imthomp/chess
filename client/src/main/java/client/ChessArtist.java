package client;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static client.EscapeSequences.*;

public class ChessArtist {

    //private static final int SQUARE_SIZE_IN_CHARS = 3;
    public static final String EMPTY_SQUARE = "   ";
    public ChessBoard board;

    public ChessArtist(ChessBoard board) {
        this.board = board;
    }

    public void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawBoardPerspectives(out);
    }

    public void drawBoardPerspectives(PrintStream out) {
        drawChessBoard(out, ChessGame.TeamColor.WHITE);
        drawChessBoard(out, ChessGame.TeamColor.BLACK);
    }

    public void highlightLegalMoves(PrintStream out, ChessGame.TeamColor perspective, ChessPosition position) {
        try {
            ChessGame chessGame = new ChessGame();
            chessGame.setBoard(board);
            chessGame.validMoves(position);
        }
        catch (Exception e) {
            out.println("Invalid position");
            return;
        }
        out.println(SET_TEXT_BOLD);
        drawRowHeader(out, perspective);
        if (perspective == ChessGame.TeamColor.WHITE) {
            for (int row = ChessBoard.BOARD_SIZE; row >= 1; row--) {
                drawHighlightRow(out, board, perspective, row, position);
            }
        } else {
            for (int row = 1; row <= ChessBoard.BOARD_SIZE; row++) {
                drawHighlightRow(out, board, perspective, row, position);
            }
        }
        drawRowHeader(out, perspective);
        out.print(RESET_TEXT_BOLD_FAINT);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(RESET_BG_COLOR);
    }

    private void drawHighlightRow(PrintStream out, ChessBoard board, ChessGame.TeamColor perspective, int row, ChessPosition position) {
        drawColHeader(out, row);
        ChessGame chessGame = new ChessGame();
        chessGame.setBoard(board);
        var validMoves = chessGame.validMoves(position);
        if (perspective == ChessGame.TeamColor.WHITE) {
            for (int col = 1; col <= ChessBoard.BOARD_SIZE; col++) {
                if (validMoves.contains(new ChessMove(position, new ChessPosition(row, col), null))) {
                    drawHighlightSquare(out, board, row, col, position);
                } else {
                    drawSquare(out, board, row, col);
                }
            }
        } else {
            for (int col = ChessBoard.BOARD_SIZE; col >= 1; col--) {
                if (validMoves.contains(new ChessMove(position, new ChessPosition(row, col), null))) {
                    drawHighlightSquare(out, board, row, col, position);
                } else {
                    drawSquare(out, board, row, col);
                }
            }
        }
        drawColHeader(out, row);
        out.println(RESET_BG_COLOR);
    }

    private void drawHighlightSquare(PrintStream out, ChessBoard board, int row, int col, ChessPosition position) {
        boolean isBlack = (row + col) % 2 == 0;
        out.print(isBlack ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_GREEN);
        if (position.getRow() == row && position.getColumn() == col) {
            out.print(SET_BG_COLOR_YELLOW);
        }
        out.print(SET_TEXT_COLOR_RED);
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece != null) {
            out.print(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE);
        }
        out.print(piece == null ? EMPTY_SQUARE : " " + piece + " ");
        out.print(RESET_BG_COLOR);
    }

    public void drawChessBoard(PrintStream out, ChessGame.TeamColor perspective) {
        out.println(SET_TEXT_BOLD);
        drawRowHeader(out, perspective);
        if (perspective == ChessGame.TeamColor.WHITE) {
            for (int row = ChessBoard.BOARD_SIZE; row >= 1; row--) {
                drawRow(out, board, perspective, row);
            }
        } else {
            for (int row = 1; row <= ChessBoard.BOARD_SIZE; row++) {
                drawRow(out, board, perspective, row);
            }
        }
        drawRowHeader(out, perspective);
        out.print(RESET_TEXT_BOLD_FAINT);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(RESET_BG_COLOR);
    }

    private void drawColHeader(PrintStream out, int row) {
        setHeader(out);
        out.print(" " + (row) + " ");
    }

    private void drawRowHeader(PrintStream out, ChessGame.TeamColor perspective) {
        setHeader(out);
        out.print(EMPTY_SQUARE);
        if (perspective == ChessGame.TeamColor.WHITE) {
            for (int col = 1; col <= ChessBoard.BOARD_SIZE; col++) {
                out.print(" " + (char) ('a' - 1 + col) + " ");
            }
        } else {
            for (int col = ChessBoard.BOARD_SIZE; col >= 1; col--) {
                out.print(" " + (char) ('a' - 1 + col) + " ");
            }
        }
        out.print(EMPTY_SQUARE);
        out.println(RESET_BG_COLOR);
    }

    private void drawRow(PrintStream out, ChessBoard board, ChessGame.TeamColor perspective, int row) {
        drawColHeader(out, row);
        if (perspective == ChessGame.TeamColor.WHITE) {
            for (int col = 1; col <= ChessBoard.BOARD_SIZE; col++) {
                drawSquare(out, board, row, col);
            }
        } else {
            for (int col = ChessBoard.BOARD_SIZE; col >= 1; col--) {
                drawSquare(out, board, row, col);
            }
        }
        drawColHeader(out, row);
        out.println(RESET_BG_COLOR);
    }

    private void drawSquare(PrintStream out, ChessBoard board, int row, int col) {
        boolean isBlack = (row + col) % 2 == 0;
        out.print(isBlack ? SET_BG_COLOR_BLACK : SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_RED);
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece != null) {
            out.print(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE);
        }
        out.print(piece == null ? EMPTY_SQUARE : " " + piece + " ");
        out.print(RESET_BG_COLOR);
    }

    private void setHeader(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}
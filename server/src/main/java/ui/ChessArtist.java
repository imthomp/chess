package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessArtist {

    //private static final int SQUARE_SIZE_IN_CHARS = 3;
    public static final String EMPTY_SQUARE = "   ";

    public static void main(String[] args) {
        ChessBoard board = new ChessBoard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawBoardPerspectives(out, board);
        board.resetBoard();
        drawBoardPerspectives(out, board);
    }

    public static void drawBoardPerspectives(PrintStream out, ChessBoard board) {
        drawChessBoard(out, board, ChessGame.TeamColor.WHITE);
        drawChessBoard(out, board, ChessGame.TeamColor.BLACK);
    }

    private static void drawChessBoard(PrintStream out, ChessBoard board, ChessGame.TeamColor perspective) {
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
    }

    private static void drawColHeader(PrintStream out, int row) {
        setHeader(out);
        out.print(" " + (row) + " ");
    }

    private static void drawRowHeader(PrintStream out, ChessGame.TeamColor perspective) {
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

    private static void drawRow(PrintStream out, ChessBoard board, ChessGame.TeamColor perspective, int row) {
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

    private static void drawSquare(PrintStream out, ChessBoard board, int row, int col) {
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

    private static void setHeader(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}
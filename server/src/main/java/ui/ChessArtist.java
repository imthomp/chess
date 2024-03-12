package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessArtist {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    public static final String EMPTY_SQUARE = "   ";

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawChessBoard(out);
    }

    private static void drawChessBoard(PrintStream out) {
        drawHeader(out);
        for (int row = 0; row < BOARD_SIZE_IN_SQUARES; row++) {
            drawRow(out, row);
        }
        drawHeader(out);
    }

    private static void drawHeader(PrintStream out) {
        setHeader(out);
        out.print(EMPTY_SQUARE);
        for (int square = 0; square < BOARD_SIZE_IN_SQUARES; square++) {
            out.print(" " + (char) ('a' + square) + " ");
        }
        out.print(EMPTY_SQUARE);
        out.println(RESET_BG_COLOR);
    }

    private static void drawRow(PrintStream out, int row) {
        setHeader(out);
        out.print(" " + (row + 1) + " ");
        for (int square = 0; square < BOARD_SIZE_IN_SQUARES; square++) {
            drawSquare(out, row, square);
        }
        setHeader(out);
        out.print(" " + (row + 1) + " ");
        out.println(RESET_BG_COLOR);
    }

    private static void drawSquare(PrintStream out, int row, int square) {
        boolean isWhite = (row + square) % 2 == 0;
        out.print(isWhite ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_RED);
        out.print(" " + "X" + " ");
        out.print(RESET_BG_COLOR);
    }

    private static void setHeader(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }
}
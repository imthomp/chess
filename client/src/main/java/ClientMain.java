import chess.*;
import client.Repl;

import static client.EscapeSequences.RESET_BG_COLOR;
import static client.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class ClientMain {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.print(RESET_BG_COLOR + SET_TEXT_COLOR_WHITE);
        System.out.println("♕ 240 Chess Client: " + piece);

        new Repl(serverUrl).run();
    }
}
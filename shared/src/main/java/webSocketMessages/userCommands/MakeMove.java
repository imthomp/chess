package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    public MakeMove(String authToken, Integer gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
    }

    public Integer gameID;
    public ChessMove move;

    public Integer getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MakeMove makeMove = (MakeMove) o;
        return gameID.equals(makeMove.gameID) && move.equals(makeMove.move);
    }

    @Override
    public int hashCode() {
        return gameID.hashCode() + move.hashCode();
    }
}
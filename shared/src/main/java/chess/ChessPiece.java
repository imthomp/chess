package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        if (type == PieceType.KING) {
            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                for (int colOffset = -1; colOffset <= 1; colOffset++) {
                    if (rowOffset != 0 || colOffset != 0) {
                        ChessPosition destination = new ChessPosition(myPosition.getRow() + rowOffset, myPosition.getColumn() + colOffset);
                        moves.add(new ChessMove(myPosition, destination, null));
                        // Check if the destination is valid
                        // Check if the destination is not occupied by a friendly piece
                    }
                }
            }
        } else if (type == PieceType.QUEEN) {
            throw new RuntimeException("Not implemented");
        } else if (type == PieceType.BISHOP) {
            throw new RuntimeException("Not implemented");
        } else if (type == PieceType.KNIGHT) {
            throw new RuntimeException("Not implemented");
        } else if (type == PieceType.ROOK) {
            throw new RuntimeException("Not implemented");
        } else if (type == PieceType.PAWN) {
            throw new RuntimeException("Not implemented");
        }
        return moves;
    }
}

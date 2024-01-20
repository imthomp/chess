package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        if (type == PieceType.KING) {
            throw new RuntimeException("Not implemented");
        } else if (type == PieceType.QUEEN) {
            throw new RuntimeException("Not implemented");
        } else if (type == PieceType.BISHOP) {
            getBishopMoves(board, myPosition, moves);
        } else if (type == PieceType.KNIGHT) {
            throw new RuntimeException("Not implemented");
        } else if (type == PieceType.ROOK) {
            throw new RuntimeException("Not implemented");
        } else if (type == PieceType.PAWN) {
            throw new RuntimeException("Not implemented");
        }
        return moves;
    }

    private void getBishopMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves) {
        int[] xDirections = new int[] {1, -1, -1, 1};
        int[] yDirections = new int[] {1, 1, -1, -1};

        for (int i = 0; i < xDirections.length; i++) {
            int xDirection = xDirections[i];
            int yDirection = yDirections[i];
            ChessPosition destination = new ChessPosition(myPosition.getRow() + xDirection, myPosition.getColumn() + yDirection);
            while (isInBounds(destination)) {
                ChessPiece piece = board.getPiece(destination);
                if (piece == null) {
                    moves.add(new ChessMove(myPosition, destination, null));
                } else if (isNotMyTeam(piece)) {
                    moves.add(new ChessMove(myPosition, destination, null));
                    break;
                } else {
                    break;
                }
                destination = new ChessPosition(destination.getRow() + xDirection, destination.getColumn() + yDirection);
            }
        }
    }

    private boolean isInBounds(ChessPosition position) {
        return position.getRow() > 0 && position.getRow() <= ChessBoard.BOARD_SIZE && position.getColumn() > 0 && position.getColumn() <= ChessBoard.BOARD_SIZE;
    }

    private boolean isNotMyTeam(ChessPiece piece) {
        return piece.getTeamColor() != getTeamColor();
    }
}

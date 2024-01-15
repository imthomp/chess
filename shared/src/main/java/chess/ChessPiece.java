package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

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
        HashSet<ChessMove> moves = new HashSet<>();
        if (type == PieceType.KING) {
            // King can move one square in any direction
            int[] rowOffsets = {-1, -1, -1, 0, 0, 1, 1, 1};
            int[] colOffsets = {-1, 0, 1, -1, 1, -1, 0, 1};

            for (int i = 0; i < rowOffsets.length; i++) {
                int rowOffset = rowOffsets[i];
                int colOffset = colOffsets[i];

                ChessPosition destination = new ChessPosition(myPosition.getRow() + rowOffset, myPosition.getColumn() + colOffset);

                // Check if the destination is valid and not occupied by a friendly piece
                if (board.isValidPosition(destination) && (board.getPiece(destination) == null || board.getPiece(destination).getTeamColor() != getTeamColor())) {
                    moves.add(new ChessMove(myPosition, destination, null));
                }
            }
        } else if (type == PieceType.QUEEN) {
            // Queen can move vertically, horizontally, and diagonally any number of squares
            // Combine the logic of rook and bishop
            // Rook-like movement
            getRookMoves(board, myPosition, moves);
            // Bishop-like movement
            getBishopMoves(board, myPosition, moves);
        } else if (type == PieceType.BISHOP) {
            // Bishop can move diagonally any number of squares
            getBishopMoves(board, myPosition, moves);
        } else if (type == PieceType.KNIGHT) {
            // Knight moves in an L-shape: two squares in one direction and one square perpendicular to that direction
            int[] rowOffsets = {-2, -2, -1, -1, 1, 1, 2, 2};
            int[] colOffsets = {-1, 1, -2, 2, -2, 2, -1, 1};

            for (int i = 0; i < rowOffsets.length; i++) {
                int rowOffset = rowOffsets[i];
                int colOffset = colOffsets[i];

                ChessPosition destination = new ChessPosition(myPosition.getRow() + rowOffset, myPosition.getColumn() + colOffset);

                // Check if the destination is valid and not occupied by a friendly piece
                if (board.isValidPosition(destination) && (board.getPiece(destination) == null || board.getPiece(destination).getTeamColor() != getTeamColor())) {
                    moves.add(new ChessMove(myPosition, destination, null));
                }
            }
        } else if (type == PieceType.ROOK) {
            // Rook can move vertically or horizontally any number of squares
            getRookMoves(board, myPosition, moves);
        } else if (type == PieceType.PAWN) {
            throw new RuntimeException("Not implemented");
        }
        return moves;
    }

    private void getBishopMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves) {
        int[] rowOffsets = {-1, -1, 1, 1};
        int[] colOffsets = {-1, 1, -1, 1};

        for (int i = 0; i < rowOffsets.length; i++) {
            int rowOffset = rowOffsets[i];
            int colOffset = colOffsets[i];

            ChessPosition destination = new ChessPosition(myPosition.getRow() + rowOffset, myPosition.getColumn() + colOffset);
            addMovesUntilBlocked(board, myPosition, destination, moves);
        }
    }

    private void getRookMoves(ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> moves) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            if (rowOffset != 0) {
                ChessPosition destination = new ChessPosition(myPosition.getRow() + rowOffset, myPosition.getColumn());
                addMovesUntilBlocked(board, myPosition, destination, moves);
            }
        }
        for (int colOffset = -1; colOffset <= 1; colOffset++) {
            if (colOffset != 0) {
                ChessPosition destination = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + colOffset);
                addMovesUntilBlocked(board, myPosition, destination, moves);
            }
        }
    }

    private void addMovesUntilBlocked(ChessBoard board, ChessPosition myPosition, ChessPosition destination, HashSet<ChessMove> moves) {
        while (board.isValidPosition(destination)) {
            ChessPiece pieceAtDestination = board.getPiece(destination);

            if (pieceAtDestination == null) {
                moves.add(new ChessMove(myPosition, destination, null));
            } else if (pieceAtDestination.getTeamColor() != getTeamColor()) {
                moves.add(new ChessMove(myPosition, destination, null));
                break;  // Capture opponent's piece, then stop the movement
            } else {
                break;  // Friendly piece blocking the path
            }

            destination = new ChessPosition(destination.getRow() + Integer.signum(myPosition.getRow() - destination.getRow()),
                    destination.getColumn() + Integer.signum(myPosition.getColumn() - destination.getColumn()));
        }
    }
}

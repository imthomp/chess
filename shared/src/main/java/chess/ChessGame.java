package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;

    public boolean gameIsDone = false;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        if (isInCheck(piece.getTeamColor())) {
            moves = getOutOfCheckMoves(piece, startPosition);
        }
        return checkAllPieceMoves(piece, startPosition, moves);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No piece at start position");
        }
        if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Not team's turn");
        }
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if (!moves.contains(move)) {
            throw new InvalidMoveException("Move not in valid moves");
        }
        if (move.getPromotionPiece() != null) {
            if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
                throw new InvalidMoveException("Piece is not a pawn");
            }
            if (move.getEndPosition().getRow() != 8 && move.getEndPosition().getRow() != 1) {
                throw new InvalidMoveException("Pawn is not at end of board");
            }
            board.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
        } else {
            board.addPiece(move.getEndPosition(), piece);
        }
        board.addPiece(move.getStartPosition(), null);
        setTeamTurn((teamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (int row = 1; row <= ChessBoard.BOARD_SIZE; row++) {
            for (int col = 1; col <= ChessBoard.BOARD_SIZE; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = new ChessPosition(row, col);
                }
            }
        }
        for (int row = 1; row <= ChessBoard.BOARD_SIZE; row++) {
            for (int col = 1; col <= ChessBoard.BOARD_SIZE; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = piece.pieceMoves(board, new ChessPosition(row, col));
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return isInEndgame(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        return isInEndgame(teamColor);
    }

    private boolean isInEndgame(TeamColor teamColor) {
        for (int row = 1; row <= ChessBoard.BOARD_SIZE; row++) {
            for (int col = 1; col <= ChessBoard.BOARD_SIZE; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (!getOutOfCheckMoves(piece, new ChessPosition(row, col)).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        gameIsDone = true;
        return true;
    }

    public void resign() {
        gameIsDone = true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private Collection<ChessMove> getOutOfCheckMoves(ChessPiece piece, ChessPosition startPosition) {
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        return checkAllPieceMoves(piece, startPosition, moves);
    }

    private Collection<ChessMove> checkAllPieceMoves(ChessPiece piece, ChessPosition startPosition, Collection<ChessMove> moves) {
        for (var move : piece.pieceMoves(board, startPosition)) {
            ChessPiece pieceAtEnd = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);
            if (isInCheck(piece.getTeamColor())) {
                moves.remove(move);
            }
            board.addPiece(move.getStartPosition(), piece);
            board.addPiece(move.getEndPosition(), pieceAtEnd);
        }
        return moves;
    }
}
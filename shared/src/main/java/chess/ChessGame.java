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

    public ChessGame() {
        board = new ChessBoard();
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
        // get the piece at the start position
        // if there is no piece, return null
        // otherwise, return the piece's valid moves
        // cannot put king in check
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
        // get the piece at the start position
        // if there is no piece, throw an exception
        // otherwise, get the piece's valid moves
        // if the move is not in the valid moves, throw an exception
        // otherwise, make the move
        // if the move is a pawn promotion, promote the pawn
        
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
        // find the king
        // find all the other team's pieces
        // check if any of the other team's pieces can move to the king
        // if so, return true
        // if not, return false
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
        // is in check?
        // valid moves?
        // if no valid moves, checkmate
        // if valid moves, not checkmate
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
        // is in check?
        // valid moves?
        // if no valid moves, stalemate
        // if valid moves, not stalemate
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
        return true;
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

    // getOutOfCheckMoves
    // get all moves for all pieces of the team
    // for each move, make the move
    // if the king is still in check, remove the move
    // undo the move
    // return the moves
    private Collection<ChessMove> getOutOfCheckMoves(ChessPiece piece, ChessPosition startPosition) {
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        return checkAllPieceMoves(piece, startPosition, moves);
    }

    private Collection<ChessMove> checkAllPieceMoves(ChessPiece piece, ChessPosition startPosition, Collection<ChessMove> moves) {
        for (var move : piece.pieceMoves(board, startPosition)) {
            // make the move
            // check if king is in check
            // if so, remove the move
            // undo the move
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

// king is in check and thinks he can get out by capturing
// when it gets to the new position, check if still in check
// so, check new position!
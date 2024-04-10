package dataAccess.object.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DatabaseManager;
import dataAccess.exception.DataAccessException;
import dataAccess.object.protocol.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;

public class SQLGameDAO implements GameDAO {
    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    public void createGame(Integer gameID, String gameName) throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(gameID, null, null, gameName, chessGame);
        String statement = "INSERT INTO game (gameID, gameData) VALUES (?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                var serializer = new Gson();
                preparedStatement.setString(2, serializer.toJson(game));
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT * FROM game WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, gameID);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String jsonGameData = resultSet.getString("gameData");
                        var serializer = new Gson();
                        return serializer.fromJson(jsonGameData, GameData.class);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    public Collection<GameData> listGames() throws DataAccessException {
        HashSet<GameData> games = new HashSet<>();
        String statement = "SELECT * FROM game";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    var serializer = new Gson();
                    while (resultSet.next()) {
                        GameData gameData = serializer.fromJson(resultSet.getString("gameData"), GameData.class);
                        games.add(gameData);
                    }
                    return games;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        String statement = "DELETE FROM game";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(GameData g) throws DataAccessException {
        int gameID = g.gameID();
        String statement = "UPDATE game SET gameData = ? WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                var serializer = new Gson();
                preparedStatement.setString(1, serializer.toJson(g));
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
                gameID INTEGER PRIMARY KEY,
                gameData TEXT NOT NULL
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        SQLAuthDAO.manageConnection(createStatements);
    }
}

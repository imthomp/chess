package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.JoinGameObject;
import model.UserData;

import java.io.*;
import java.net.*;

public class ServerFacade {

    String serverUrl;
    public ServerFacade (String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String register(String username, String password, String email) throws ResponseException {
        String path = "/user";
        UserData user = new UserData(username, password, email);
        AuthData auth = this.makeRequest("POST", path, user, AuthData.class, null);
        return auth.authToken();
    }

    public String login(String username, String password) throws ResponseException {
        String path = "/session";
        UserData user = new UserData(username, password, null);
        AuthData auth = this.makeRequest("POST", path, user, AuthData.class, null);
        return auth.authToken();
    }

    public void logout(String authToken) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authToken);
    }

    public int createGame(GameData game, String authToken) throws ResponseException {
        var path = "/game";
        GameData gameReturned = this.makeRequest("POST", path, game, GameData.class, authToken);
        return gameReturned.gameID();
    }

    public void joinGame(GameData game, String authToken) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, game, JoinGameObject.class, authToken);
    }

    public void observeGame(GameData game, String authToken) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, game, JoinGameObject.class, authToken);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public GameData[] listGames(String authToken) throws ResponseException {
        var path = "/game";
        record listGameResponse(GameData[] game) {}
        var response = this.makeRequest("GET", path, null, listGameResponse.class, authToken);
        return response.game();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authToken) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (var reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
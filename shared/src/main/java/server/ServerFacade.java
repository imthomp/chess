package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import model.UserData;

import java.io.*;
import java.net.*;

public class ServerFacade {

    String serverUrl;
    public ServerFacade (String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        var user = new UserData(username, password, email);
        this.makeRequest("POST", path, user, UserData.class);
    }

    public void login(String username, String password) throws ResponseException {
        var path = "/session";
        UserData user = new UserData(username, password, null);
        this.makeRequest("POST", path, user, UserData.class);
    }

    public void logout() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public void createGame(GameData game) throws ResponseException {
        var path = "/game";
        this.makeRequest("POST", path, game, GameData.class);
    }

    public void joinGame(GameData game) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, game, null);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public GameData[] listGames() throws ResponseException {
        var path = "/game";
        record listGameResponse(GameData[] game) {        }
        var response = this.makeRequest("GET", path, null, listGameResponse.class);
        return response.game();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

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
            try (OutputStream reqBody = http.getOutputStream()) {
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
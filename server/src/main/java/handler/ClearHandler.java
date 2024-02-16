package handler;

import com.google.gson.Gson;
import result.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    public Object handleClear(Request req, Response res) {
        ClearService service = new ClearService();
        ClearResult result = service.clear();
        if (result != null) {
            var serializer = new Gson();
            var json = serializer.toJson(result);
            res.status(500);
            return json;
        }
        res.status(200);
        return "";
    }
}

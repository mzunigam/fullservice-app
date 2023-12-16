package web.multitask.app.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.multitask.app.mysql.ProcedureMysql;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class AppApi {

    final ProcedureMysql procedureMysql;

    public AppApi(ProcedureMysql procedureMysql) {
        this.procedureMysql = procedureMysql;
    }

    @PostMapping("/procedure")
    public String callProcedure(@RequestBody String body) {
        JSONObject json = new JSONObject(body);

        if (json.has("procedure")) {
            try {
                JSONArray params = json.isNull("params") ? new JSONArray() : json.getJSONArray("params");
                JSONObject response = procedureMysql.ProcedureExecution(json.getString("procedure"), params.toList().toArray());
                return response.put("status", true).put("message", "Success").toString();
            } catch (Exception e) {
                return new JSONObject().put("data", new JSONArray()).put("message", e.getMessage()).put("status", false).toString();
            }
        } else {
            return new JSONObject().put("data", new JSONArray()).put("message", "Invalid Request").put("status", false).toString();
        }
    }

}
package web.multitask.app.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import web.multitask.app.mysql.ProcedureMysql;
import web.multitask.app.repository.UserRespository;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class AppApi {

    final ProcedureMysql procedureMysql;
    final UserRespository userRepo;

    public AppApi(ProcedureMysql procedureMysql, UserRespository userRepo) {
        this.procedureMysql = procedureMysql;
        this.userRepo = userRepo;
    }

    @PostMapping("/procedure")
    public String callProcedure(@RequestBody String body) {
        JSONObject json = new JSONObject(body);
        if (json.has("procedure")) {
            try {
                JSONArray params = json.isNull("params") ? new JSONArray() : json.getJSONArray("params");
                JSONObject response = procedureMysql.ProcedureExecution(json.getString("procedure"),json.getString("database"), params.toList().toArray());
                return response.toString();
            } catch (Exception e) {
                return new JSONObject().put("data", new JSONArray()).put("message", e.getMessage()).put("status", false).toString();
            }
        } else {
            return new JSONObject().put("data", new JSONArray()).put("message", "Invalid Request").put("status", false).toString();
        }
    }

    @GetMapping("/users")
    public String getUsers (){
        return new JSONObject().put("data", userRepo.findAll()).put("message", "Success").put("status", true).toString();
    }

}
package web.multitask.app.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/private/procedure")
    public ResponseEntity<?> callProcedure(@RequestBody String body) {
        JSONObject json = new JSONObject(body);
        if (json.has("procedure")) {
            try {
                JSONArray params = json.isNull("params") ? new JSONArray() : json.getJSONArray("params");
                JSONObject response = procedureMysql.ProcedureExecution(json.getString("procedure"),json.getString("database"), params.toList().toArray());
                return ResponseEntity.ok(response.toMap());
            } catch (Exception e) {
                return ResponseEntity.internalServerError().body(new JSONObject().put("message", e.getMessage()).put("status", false).toMap());
            }
        } else {
            return ResponseEntity.badRequest().body(new JSONObject().put("message", "Invalid Request").put("status", false).toMap());
        }
    }

    @GetMapping("/private/users")
    public ResponseEntity<?> getUsers (){
        return ResponseEntity.ok(new JSONObject().put("data", userRepo.findAll()).put("message", "Success").put("status", true).toMap());
    }

}
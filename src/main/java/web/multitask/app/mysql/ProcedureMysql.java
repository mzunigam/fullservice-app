package web.multitask.app.mysql;


import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProcedureMysql {
    
    private final JdbcTemplate jdbcTemplate;

    public ProcedureMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JSONObject ProcedureExecution(String procedure ,String database, Object[] params) {
        try {
            StringBuilder query = new StringBuilder("CALL " + database + "." + procedure);

            if (params.length > 0) {
                query.append("(");
                for (int i = 0; i < params.length; i++) {
                    query.append("?");
                    if (i < params.length - 1) {
                        query.append(",");
                    }
                }
                query.append(")");
            }

            String checkProcedure = "SELECT COUNT(*) FROM information_schema.routines WHERE routine_schema = '" + database + "' AND routine_name = '" + procedure;
            List<Map<String, Object>> countProcedure = jdbcTemplate.queryForList(checkProcedure);
            
            if (countProcedure.get(0).get("COUNT(*)").toString().equals("0")) {
                return new JSONObject().put("message", "Procedure not found").put("status", false);
            }

            List<Map<String, Object>> list = jdbcTemplate.queryForList(query.toString(), params);
            JSONObject result = new JSONObject();
            result.put("data", list);
            result.put("message", "Success");
            result.put("status", true);
            return result;
        } catch (Exception e) {
            return new JSONObject().put("data", new JSONObject()).put("message", e.getMessage()).put("status", false);
        }
    }
}
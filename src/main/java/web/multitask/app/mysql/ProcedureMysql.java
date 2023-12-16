package web.multitask.app.mysql;


import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class ProcedureMysql {
    
    private final JdbcTemplate jdbcTemplate;

    public ProcedureMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JSONObject ProcedureExecution(String query , Object[] params) {
        try {
            List<Map<String, Object>> list = jdbcTemplate.queryForList(query, params);
            JSONObject result = new JSONObject();
            result.put("data", list);
            return result;
        } catch (Exception e) {
            Logger.getLogger("ProcedureMysql").warning(e.getMessage());
        }
        return null;
    }
}
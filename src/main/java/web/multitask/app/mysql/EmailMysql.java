package web.multitask.app.mysql;

import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import web.multitask.app.utils.JWTokenUtil;

import java.util.List;
import java.util.Map;

@Service
public class EmailMysql {

    private final JdbcTemplate jdbcTemplate;

    public EmailMysql(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JSONObject getHTMLTemplate (JSONObject json) {
        String id = json.optString("id",null);
        if(id == null){
            return new JSONObject().put("message", "Invalid Request").put("status", false);
        }
        String sql = "SELECT * FROM security.email_template  WHERE id = ?";
        Object[] params = new Object[] { id };
        try{
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, params);
            return new JSONObject().put("data", rows).put("message", "Success").put("status", true);
        }catch (Exception e){
            return new JSONObject().put("message", e.getMessage()).put("status", false);
        }
    }
}
package website;

import hi.request;
import hi.response;
import hi.route;

import java.util.regex.Matcher;
import java.util.Map;
import java.util.HashMap;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;

import com.google.gson.Gson;

import website.website;
import website.db_help;

public class update implements hi.route.run_t {
    public update() {
    }

    public void handler(hi.request req, hi.response res, Matcher m) {
        if (req.method.equals("POST")) {
            String sql = "UPDATE `websites` SET `name` = ? ,`url` = ? WHERE `id` = ?;";
            Object[] params = new Object[3];
            if (req.form.containsKey("name") && req.form.containsKey("url") && req.form.containsKey("id")) {
                params[0] = req.form.get("name");
                params[1] = req.form.get("url");
                params[2] = Integer.valueOf(req.form.get("id")).intValue();
                res.set_content_type("application/json");
                Gson gson = new Gson();
                HashMap<String, Object> map = new HashMap<String, Object>();
                try {
                    QueryRunner qr = new QueryRunner(db_help.get_instance().get_data_source());
                    int result = qr.update(sql, params);
                    map.put("status", true);
                    map.put("rows", result);
                    res.content = gson.toJson(map);
                    res.status = 200;
                } catch (SQLException e) {
                    map.put("status", false);
                    map.put("message", e.getMessage());
                    res.content = gson.toJson(map);
                    res.status = 500;
                }
            }
        }
    }
}

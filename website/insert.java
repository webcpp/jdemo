package website;

import hi.request;
import hi.response;
import hi.route;

import java.util.regex.Matcher;
import java.util.Map;
import java.util.HashMap;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;

import com.google.gson.Gson;

import website.website;
import website.db_help;

public class insert implements hi.route.run_t {
    public insert() {
    }

    public void handler(hi.request req, hi.response res, Matcher m) {
        if (req.method.equals("POST")) {
            String sql = "INSERT INTO `websites`(`name`,`url`)VALUES(?,?);";
            Object[] params = { "", "" };
            if (req.form.containsKey("name") && req.form.containsKey("url")) {
                params[0] = req.form.get("name");
                params[1] = req.form.get("url");
                res.set_content_type("application/json");
                Gson gson = new Gson();
                HashMap<String, Object> map = new HashMap<String, Object>();
                try {

                    QueryRunner qr = new QueryRunner(db_help.get_instance().get_data_source());
                    Object[] result = qr.insert(sql, new ArrayHandler(), params);
                    map.put("status", true);
                    for (Object item : result) {
                        map.put("id", item);
                    }
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

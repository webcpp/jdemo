package website;

import hi.request;
import hi.response;
import hi.route;

import java.util.regex.Matcher;
import java.util.Map;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanMapHandler;

import website.website;
import website.db_help;

public class list implements hi.route.run_t {
    public list() {
    }

    public void handler(hi.request req, hi.response res, Matcher m) {
        if (req.method.equals("GET")) {
            String sql = "SELECT * FROM `websites` ORDER BY `id` LIMIT ?,?;";
            Object[] params = { 0, 5 };
            if (req.form.containsKey("start")) {
                params[0] = Integer.valueOf(req.form.get("start")).intValue();
            }
            if (req.form.containsKey("size")) {
                params[1] = Integer.valueOf(req.form.get("size")).intValue();
            }
            res.set_content_type("text/plain;charset=UTF-8");
            try {
                QueryRunner qr = new QueryRunner(db_help.get_instance().get_data_source());

                Map<String, website> result = qr.query(sql, new BeanMapHandler<String, website>(website.class), params);
                StringBuffer content = new StringBuffer();

                for (Map.Entry<String, website> item : result.entrySet()) {
                    content.append(String.format("%s\tid = %s\tname = %s\turl = %s\n", item.getKey(),
                            item.getValue().getId(), item.getValue().getName(), item.getValue().getUrl()));
                }

                res.content = content.toString();
                res.status = 200;
            } catch (SQLException e) {
                res.content = e.getMessage();
                res.status = 500;
            }
        }
    }
}

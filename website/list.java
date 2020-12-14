package website;

import hi.request;
import hi.response;
import hi.route;

import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import website.website;
import website.db_help;

public class list implements hi.route.run_t {
    private static List<String> order_t = Arrays.asList("DESC", "desc", "ASC", "asc");
    private static String integer_pattern = new String("[1-9]+[0-9]*");

    public list() {
    }

    public void handler(hi.request req, hi.response res, Matcher m) {
        if (req.method.equals("GET")) {
            String sql = "SELECT * FROM `websites` ORDER BY `id` %s LIMIT ?,?;";
            Object[] params = { 0, 5 };
            if (req.form.containsKey("order")) {
                String tmp = req.form.get("order");
                if (!list.order_t.contains(tmp)) {
                    sql = String.format(sql, "DESC");
                } else {
                    sql = String.format(sql, tmp);
                }
            } else {
                sql = String.format(sql, "DESC");
            }
            if (req.form.containsKey("start")) {
                String p1 = req.form.get("start");
                if (p1.matches(list.integer_pattern)) {
                    params[0] = Integer.valueOf(p1).intValue();
                }
            }
            if (req.form.containsKey("size")) {
                String p2 = req.form.get("size");
                if (p2.matches(list.integer_pattern)) {
                    params[1] = Integer.valueOf(p2).intValue();
                }
            }
            res.set_content_type("text/plain;charset=UTF-8");
            try {
                QueryRunner qr = new QueryRunner(db_help.get_instance().get_data_source());

                List<website> result = qr.query(sql, new BeanListHandler<website>(website.class), params);
                StringBuffer content = new StringBuffer();

                for (website item : result) {
                    content.append(String.format("id = %s\tname = %s\turl = %s\n", item.getId(), item.getName(),
                            item.getUrl()));
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

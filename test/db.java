package test;

import hi.request;
import hi.response;
import hi.route;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.HashMap;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import org.mariadb.jdbc.MariaDbPoolDataSource;

public class db implements hi.route.run_t {

    private static class db_help {
        private static MariaDbPoolDataSource ds = null;

        private static db_help instance = new db_help();

        private db_help() {

        }

        public static db_help get_instance() {
            return db_help.instance;
        }

        public MariaDbPoolDataSource get_data_source() throws SQLException {
            if (db_help.ds != null) {
                return db_help.ds;
            }
            db_help.ds = new MariaDbPoolDataSource(hi.route.get_instance().get_config().getString("mariadb.url"));
            db_help.ds.setUser(hi.route.get_instance().get_config().getString("mariadb.username"));
            db_help.ds.setPassword(hi.route.get_instance().get_config().getString("mariadb.password"));
            return db_help.ds;
        }
    }

    public db() {

    }

    public void handler(hi.request req, hi.response res, Matcher m) {
        String sql = "SELECT * FROM `article` ORDER BY `id` LIMIT 0,5;";
        ResultSetHandler<ArrayList<HashMap<String, Object>>> h = (ResultSet rs) -> {
            ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
            while (rs.next()) {
                HashMap<String, Object> row = new HashMap<String, Object>();
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();

                for (int i = 0; i < cols; i++) {
                    row.put(meta.getColumnName(i + 1), rs.getObject(i + 1));
                }
                result.add(row);
            }
            return result;
        };
        try {
            QueryRunner qr = new QueryRunner(db_help.get_instance().get_data_source());
            ArrayList<HashMap<String, Object>> result = qr.query(sql, h);
            StringBuffer content = new StringBuffer();
            for (HashMap<String, Object> item : result) {
                for (HashMap.Entry<String, Object> iter : item.entrySet()) {
                    content.append(String.format("%s = %s\n", iter.getKey(), iter.getValue().toString()));
                }
                content.append("\n\n");
            }

            res.content = content.toString();
            res.status = 200;
        } catch (SQLException e) {
            res.content = e.getMessage();
            res.status = 500;
        }
    }
}
package website;

import java.sql.SQLException;
import com.alibaba.druid.pool.DruidDataSource;

public class db_help {
    private static DruidDataSource ds = null;

    private static db_help instance = new db_help();

    private db_help() {

    }

    public static db_help get_instance() {
        return db_help.instance;
    }

    public DruidDataSource get_data_source() throws SQLException {
        if (db_help.ds != null) {
            return db_help.ds;
        }
        db_help.ds = new DruidDataSource();
        db_help.ds.setUrl(hi.route.get_instance().get_config().getString("mariadb.url"));
        db_help.ds.setUsername(hi.route.get_instance().get_config().getString("mariadb.username"));
        db_help.ds.setPassword(hi.route.get_instance().get_config().getString("mariadb.password"));
        return db_help.ds;
    }
}
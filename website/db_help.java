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
        
        db_help.ds.setInitialSize(hi.route.get_instance().get_config().getInt("druid.initialSize"));
        db_help.ds.setMaxActive(hi.route.get_instance().get_config().getInt("druid.maxActive"));
        db_help.ds.setMaxWait(hi.route.get_instance().get_config().getLong("druid.maxWait"));
        db_help.ds.setMinIdle(hi.route.get_instance().get_config().getInt("druid.minIdle"));
        db_help.ds.setValidationQuery(hi.route.get_instance().get_config().getString("druid.validationQuery"));
        db_help.ds.setTestWhileIdle(hi.route.get_instance().get_config().getBoolean("druid.testWhileIdle"));
        db_help.ds.setTestOnBorrow(hi.route.get_instance().get_config().getBoolean("druid.testOnBorrow"));
        db_help.ds.setTestOnReturn(hi.route.get_instance().get_config().getBoolean("druid.testOnReturn"));

        return db_help.ds;
    }
}
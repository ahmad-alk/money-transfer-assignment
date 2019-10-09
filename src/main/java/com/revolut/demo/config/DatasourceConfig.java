package com.revolut.demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class DatasourceConfig {

    private static HikariDataSource hikariDataSource;


    public static DataSource getInstance() {

        if (null == hikariDataSource) {

            HikariConfig hikariConfig = new HikariConfig();
//            hikariConfig.setJdbcUrl("jdbc_url=jdbc:h2:tcp://localhost/~/revolut_schema;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
            hikariConfig.setJdbcUrl("jdbc:h2:mem:revolut_schema;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
            hikariConfig.setUsername("sa");
            hikariConfig.setPassword("");
            hikariConfig.setAutoCommit(true);
            hikariConfig.setDriverClassName("org.h2.Driver");

            return hikariDataSource = new HikariDataSource(hikariConfig);

        } else {
            return hikariDataSource;
        }
    }

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (null == connection) {
                return connection = getInstance().getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


}

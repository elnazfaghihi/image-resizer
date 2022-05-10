package com.debijenkorf.elnazfaghihiimageServiceassignment.config.log;

import com.debijenkorf.elnazfaghihiimageServiceassignment.utils.ProfileConstants;
import org.apache.logging.log4j.core.appender.db.jdbc.AbstractConnectionSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@Profile(ProfileConstants.SPRING_PROFILE_CLOUD)
public class ConnectionFactory extends AbstractConnectionSource {

    @Qualifier("logDataSource")
    private final DataSource dataSource;

    public ConnectionFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

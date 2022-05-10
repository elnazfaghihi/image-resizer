package com.debijenkorf.elnazfaghihiimageServiceassignment.config.jpa;

import com.debijenkorf.elnazfaghihiimageServiceassignment.utils.ProfileConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@ConfigurationProperties(prefix = "logdb.datasource")
@Profile(ProfileConstants.SPRING_PROFILE_CLOUD)
@Configuration
public class DatabaseConfiguration extends HikariConfig {

    @Primary
    @Bean("logDataSource")
    public DataSource mainDataSource() {
        return new HikariDataSource(this);
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}

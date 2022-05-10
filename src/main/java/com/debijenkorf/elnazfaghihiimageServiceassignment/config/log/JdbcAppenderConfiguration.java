//package com.debijenkorf.elnazfaghihiimageServiceassignment.config.log;
//
//import com.debijenkorf.elnazfaghihiimageServiceassignment.utils.ProfileConstants;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.core.Appender;
//import org.apache.logging.log4j.core.LoggerContext;
//import org.apache.logging.log4j.core.appender.db.ColumnMapping;
//import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
//import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
//import org.apache.logging.log4j.core.config.LoggerConfig;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Profile;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.stereotype.Component;
//
//
//@Component
//@Profile(ProfileConstants.SPRING_PROFILE_CLOUD)
//public class JdbcAppenderConfiguration implements ApplicationListener<ContextRefreshedEvent> {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcAppenderConfiguration.class);
//    private final ConnectionFactory connectionFactory;
//
//    public JdbcAppenderConfiguration(ConnectionFactory connectionFactory) {
//        this.connectionFactory = connectionFactory;
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
//
//        final ColumnConfig[] columnConfigs = {
//                ColumnConfig.newBuilder().setConfiguration(ctx.getConfiguration()).setName("LOG_LEVEL").setPattern("%level").setUnicode(false).build(),
//                ColumnConfig.newBuilder().setConfiguration(ctx.getConfiguration()).setName("DATE_TIME").setEventTimestamp(true).build(),
//                ColumnConfig.newBuilder().setUnicode(false).setConfiguration(ctx.getConfiguration()).setName("CLASS").setPattern("%logger").build(),
//                ColumnConfig.newBuilder().setUnicode(false).setConfiguration(ctx.getConfiguration()).setName("LEVEL").setPattern("%level").build(),
//                ColumnConfig.newBuilder().setUnicode(false).setConfiguration(ctx.getConfiguration()).setName("MESSAGE").setPattern("%message").build(),
//                ColumnConfig.newBuilder().setUnicode(false).setConfiguration(ctx.getConfiguration()).setName("EXCEPTION").setPattern("%ex{full}").build()
//        };
//
////        AppenderRef.createAppenderRef("databaseAppender", Level.DEBUG);
//
//        final Appender appender = JdbcAppender
//                .newBuilder()
//                .setName("databaseAppender")
//                .setIgnoreExceptions(false)
//                .setConnectionSource(connectionFactory)
////                .setColumnMappings(new ColumnMapping[]{})
//                .setTableName("event_logs")
//                .setColumnConfigs(columnConfigs)
//                .build();
//
//        appender.start();
//
//        ctx.getConfiguration().addAppender(appender);
//
//        final LoggerConfig loggerConfig =
//                ctx.getConfiguration()
//                        .getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
//        loggerConfig.addAppender(appender, null, null);
//
//        ctx.updateLoggers();
//
//    }
//}

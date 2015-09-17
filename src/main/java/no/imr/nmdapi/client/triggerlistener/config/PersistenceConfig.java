package no.imr.nmdapi.client.triggerlistener.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class containing the datasources
 *
 * @author sjurl
 */
@Configuration
public class PersistenceConfig {

    @Autowired
    @Qualifier("persistanceConfig")
    private PropertiesConfiguration configuration;

    /**
     * Datasource bean
     *
     * @return
     */
    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(configuration.getString("jdbc.driver"));
        dataSource.setUrl(configuration.getString("jdbc.url"));
        dataSource.setUsername(configuration.getString("jdbc.user"));
        dataSource.setPassword(configuration.getString("jdbc.password"));

        dataSource.setAccessToUnderlyingConnectionAllowed(true);
        return dataSource;
    }

    /**
     * Connection used for gathering messages. It listens to the correct message
     * queue
     *
     * @return
     * @throws SQLException
     */
    @Bean
    public Connection connection() throws SQLException {
        Connection conn = dataSource().getConnection();
        Statement listenStatement = conn.createStatement();
        listenStatement.execute("LISTEN exporttrigger");
        listenStatement.close();
        return conn;
    }
}

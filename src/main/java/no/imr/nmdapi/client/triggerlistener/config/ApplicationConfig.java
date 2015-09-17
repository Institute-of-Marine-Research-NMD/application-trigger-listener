package no.imr.nmdapi.client.triggerlistener.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the application
 *
 * @author sjurl
 */
@Configuration
public class ApplicationConfig {

    /**
     * Active mq configuration
     *
     * @return
     * @throws ConfigurationException
     */
    @Bean(name = "activeMQConf")
    public PropertiesConfiguration getActiveMQConfiguration() throws ConfigurationException {
        PropertiesConfiguration conf = new PropertiesConfiguration(System.getProperty("catalina.base") + "/conf/activemq.properties");
        conf.setReloadingStrategy(new FileChangedReloadingStrategy());
        return conf;
    }

    /**
     * Persistance configuration
     *
     * @return
     * @throws ConfigurationException
     */
    @Bean(name = "persistanceConfig")
    public PropertiesConfiguration persistanceConfig() throws ConfigurationException {
        PropertiesConfiguration conf = new PropertiesConfiguration(System.getProperty("catalina.base") + "/conf/s2d_connection.properties");
        conf.setReloadingStrategy(new FileChangedReloadingStrategy());
        return conf;
    }
}

package no.imr.nmdapi.client.triggerlistener.config;

import no.imr.nmdapi.client.triggerlistener.route.TriggerListenerRoute;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.SingleRouteCamelConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for camel
 *
 * @author sjurl
 */
@Configuration
public class CamelConfig extends SingleRouteCamelConfiguration implements InitializingBean {

    @Override
    public RouteBuilder route() {
        return new TriggerListenerRoute();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // no properties loaded so not used
    }

}

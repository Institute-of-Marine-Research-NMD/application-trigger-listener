package no.imr.nmdapi.client.triggerlistener.config;

import org.apache.camel.Predicate;
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
        return new RouteBuilder() {

            @Override
            public void configure() {
                Predicate reference = body(String.class).contains("nmdreference");
                Predicate mission = body(String.class).contains("nmdmission");
                Predicate biotic = body(String.class).contains("nmdbiotic");
                Predicate echosounder = body(String.class).contains("nmdechosounder");

                from("timer://harvesttimer?fixedRate=true&period=10000")
                        .errorHandler(deadLetterChannel("jms:queue:dead").maximumRedeliveries(3).redeliveryDelay(30000))
                        .to("triggerListener")
                        .split(body(String.class).tokenize(";"))
                        .choice()
                        .when(reference)
                        .to("jms:queue:export-nmdreference")
                        .when(mission)
                        .to("jms:queue:export-nmdreference")
                        .when(biotic)
                        .to("jms:queue:export-nmdbiotic")
                        .when(echosounder)
                        .to("jms:queue:export-nmdechosounder")
                        .to("log:end?level=INFO");
            }
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // no properties loaded so not used
    }

}

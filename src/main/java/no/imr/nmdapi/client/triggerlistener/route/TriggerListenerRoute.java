package no.imr.nmdapi.client.triggerlistener.route;

import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;

/**
 * Camel route that listens to the databasen and sends any messages to message
 * queue
 *
 * @author sjurl
 */
public class TriggerListenerRoute extends RouteBuilder {

    private static final int REDELIVERY_DELAY = 30000;
    private static final int MAX_REDELIVERIES = 3;

    @Override
    public void configure() throws Exception {
        Predicate reference = body(String.class).contains("nmdreference");
        Predicate mission = body(String.class).contains("nmdmission");
        Predicate biotic = body(String.class).contains("nmdbiotic");
        Predicate echosounder = body(String.class).contains("nmdechosounder");

        from("timer://harvesttimer?fixedRate=true&period=10000")
                .errorHandler(deadLetterChannel("jms:queue:dead").maximumRedeliveries(MAX_REDELIVERIES).redeliveryDelay(REDELIVERY_DELAY))
                .to("triggerListener")
                .split(body(String.class).tokenize(";"))
                .choice()
                .when(reference).to("jms:queue:export-nmdreference")
                .when(mission).to("jms:queue:export-nmdmission")
                .when(biotic).to("jms:queue:export-nmdbiotic")
                .when(echosounder).to("jms:queue:export-nmdechosounder")
                .to("log:end?level=INFO");
    }

}

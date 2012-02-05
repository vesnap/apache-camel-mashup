package de.fhb.morgenthal;

import java.util.HashMap;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import de.fhb.morgenthal.vo.Flight;

public class AirportFlightAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Flight fl = oldExchange.getIn().getBody(Flight.class);
		HashMap<String,String> body = newExchange.getIn().getBody(HashMap.class);
		
		String airport = body.get("to");
		
		if(airport == null)
			fl.setAirport("unknown");
		else if(airport.isEmpty())
			fl.setAirport("unknown");
		else
			fl.setAirport(airport);
		
		return oldExchange;
	}

}

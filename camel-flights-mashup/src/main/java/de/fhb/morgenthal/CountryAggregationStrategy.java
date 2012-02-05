package de.fhb.morgenthal;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import de.fhb.morgenthal.vo.Flight;

public class CountryAggregationStrategy implements AggregationStrategy{

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Flight fl = oldExchange.getIn().getBody(Flight.class);
		String body = newExchange.getIn().getBody(String.class);
		if(body.isEmpty())
			fl.setCountry("");
		else
			fl.setCountry(body);
		return oldExchange;
	}

}

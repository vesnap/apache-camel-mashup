package de.fhb.morgenthal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mail.MailMessage;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.model.SetHeaderDefinition;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.spi.DataFormat;
import org.apache.camel.spring.Main;

import de.fhb.morgenthal.converter.FlightSplitter;
import de.fhb.morgenthal.vo.Flight;

/**
 * A Camel Router
 */
public class MyRouteBuilder extends RouteBuilder {

	public static final String RSSFileDirectory = "flights";
	public static final String RSSFile = RSSFileDirectory + "\\RssItems.txt";
	public static final String RSSHelperDirectory = RSSFileDirectory + "\\Content";
	public static final String RSSTempDirectory = RSSFileDirectory + "\\Temp";
	public static final int PlacesDisplay = 2;
	
    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
   	
        Main.main(args);
    }

    /**
     * Lets configure the Camel routing rules using Java code...
     */
    public void configure() {
    	
    	Predicate isNotfall = new Predicate() {
			
    		private List<String> emergencyCodes = Arrays.asList("7500","7600","7700");
    		
			@Override
			public boolean matches(Exchange exchange) {
				Flight fl = exchange.getIn().getBody(Flight.class); 				
				return emergencyCodes.contains(fl.getSquawk());
			}
		};

		//main route
		from("timer://pollingTimer?fixedRate=true&delay=0&period=600000")//do this action every 10 minutes
    		.to("http://flightradar24.com/PlaneFeed.json")
    		.process(new HandleRssFilesProcessor())
    		.log("get http://flightradar24.com/PlaneFeed.json")
    		.unmarshal().json(JsonLibrary.Jackson)	//convert to JSON   		
    		.split().method(FlightSplitter.class)	//split big message into single flight messages
    		.convertBodyTo(Flight.class)			//convert flight into Flight POJO objects
    		.setHeader("fln", simple("${body.number}"))
    		.setHeader("myID", simple("${id}"))
    		.multicast().to("direct:sendMailIfEmergency","direct:progress");
 
    	//send email in case of emergency
    	from("direct:sendMailIfEmergency")
    		.choice().when(isNotfall)
    			.log("send emergency mail")
				.process(new MailProcessor())
				.to("smtps://camelfhb@smtp.gmail.com?password=camelfhb")
			.end();
    	
    	//Prozess Route
    	from("direct:progress")
	    	//put country information to flight
			.enrich("direct:countryEnricher", new CountryAggregationStrategy()).log("${body.country} ${body.number} ${body.squawk}")
			.choice().when(simple("${body.country} == 'DE' || ${body.country} == 'AT' || ${body.country} == 'CH'"))
				.log("In D-A-CH ${body.number}").multicast().parallelProcessing().to("direct:weatherChannel", "direct:airportChannel","direct:placesChannel")
			.end();
    	
    	//route to add weather information to message
    	from("direct:weatherChannel")
    		.enrich("direct:weatherEnricher", new WeatherFlightAggregationStrategy())
    		.to("direct:aggregator");
    	
    	//route to add destination airport to message
    	from("direct:airportChannel")
			.enrich("direct:airportEnricher", new AirportFlightAggregationStrategy())
			.to("direct:aggregator");

    	//route to add points of interest near plane position to message
    	from("direct:placesChannel")
			.enrich("direct:placesEnricher", new PlacesAggregationStrategy())
			.to("direct:aggregator");

    	//aggregates parallel processed and splited flights into rss files  
		from("direct:aggregator")
			.aggregate(header("fln"),new FlightAggregator()).completionSize(3)
			.setHeader("CamelFileName", simple("\\${body.country}\\${header.myID}"))
			.log("Aggregated ${header.fln}").marshal().rss()
			.transform().xpath("/rss/channel/item")
			.to("file://" + MyRouteBuilder.RSSTempDirectory + "?fileExist=Append");

    	//enrich flight with country information
    	from("direct:countryEnricher")
	    	.setHeader(Exchange.HTTP_QUERY, simple("latlng=${body.latidude},${body.longitude}&sensor=false"))
	    	.setBody().simple("1")
	    	.to("http://maps.google.com/maps/api/geocode/xml")
	    	.transform().xpath("(//type[text() = 'country']/./../short_name/text())[1]");
    	
    	//enrich flight with weather information
    	from("direct:weatherEnricher")
			.setHeader(Exchange.HTTP_QUERY, simple("q=${body.latidude},${body.longitude}&format=xml&key=YOUR-KEY-HERE"))
			.setBody().simple("1")
			.to("http://free.worldweatheronline.com/feed/weather.ashx")
			.transform().xpath("(//current_condition)[1]");
    	
    	//enrich flight with destination airport information
    	from("direct:airportEnricher")
			.setHeader(Exchange.HTTP_QUERY, simple("callsign=${body.number}&hex=${body.hexcode}&date=${body.unixTimestamp}"))
			.setBody().simple("1")
			.to("http://www.flightradar24.com/FlightDataService.php")
			.unmarshal().json(JsonLibrary.Jackson);

    	//enrich points of interests on plane position
    	from("direct:placesEnricher")
			.setHeader(Exchange.HTTP_QUERY, simple("location=${body.latidude},${body.longitude}&radius=500&sensor=false&key=YOUR-KEY-HERE"))
			.log("POI near by ${body.latidude},${body.longitude}")
			.setBody().simple("1")
			.to("https://maps.googleapis.com/maps/api/place/search/xml")
			.transform().xpath("(/PlaceSearchResponse/result)[position()<="+PlacesDisplay+"]");

    }
}



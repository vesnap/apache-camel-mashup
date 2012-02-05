package de.fhb.morgenthal;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import java.io.StringReader;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import de.fhb.morgenthal.vo.Flight;

public class WeatherFlightAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Flight fl = oldExchange.getIn().getBody(Flight.class);
		String body = newExchange.getIn().getBody(String.class);
		
		try{ 
			
			DocumentBuilderFactory factoryBuilder = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factoryBuilder.newDocumentBuilder();
	        Document doc = builder.parse(new InputSource(new StringReader(body)));

		    XPathFactory factory = XPathFactory.newInstance();
		    XPath xpath = factory.newXPath();

		    fl.setWeather_Degree( getStringFromXPath(doc, xpath,"/current_condition/temp_C/text()") );
		    fl.setWeatherIconUrl( getStringFromXPath(doc, xpath,"/current_condition/weatherIconUrl/text()") );
		    fl.setWindspeedKmph( getStringFromXPath(doc, xpath,"/current_condition/windspeedKmph/text()") );
		    fl.setWinddir16Point( getStringFromXPath(doc, xpath,"/current_condition/winddir16Point/text()") );
		    fl.setWeatherDesc( getStringFromXPath(doc, xpath,"/current_condition/weatherDesc/text()") );
		    fl.setHumidity( getStringFromXPath(doc, xpath,"/current_condition/humidity/text()") );
		    fl.setVisibility( getStringFromXPath(doc, xpath,"/current_condition/visibility/text()") );
		    fl.setPressure( getStringFromXPath(doc, xpath,"/current_condition/pressure/text()") );
		    fl.setCloudcover( getStringFromXPath(doc, xpath,"/current_condition/cloudcover/text()") );
		    fl.setPrecipMM( getStringFromXPath(doc, xpath,"/current_condition/precipMM/text()") );
		    
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		fl.setWeather(body);
		
		return oldExchange;
	}

	private String getStringFromXPath(Document doc, XPath xpath, String expression) throws Exception{
		XPathExpression expr = xpath.compile(expression);
		return expr.evaluate(doc, XPathConstants.STRING).toString();
	}

}

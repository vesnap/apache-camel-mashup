package de.fhb.morgenthal;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.log4j.Logger;

import java.io.StringReader;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;
import javax.xml.xpath.*;

import de.fhb.morgenthal.vo.Flight;

public class PlacesAggregationStrategy implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		Flight fl = oldExchange.getIn().getBody(Flight.class);

		String body = "<PlaceSearchResponse>"+newExchange.getIn().getBody(String.class)+"</PlaceSearchResponse>";
        String output=" ";
        int count=0;

		try{ 
			DocumentBuilderFactory factoryBuilder = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factoryBuilder.newDocumentBuilder();
	        
	        Document doc = builder.parse(new InputSource(new StringReader(body)));
	        NodeList nodes = doc.getElementsByTagName("result");
				
	        if(nodes.getLength()>MyRouteBuilder.PlacesDisplay)
	        	count=MyRouteBuilder.PlacesDisplay;
	        else
	        	count=nodes.getLength();
	        // iterate the places
	        for (int i = 0; i < count; i++) {
	           Element element = (Element) nodes.item(i);
	           NodeList Nname = element.getElementsByTagName("name");
	           Element name = (Element) Nname.item(0);
	           String Sname="";
    		   Sname=getCharacterDataFromElement(name);
    		   NodeList Nvicinity = element.getElementsByTagName("vicinity");
	           Element vicinity = (Element) Nvicinity.item(0);
	           String Svicinity="";
    		   Svicinity=getCharacterDataFromElement(vicinity);
	           NodeList Nicon = element.getElementsByTagName("icon");
	           Element icon = (Element) Nicon.item(0);
	           String Sicon="";
	           Sicon=getCharacterDataFromElement(icon);
	           
	           output=output+ "<br/><img src=\""+Sicon+"\">"+Sname+"in "+Svicinity;
	        }
	        
		}catch (Exception e) {
			Logger logger=Logger.getLogger("com.foo");
			logger.info("XML uncompleted");
		}

		fl.setPlaces(output.replaceAll("ü", "ue").replaceAll("ä", "ae").replaceAll("ö","oe").replaceAll("ß", "ss").replaceAll("Ü", "Ue").replaceAll("Ä", "Ae").replaceAll("Ö","Oe"));

		return oldExchange;
	}
	  public static String getCharacterDataFromElement(Element e) {
		    Node child = e.getFirstChild();
		    if (child instanceof CharacterData) {
		       CharacterData cd = (CharacterData) child;
		       return cd.getData();
		    }
		    return "?";
	  }
}

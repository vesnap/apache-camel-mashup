package de.fhb.morgenthal.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.camel.Converter;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedOutput;

import de.fhb.morgenthal.vo.Flight;

@Converter
public class FlightSplitter {
	
	
	public List<ArrayList<Object>> split(HashMap<String, ArrayList<Object>> body) {
		
        List<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
        
        Set<Entry<String, ArrayList<Object>>> set = body.entrySet();
        
        for (Entry<String, ArrayList<Object>> item : set) {
        	item.getValue().add(item.getKey());
            result.add(item.getValue());
        }
        return result;
    }
	
	@Converter
	public com.sun.syndication.feed.synd.SyndFeed toSyndFeed(Flight item){
		

          SyndFeed feed = new SyndFeedImpl();
          feed.setTitle("leer");
          feed.setDescription("leer");
          feed.setAuthor("leer");
          feed.setLink("http://www.example.org/");
          feed.setPublishedDate(new Date());
          feed.setFeedType("rss_2.0");
          
          SyndEntry entry = new SyndEntryImpl();
          entry.setTitle(item.getFlugzeugID());

          
          SyndContent description = new SyndContentImpl();
          description.setValue(
        		  "Flugzeug mit ID "+item.getFlugzeugID()+
        		  " vom Typ "+item.getPlaneType()+
        		  " befindet sich momentan in "+item.getCountry()+
        		  ".<br/>Aktuelle Position: <a href=\"http://maps.google.com/maps?z=6&q=" + item.getLatidude() + "+" + item.getLongitude() + "\">"+item.getLatidude()+", "+item.getLongitude()+"</a>"+
        		  ".<br/>Zielflughafen: "+ item.getAirport() +
        		  ".<br/>Aktuelle Geschwindigkeit: "+item.getSpeed()+" MPH"+
        		  ".<br/>Aktueller Squawk Code: "+item.getSquawk()+
        		  ".<br/><br/>Wetterinformation an Flugzeugposition:"+
        		  "<br/>" + item.getWeather_Degree()+" Grad Celsius " + 
        		  "<br/>"+"<img src=\"" + item.getWeatherIconUrl() + "\">" + item.getWeatherDesc() +
	      		  "<br/>Windgeschwindigkeit " + item.getWindspeedKmph() +
	      		  "<br/>Windrichtung " + item.getWinddir16Point() +
	      		  "<br/>Luftdruck " + item.getPressure() +
	      		  "<br/>Luftfeuchtigkeit " + item.getHumidity() + 
	      		  "<br/>POI an Flugzeugposition:"+item.getPlaces()+
	      		  "<br/><br/>"
		      		.replaceAll("ü", "ue").replaceAll("ä", "ae").replaceAll("ö","oe").replaceAll("ß", "ss").replaceAll("Ü", "Ue").replaceAll("Ä", "Ae").replaceAll("Ö","Oe").replaceAll("é","e").replaceAll("è","e").replaceAll("â","a").replaceAll("ô","o").replaceAll("û","u").replaceAll("ê","e").replaceAll("î","i")
        		  );
          description.setType("text");
          
          entry.setDescription(description);
          entry.setAuthor("Andre Morgenthal und Frank Sonnabend");
          
          List<SyndEntry> ll = new ArrayList<SyndEntry>();
          ll.add(entry);

          feed.setEntries(ll);

	    SyndFeedOutput output = new SyndFeedOutput();

	    String xmlFeed = null;
	    try { 
	    	xmlFeed = output.outputString(feed); 
	    }
	    catch (FeedException e) { 
	    	e.printStackTrace(); 
	    }
		
        return feed;
	}
	
	@Converter
	public Flight toFlight(ArrayList<Object> item){
		Flight newFlight = new Flight();
        newFlight.setPayload(item);
        return newFlight;
	}

}

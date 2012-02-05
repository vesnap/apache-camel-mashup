package de.fhb.morgenthal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
//
import org.apache.camel.Message;

public class RSSRouteBuilder extends RouteBuilder{

	
	 public void configure() {
         
		 String name = RestServiceImpl.class.getName();

         from("cxfrs://http://localhost:9000?resourceClasses=" + name).process(new Processor() {
             public void process(Exchange exchange) throws Exception {
            	 //aufgerufende URL auslesen
            	 Message inMessage = exchange.getIn();
            	 String path = inMessage.getHeader(Exchange.HTTP_PATH, String.class);
            	 String land[] = path.split("/rss-feed/");
            	 
            	 //nehme RSS Datei die gerade aufgebaut wird
        		 File latest = RSSFilesUtils.getLatestRssFile(land[1]);
        		 
        		 if(latest != null)
        			 setRssFromFile(exchange, latest);
        		 else
        			 exchange.getOut().setBody("Noch keine Flüge vorhanden.");         
             }

			private void setRssFromFile(Exchange exchange, File latest)
					throws Exception {
				StringBuffer sb = new StringBuffer();
				
				readFile(sb, new File(MyRouteBuilder.RSSHelperDirectory+"\\Header.txt"));
				readFile(sb, latest);
				readFile(sb, new File(MyRouteBuilder.RSSHelperDirectory+"\\Footer.txt"));
 
				exchange.getOut().setBody(sb.toString());
			}

			private void readFile(StringBuffer sb, File file) throws Exception{
				BufferedReader in = new BufferedReader( new FileReader(file));

        		String line = "";
        		
     			while ((line = in.readLine()) != null) {
     				sb.append(line);
     			}
     				
     			in.close();

			}
         });
	 }
}
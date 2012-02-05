package de.fhb.morgenthal;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.mail.MailMessage;

import de.fhb.morgenthal.vo.Flight;

public class MailProcessor implements Processor{
	@Override
	public void process(Exchange exchange) throws Exception {
		
		Flight fl = exchange.getIn().getBody(Flight.class);
						
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("contentType", "text/html");
		map.put("To", "si@thomas-preuss.de" );
		map.put("Subject", "Flugzeug Notfall");
		
		String emailBody = "<b>Flugzeug Notfall</b><br/>";				
		emailBody += "Flug "+fl.getNumber()+" Notfall. Code = "+fl.getSquawk()+".";
		
		MailMessage mm = new MailMessage();
		mm.setBody(emailBody);
		mm.setHeaders(map);
		
		exchange.setIn(mm);
	}
}

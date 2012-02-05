package de.fhb.morgenthal;

import java.io.File;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;

public class HandleRssFilesProcessor implements Processor{

	@Override
	public void process(Exchange exchange) throws Exception {
		File latestCH = RSSFilesUtils.getLatestRssFile("CH");
		File latestDE = RSSFilesUtils.getLatestRssFile("DE");
		File latestAT = RSSFilesUtils.getLatestRssFile("AT");
		File rssFile = new File(MyRouteBuilder.RSSFile);
		if(rssFile!=null && rssFile.exists())
			rssFile.delete();
		if(latestCH!= null && latestCH.exists())
			latestCH.delete();
		if(latestDE!= null && latestDE.exists())
			latestDE.delete();
		if(latestAT!= null && latestAT.exists())
			latestAT.delete();
	}

}

package forge.user.scraper.links;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import forge.user.scraper.DocumentInterpreter;

public class LinkExplorer extends DocumentInterpreter {

    private HttpClient client;
    private URL mainPage;
    private String linkPattern;
    private LinkListener listener;
//    private List<NameValuePair> cookies;
    
    
    /**
     * @param client
     * @param mainPage
     * @param linkPattern
     * @param listener
     * @param cookies
     */
    public LinkExplorer(HttpClient client, URL mainPage, String linkPattern,
	    LinkListener listener
//	    , List<NameValuePair> cookies
	    ) {
	super();
	this.client = client;
	this.mainPage = mainPage;
	this.linkPattern = linkPattern;
	this.listener = listener;
//	this.cookies = cookies;
    }
    
    public void explore(){
	HttpGet httpGet = new HttpGet(mainPage.toString());
	 try {
	     HttpResponse response1 = client.execute(httpGet);
	     HttpEntity entity1 = response1.getEntity();
	     this.interpret(entity1.getContent());
	     EntityUtils.consume(entity1);
	 } catch (IOException e) {
	     e.printStackTrace();
	} finally {
	     httpGet.releaseConnection();
	 }	
    }

    @Override
    protected void business(Node item) {
	listener.LinkFound(mainPage.getProtocol() + "://" + mainPage.getHost() + ((Element)item).getAttribute("href"));
    }

    @Override
    protected boolean isThisNodeYoureLookingFor(Node item) {
	return item.getNodeName().equalsIgnoreCase("a")
		&& ((Element)item).getAttribute("href").matches(linkPattern);
    }

}

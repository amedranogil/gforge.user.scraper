package forge.user.scraper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.HttpClient;

import forge.user.scraper.links.LinkExplorer;
import forge.user.scraper.links.LinkListener;

public class ProjectExplorer implements LinkListener {
    
    private HttpClient client;
    private Set<String> visited;
    private LinkListener userLinkListener;
    
    /**
     * @param client
     */
    public ProjectExplorer(HttpClient client, LinkListener userLinkListener) {
	super();
	this.client = client;
	this.userLinkListener = userLinkListener;
	visited = new HashSet<String>();
    }

    public void LinkFound(String link) {
	if(!visited.contains(link)){
	 visited.add(link);
//	 System.out.println("project: " + link);
	 explore(link);
	}
    }

    public void explore(String link){
	try {
	    LinkExplorer le = new LinkExplorer(client, new URL(link), "/gf/user/(\\w|\\.)*/$", userLinkListener);
	    le.explore();
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}

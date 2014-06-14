package forge.user.scraper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import forge.user.scraper.links.LinkExplorer;
import forge.user.scraper.links.LinkListener;

public class Crawler {

    /**
     * @param args
     */
    public static void main(String[] args) {
	try {
	    HttpClient c = new DefaultHttpClient();
	    URL li = new URL(
		    args[0] + "account/?action=Login&redirect=%2Fgf%2F%3F");
	    SignIn s = new SignIn(c, li, args[1], args[2]);
	    s.login();

	    UserExtractor perUser = new UserExtractor(c);
	    
	    LinkListener perProject = new  ProjectExplorer(c, perUser);
	    
	    LinkExplorer le = new LinkExplorer(c, new URL(
		    "http://forge.universaal.org/gf/project/"),
		    "/gf/project/\\w*/$", perProject);
	    le.explore();

	    perUser.toFile("users.txt");
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}

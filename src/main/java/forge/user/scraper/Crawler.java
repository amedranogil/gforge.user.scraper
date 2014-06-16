package forge.user.scraper;

import java.net.MalformedURLException;
import java.net.URL;

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
		
		String url;
		String username;
		String password;
		String output = "users.txt";
		if (args.length >= 4){
			output = args[3];
		}
		if (args.length >= 3) {
			url = args[0];
			username = args[1];
			password = args[2];
		}
		else {
			System.out.println("Usage: Crawler http://server/gforge username password [outputfile]");
			return;
		}
			
	
		
	    HttpClient c = new DefaultHttpClient();
	    URL li = new URL(
		    url + "account/?action=Login&redirect=%2Fgf%2F%3F");
	    SignIn s = new SignIn(c, li, username, password);
	    s.login();

	    UserExtractor perUser = new UserExtractor(c);
	    
	    LinkListener perProject = new  ProjectExplorer(c, perUser);
	    
	    LinkExplorer le = new LinkExplorer(c, new URL(
		    url + "project/"),
		    "/gf/project/\\w*/$", perProject);
	    le.explore();

	    perUser.toFile(output);
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}

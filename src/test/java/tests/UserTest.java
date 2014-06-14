package tests;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import forge.user.scraper.SignIn;
import forge.user.scraper.UserExtractor;

public class UserTest {
    
    String url = "http://forge.universaal.org/gf/";
    String user = "";
    String password = "";
    
    
    @Test
    public void test() {
	try {
	    HttpClient c = new DefaultHttpClient();
	    URL li = new URL(url + "account/?action=Login&redirect=%2Fgf%2F%3F");
	    SignIn s = new SignIn(c, li, user, password);
	    s.login();
	    
	   String userURL = "http://forge.universaal.org/gf/user/alfiva/";
	   
	   UserExtractor ue = new UserExtractor(c);
	    
	   ue.LinkFound(userURL);
	    
	    
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    
    
}

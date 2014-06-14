package tests;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import forge.user.scraper.PageGetter;
import forge.user.scraper.SignIn;

public class SignInTest {

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
	    
	   PageGetter.get(c, "http://forge.universaal.org/gf/project/", "target/projects.html");
	    
	    
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    
    
}

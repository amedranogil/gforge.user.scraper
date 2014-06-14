package forge.user.scraper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SignIn extends DocumentInterpreter {

    private HttpClient client;
    private URL loginPage;
    private String user;
    private String password;
    private List<NameValuePair> resendPairs;
    
    
    
    /**
     * @param client
     * @param loginPage
     * @param user
     * @param password
     */
    public SignIn(HttpClient client, URL loginPage, String user, String password) {
	super();
	this.client = client;
	this.loginPage = loginPage;
	this.user = user;
	this.password = password;
	
    }
    
    public void login(){
	HttpGet httpGet = new HttpGet(loginPage.toString());
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

    public List<NameValuePair> getCookies(){
	return resendPairs;
    }
    
    private void post(String url, List<NameValuePair> namePair){
	HttpPost post = new HttpPost(loginPage.getProtocol() + "://" + loginPage.getHost() + url);
	try {
	    post.setEntity(new UrlEncodedFormEntity(namePair));

	    HttpResponse response = client.execute(post);
	    
	    //System.out.println("Initial set of cookies:");
	    List<Cookie> cookies = ((DefaultHttpClient)client).getCookieStore().getCookies();
	    resendPairs = new ArrayList<NameValuePair>();
	    if (!cookies.isEmpty()) {
	        for (int i = 0; i < cookies.size(); i++) {
	            //System.out.println("- " + cookies.get(i).toString());
	            resendPairs.add(new BasicNameValuePair(
	        	    cookies.get(i).getName(), 
	        	    cookies.get(i).getValue()));
	        }
	    }
	    
//	    IOUtils.copy(response.getEntity().getContent(),
//		    new FileOutputStream(new File("post.htm")));
	    
	} catch (IOException e) {
	     e.printStackTrace();
	}
    }
    
    @Override
    protected void business(Node item) {
	LogInFormInterpreter ni = new LogInFormInterpreter();
	ni.iterate(item.getParentNode().getChildNodes());
	post(((Element)item).getAttribute("action"),ni.nameValuePairs);
    }

    @Override
    protected boolean isThisNodeYoureLookingFor(Node item) {
	return item.getNodeName().equalsIgnoreCase("form")
		&& ((Element)item).getAttribute("action").contains("LoginAction");
    }
    
    class LogInFormInterpreter extends NodeInterpreter{
        List<NameValuePair> nameValuePairs;
        
        public LogInFormInterpreter() {
            nameValuePairs = new ArrayList<NameValuePair>();
        }
        
        @Override
        protected void business(Node item) {
            String name = ((Element) item).getAttribute("name");
            if (name.equals("username")){
        	nameValuePairs.add(new BasicNameValuePair(name, user));
            }
            else if (name.equals("password")){
        	nameValuePairs.add(new BasicNameValuePair(name, password));
            }
            else{
        	nameValuePairs.add(new BasicNameValuePair(name, ((Element) item).getAttribute("value")));
            }
        }
    
        @Override
        protected boolean isThisNodeYoureLookingFor(Node item) {
            return item.getNodeName().equalsIgnoreCase("input")
        	    || item.getNodeName().equalsIgnoreCase("submit");
        }
        
    }
}

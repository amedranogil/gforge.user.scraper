package forge.user.scraper;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import forge.user.scraper.links.LinkListener;

public class UserExtractor implements LinkListener {

    private class User {
	String login;
	String uName;
	String email;
    }

    private class UserInterpreter extends DocumentInterpreter {

	User u = new User();

	User getUser(String link) {
	    HttpGet httpGet = new HttpGet(link);
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
	    return u;
	}

	@Override
	protected void business(Node item) {
	    NodeList cn = item.getChildNodes();
	    if (cn.getLength() == 2
		    && cn.item(0).getNodeName().equalsIgnoreCase("td")) {
		String first = cn.item(0).getFirstChild().getNodeValue();
		if (first.equalsIgnoreCase("Login")) {
		    u.login = getRawText(cn.item(1));
		}
		if (first.equalsIgnoreCase("User Name")) {
		    u.uName = getRawText(cn.item(1));
		}
		if (first.equalsIgnoreCase("Email:")) {
		    u.email = getRawText(cn.item(1));
		}
	    }
	}

	private String getRawText(Node item) {
	    String value = item.getNodeValue();
	    // System.out.println(value);
	    NodeList cn = item.getChildNodes();
	    if (cn.getLength() == 0) {
		return value;
	    } else {
		return getRawText(cn.item(0));
	    }
	}

	@Override
	protected boolean isThisNodeYoureLookingFor(Node item) {
	    return item.getNodeName().equalsIgnoreCase("tr");
	}
    }

    private HttpClient client;
    private Map<String, User> visited;
    private List<Thread> threads;

    /**
     * @param client
     */
    public UserExtractor(HttpClient client) {
	super();
	this.client = client;
	visited = new TreeMap<String, UserExtractor.User>();
	threads = new ArrayList<Thread>();
    }

    public void LinkFound(final String link) {
	if (!visited.containsKey(link)) {
	    Thread t = new Thread(new Runnable() {
	        
	        public void run() {
	            User u = new UserInterpreter().getUser(link);
	            synchronized (visited) {
			visited.put(link, u);
		    }
	        }
	    }, link);
	    t.start();
	    threads.add(t);
	}
    }

    public void toFile(String filename) {
	
	for (Thread t : threads) {
	    try {
		t.join();
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	
	try {
	    PrintStream ps = new PrintStream(new File(filename), "UTF-8");
	    for (User u : visited.values()) {
		ps.println(u.login + " = " + u.uName + " <"
			+ u.email.replace(" @nospam@ ", "@") + ">");
	    }
	    ps.close();
	} catch (IOException e) {
	    e.printStackTrace();
	}

    }

}

package forge.user.scraper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

public class PageGetter {

    public static void get(HttpClient client, String url, String file) {
	HttpGet httpGet = new HttpGet(url);
	try {
	    HttpResponse response1 = client.execute(httpGet);
	    HttpEntity entity1 = response1.getEntity();

	    IOUtils.copy(response1.getEntity().getContent(),
		    new FileOutputStream(new File(file)));

	    EntityUtils.consume(entity1);
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    httpGet.releaseConnection();
	}
    }

}

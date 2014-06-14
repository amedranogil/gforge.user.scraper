package forge.user.scraper;

import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

public abstract class DocumentInterpreter  extends NodeInterpreter {
    
    public void interpret(InputStream is){
	    Tidy t = new Tidy();
	    t.setShowWarnings(false);
	    t.setQuiet(true);
	    t.setCharEncoding(org.w3c.tidy.Configuration.UTF8);
	    Document document = t.parseDOM(is, null);
	    
	    NodeList nl = document.getChildNodes();
	    iterate(nl); 	
    }
}
    

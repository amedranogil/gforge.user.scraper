package forge.user.scraper;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class NodeInterpreter {
    
    protected void iterate(NodeList nodes){
	for (int i = 0; i < nodes.getLength(); i++) {
	    Node n = nodes.item(i);
	    if (isThisNodeYoureLookingFor(n)){
		business(n);
	    }
	    iterate(n.getChildNodes());
	}
    }


    protected abstract void business(Node item);


    protected abstract boolean isThisNodeYoureLookingFor(Node item);
}

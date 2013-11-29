package rdf.serializer;

/**
 * Basic class to generator URIs.  Eventually should be replaced by a better factory that takes 
 * in an ID as a parameter.  URI factories created in different JREs should use different IDs.
 * 
 * @author alex
 *
 */


public final class URIFactory {

    private static final URIFactory INSTANCE = new URIFactory();
    int count = 0;

    private URIFactory() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }
    }

    public static URIFactory getInstance() {
        return INSTANCE;
    }
    
    public String generateURI() {
    	count++;
    	return "ID_" + count;
    }
}

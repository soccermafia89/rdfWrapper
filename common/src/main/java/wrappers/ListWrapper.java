/*
 */

package wrappers;

import com.hp.hpl.jena.rdf.model.*;
import java.io.InputStream;

/**

*/

public class ListWrapper{

    protected static final String uri = "http://ethier.alex.com/List#";

    /** returns the URI for this schema
        @return the URI for this schema
    */
    public static String getURI()
        { return uri; }

    protected static final Resource resource( String local )
        { return ResourceFactory.createResource( uri + local ); }

    protected static final Property property( String local )
        { return ResourceFactory.createProperty( uri, local ); }
    
	public static final Resource listNode = resource( "listNode" );
	public static final Resource index = resource( "index" );

	public static final Property hasIndex = property( "hasIndex" );
	public static final Property hasNext = property( "hasNext" );
	public static final Property hasPrevious = property( "hasPrevious" );
	
	//Ontology Relationships
	public static final Model model = getModel();
	
	protected static Model getModel() {
		Model myModel = ModelFactory.createDefaultModel();
		InputStream inputStream = ClassLoader.getSystemResourceAsStream("Collection.owl");

		myModel.read(inputStream, null, "RDF/XML");
		
		return myModel;		
	}
}

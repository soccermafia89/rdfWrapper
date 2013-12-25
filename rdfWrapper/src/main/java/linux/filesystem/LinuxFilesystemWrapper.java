/*
 */

package linux.filesystem;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;

/**

*/

public class LinuxFilesystemWrapper{

    protected static final String uri = "http://ethier.alex.com/LinuxFilesystem#";

    /** returns the URI for this schema
        @return the URI for this schema
    */
    public static String getURI()
        { return uri; }

    protected static final Resource resource( String local )
        { return ResourceFactory.createResource( uri + local ); }

    protected static final Property property( String local )
        { return ResourceFactory.createProperty( uri, local ); }
    
	public static final Resource content = resource( "content" );
	public static final Resource filename = resource( "filename" );
	public static final Resource path = resource( "path" );
	
	//Resources defined by properties
	public static final Resource node = resource( "node" );
	public static final Resource directory = resource( "directory" );
	public static final Resource file = resource( "file" );
	
	public static final Property hasChildren = property( "hasChildren" );
	public static final Property hasContent = property( "hasContent" );
	public static final Property hasFilename = property( "hasFilename" );
	public static final Property hasParentDirectory = property( "hasParentDirectory" );
	public static final Property hasPath = property( "hasPath" );
	
	//Ontology Relationships
	public static final Model model = getModel();
	
	protected static Model getModel() {
		Model model = ModelFactory.createDefaultModel();
		InputStream inputStream = ClassLoader.getSystemResourceAsStream("LinuxFilesystem.owl");

		model = ModelFactory.createDefaultModel();
		model.read(inputStream, null, "RDF/XML");
		
		return model;		
	}
}

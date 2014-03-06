package rdf.serializer;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;


public class RDFSerializerWrapper {

	protected static final String uri = "http://ethier.alex.com/RDFSerializer#";

	/**
	 * returns the URI for this schema
	 * 
	 * @return the URI for this schema
	 */
	public static String getURI()
	{
		return uri;
	}

	protected static final Resource resource(String local)
	{
		return ResourceFactory.createResource(uri + local);
	}

	protected static final Property property(String local)
	{
		return ResourceFactory.createProperty(uri, local);
	}

        //A hash uniquely identifies a node in a graph.
        public static final Resource hash = resource("hash");
        
        // If one resouce is linked with another, they are the same resource and the two nodes need to be joined into one.
	public static final Property isLinkedWith = property("isLinkedWith");
        
        //Used to link any node with a resource of type hash.
        public static final Property hasHash = property("hasHash");
        
	//Ontology Relationships
	public static final Model model = getModel();

	protected static Model getModel() {
		Model model = ModelFactory.createDefaultModel();
//		InputStream inputStream = ClassLoader.getSystemResourceAsStream("StringContent.owl");
//
//		model = ModelFactory.createDefaultModel();
//		model.read(inputStream, null, "RDF/XML");

		return model;
	}
}

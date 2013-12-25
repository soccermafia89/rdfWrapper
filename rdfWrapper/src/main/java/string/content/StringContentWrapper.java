package string.content;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Property;


public class StringContentWrapper {

	protected static final String uri = "http://ethier.alex.com/StringContent#";

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

	//Resources defined by properties
	public static final Resource character = resource("character");
	public static final Resource content = resource("content");
	public static final Resource line = resource("line");
	public static final Resource phrase = resource("phrase");
	public static final Resource word = resource("word");

	public static final Property hasCharacter = property("hasCharacter");
	public static final Property hasContent = property("hasContent");
	public static final Property hasContentOffset = property("hasContentOffset");
	public static final Property hasLine = property("hasLine");
	public static final Property hasLineOffset = property("hasLineOffset");
	public static final Property hasPhrase = property("hasPhrase");
	public static final Property hasSource = property("hasSource");
	public static final Property hasWord = property("hasWord");
	public static final Property hasWordOffset = property("hasWordOffset");

	//Ontology Relationships
	public static final Model model = getModel();

	protected static Model getModel() {
		Model model = ModelFactory.createDefaultModel();
		InputStream inputStream = ClassLoader.getSystemResourceAsStream("StringContent.owl");

		model = ModelFactory.createDefaultModel();
		model.read(inputStream, null, "RDF/XML");

		return model;
	}
}

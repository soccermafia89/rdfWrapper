package string.content;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class StringContentWrapper {
	
	//Resources defined by properties
	private static Resource character;
	private static Resource content;
	private static Resource line;
	private static Resource phrase;
	private static Resource word;
	
	private static Property hasCharacter;
	private static Property hasContent;
	private static Property hasContentOffset;
	private static Property hasLine;
	private static Property hasLineOffset;
	private static Property hasPhrase;
	private static Property hasSource;
	private static Property hasWord;
	private static Property hasWordOffset;
	
	static {
		InputStream inputStream = ClassLoader.getSystemResourceAsStream("StringContent.owl");

		Model model = ModelFactory.createDefaultModel();
		model.read(inputStream, null, "RDF/XML");
		
		String prefix = "http://ethier.alex.com/StringContent#";
		
		character = model.getResource(prefix + "character");
		content = model.getResource(prefix + "content");
		line = model.getResource(prefix + "line");
		phrase = model.getResource(prefix + "phrase");
		word = model.getResource(prefix + "word");
		
		hasCharacter = model.getProperty(prefix + "hasCharacter");
		hasContent = model.getProperty(prefix + "hasContent");
		hasContentOffset = model.getProperty(prefix + "hasContentOffset");
		hasLine = model.getProperty(prefix + "hasLine");
		hasLineOffset = model.getProperty(prefix + "hasLineOffset");
		hasPhrase = model.getProperty(prefix + "hasPhrase");
		hasSource = model.getProperty(prefix + "hasSource");
		hasWord = model.getProperty(prefix + "hasWord");
		hasWordOffset = model.getProperty(prefix + "hasWordOffset");
	}

	public static Resource characterResource() {
		return character;
	}

	public static Resource contentResource() {
		return content;
	}

	public static Resource lineResource() {
		return line;
	}

	public static Resource phraseResource() {
		return phrase;
	}

	public static Resource wordResource() {
		return word;
	}

	public static Property hasCharacterProperty() {
		return hasCharacter;
	}

	public static Property hasContentResource() {
		return hasContent;
	}

	public static Property hasContentOffsetProperty() {
		return hasContentOffset;
	}

	public static Property hasLineProperty() {
		return hasLine;
	}

	public static Property hasLineOffsetProperty() {
		return hasLineOffset;
	}

	public static Property hasPhraseProperty() {
		return hasPhrase;
	}

	public static Property hasSourceProperty() {
		return hasSource;
	}

	public static Property hasWordProperty() {
		return hasWord;
	}

	public static Property hasWordOffsetProperty() {
		return hasWordOffset;
	}
}

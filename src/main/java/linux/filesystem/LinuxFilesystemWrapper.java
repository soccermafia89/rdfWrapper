package linux.filesystem;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;


public class LinuxFilesystemWrapper {
	private static Model model;
	
	private static Resource content;
	private static Resource filename;
	private static Resource path;
	
	//Resources defined by properties
	private static Resource node;
	private static Resource directory;
	private static Resource file;
	
	private static Property hasChildren;
	private static Property hasContent;
	private static Property hasFilename;
	private static Property hasParentDirectory;
	private static Property hasPath;
	
	static {
		InputStream inputStream = ClassLoader.getSystemResourceAsStream("LinuxFilesystem.owl");

		model = ModelFactory.createDefaultModel();
		model.read(inputStream, null, "RDF/XML");
		
		String prefix = "http://ethier.alex.com/LinuxFilesystem#";
		
		content = model.getResource(prefix + "Content");
		filename = model.getResource(prefix + "Filename");
		path = model.getResource(prefix + "Path");
		node = model.getResource(prefix + "Node");
		directory = model.getResource(prefix + "Directory");
		file = model.getResource(prefix + "File");
		
		hasChildren = model.getProperty(prefix + "hasChildren");
		hasContent = model.getProperty(prefix + "hasContent");
		hasFilename = model.getProperty(prefix + "hasFilename");
		hasParentDirectory = model.getProperty(prefix + "hasParentDirectory");
		hasPath = model.getProperty(prefix + "hasPath");
	}
	
	public static Model getModel() {
		return model;
	}
	
	public static Resource contentResource() {
		return content;
	}
	
	public static Resource filenameResource() {
		return filename;
	}
	
	public static Resource pathResource() {
		return path;
	}
	
	public static Resource nodeResource() {
		return node;
	}
	
	public static Resource directoryResource() {
		return directory;
	}
	
	public static Resource fileResource() {
		return file;
	}
	
	public static Property hasChildrenProperty() {
		return hasChildren;
	}
	
	public static Property hasContentProperty() {
		return hasContent;
	}
	
	public static Property hasFilenameProperty() {
		return hasFilename;
	}
	
	public static Property hasParentDirectoryProperty() {
		return hasParentDirectory;
	}
	
	public static Property hasPathProperty() {
		return hasPath;
	}
}

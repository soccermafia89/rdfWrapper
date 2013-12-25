package linuxFilesystem;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import rdf.serializer.DataModel;
import rdf.serializer.URIFactory;
import wrappers.LinuxFilesystemWrapper;
import wrappers.StringContentWrapper;

public class NodeAppender {
	
	private static URIFactory URIGenerator = URIFactory.getInstance();

	public static DataModel appendParentDirectory(Collection<Resource> resources, Model metaData, HashMap<String, Object> data) {

		Model newMetaData = ModelFactory.createDefaultModel();
		HashMap<String, Object> newData = new HashMap<String, Object>();

		Iterator<Resource> it = resources.iterator();

		while (it.hasNext()) {
			Resource nextNode = it.next();

			//First determine what the parent path is.

			Resource nodePath = nextNode.getProperty(LinuxFilesystemWrapper.hasPath).getObject().asResource();
			String pathString = (String) data.get(nodePath.getURI());

			String parentPath = pathString.substring(0, StringUtils.lastIndexOf(pathString, "/"));
			String parentFilename = parentPath.substring(StringUtils.lastIndexOf(parentPath, "/") + 1);
			
//			System.out.println("");
//			System.out.println("Child Path: " + pathString);
//			System.out.println("Parent Path: " + parentPath);
//			System.out.println("Parent Filename: " + parentFilename);

			Resource parentNode = newMetaData.createResource(URIGenerator.generateURI(), LinuxFilesystemWrapper.node);

			//Append data about the file's parent directory.
			newMetaData.add(nextNode, LinuxFilesystemWrapper.hasParentDirectory, parentNode);

			//Append datum about the parent directory's name
			Resource parentNodeFilename = newMetaData.createResource(URIGenerator.generateURI(), LinuxFilesystemWrapper.filename);
			newMetaData.add(parentNode, LinuxFilesystemWrapper.hasFilename, parentNodeFilename);
			newData.put(parentNodeFilename.getURI(), parentFilename);

			//Append datum about the parent directory's path
			Resource parentNodePath = newMetaData.createResource(URIGenerator.generateURI(), LinuxFilesystemWrapper.path);
			newMetaData.add(parentNode, LinuxFilesystemWrapper.hasPath, parentNodePath);
			newData.put(parentNodePath.getURI(), parentPath);

		}

		return new DataModel(newMetaData, newData);
	}

	public static DataModel appendStringContent(Collection<Resource> resources, Model metaData, HashMap<String, Object> data) throws IOException {

		Model newMetaData = ModelFactory.createDefaultModel();
		HashMap<String, Object> newData = new HashMap<String, Object>();
		
		Iterator<Resource> it = resources.iterator();
		
		while (it.hasNext()) {
			Resource nextNode = it.next();
			
			Resource nodePath = nextNode.getProperty(LinuxFilesystemWrapper.hasPath).getObject().asResource();
			
			//First get the path
			String pathString = (String) data.get(nodePath.getId());

			//Now make OS call to get content
			String[] cat = { "cat", pathString };
			Process proc = Runtime.getRuntime().exec(cat);

			InputStream in = proc.getInputStream();
			InputStreamReader streamReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(streamReader);

			String contentString = "";
			String line = null;
			int lineOffset = 0;
			while ((line = reader.readLine()) != null) {
				contentString += line + "\n";

				lineOffset++;
			}

			//Now append a resource representing the content
			Resource newContentResource = newMetaData.createResource(URIGenerator.generateURI(), StringContentWrapper.content);
			metaData.add(nextNode, StringContentWrapper.hasContent, newContentResource);
			newData.put(newContentResource.getURI(), contentString);
		}

		return new DataModel(newMetaData, newData);
	}
}

package linux.filesystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import rdf.serializer.DataModel;
import string.content.StringContentWrapper;

public class NodeAppender {

	public static DataModel appendParentDirectory(Collection<Resource> resources, Model metaData, HashMap<AnonId, Object> data) {

		Model newMetaData = ModelFactory.createDefaultModel();
		HashMap<AnonId, Object> newData = new HashMap<AnonId, Object>();

		Iterator<Resource> it = resources.iterator();

		while (it.hasNext()) {
			Resource nextNode = it.next();

			//First determine what the parent path is.

			Resource nodePath = nextNode.getProperty(LinuxFilesystemWrapper.hasPath).getObject().asResource();
			String pathString = (String) data.get(nodePath.getId());

			String parentPath = pathString.substring(0, StringUtils.lastIndexOf(pathString, "/"));
			String parentFilename = pathString.substring(StringUtils.lastIndexOf(pathString, "/"));

			Resource parentNode = newMetaData.createResource(LinuxFilesystemWrapper.node);

			//Append data about the file's parent directory.
			newMetaData.add(nextNode, LinuxFilesystemWrapper.hasParentDirectory, parentNode);

			//Append datum about the parent directory's name
			Resource parentNodeFilename = newMetaData.createResource(LinuxFilesystemWrapper.filename);
			newMetaData.add(parentNode, LinuxFilesystemWrapper.hasFilename, parentNodeFilename);
			newData.put(parentNodeFilename.getId(), parentFilename);

			//Append datum about the parent directory's path
			Resource parentNodePath = newMetaData.createResource(LinuxFilesystemWrapper.path);
			newMetaData.add(parentNode, LinuxFilesystemWrapper.hasPath, parentNodePath);
			newData.put(parentNodePath.getId(), parentPath);

		}

		return new DataModel(newMetaData, newData);
	}

	public static DataModel appendStringContent(Collection<Resource> resources, Model metaData, HashMap<AnonId, Object> data) throws IOException {

		Model newMetaData = ModelFactory.createDefaultModel();
		HashMap<AnonId, Object> newData = new HashMap<AnonId, Object>();
		
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
			Resource newContentResource = newMetaData.createResource(StringContentWrapper.content);
			metaData.add(nextNode, StringContentWrapper.hasContent, newContentResource);
			newData.put(newContentResource.getId(), contentString);
		}

		return new DataModel(newMetaData, newData);
	}
}

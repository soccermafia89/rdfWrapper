package combiners;

import java.util.HashMap;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import rdf.serializer.RDFSerializerWrapper;
import linux.filesystem.LinuxFilesystemWrapper;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

public class NodeCombiner {
	

	public static Model linkNodes(Model metaData, Model newMetaData, HashMap<String, Object> data, HashMap<String, Object> newData) {
		Model linkedMetadata = ModelFactory.createDefaultModel();

		Reasoner reasoner = PelletReasonerFactory.theInstance().create();
		InfModel oldInf = ModelFactory.createInfModel(reasoner, metaData);

		HashMap<String, Resource> oldSet = new HashMap<String, Resource>();

		//Create a reverse map for the old set of metadata.
		StmtIterator stmtIt = oldInf.listStatements((Resource) null, RDF.type, LinuxFilesystemWrapper.node);
		while (stmtIt.hasNext()) {
			Statement stmt = stmtIt.nextStatement();

			Resource nextNode = stmt.getSubject();
			Resource nodePath = nextNode.getPropertyResourceValue(LinuxFilesystemWrapper.hasPath);

			String oldPath = (String) data.get(nodePath.getURI());
			oldSet.put(oldPath, nextNode);
		}

		InfModel newInf = ModelFactory.createInfModel(reasoner, newMetaData);

		//Iterate across new set of metadata
		stmtIt = newInf.listStatements((Resource) null, RDF.type, LinuxFilesystemWrapper.node);
		while (stmtIt.hasNext()) {
			Statement stmt = stmtIt.nextStatement();

			Resource nextNode = stmt.getSubject();
			Resource nodePath = nextNode.getPropertyResourceValue(LinuxFilesystemWrapper.hasPath);

			String newPath = (String) newData.get(nodePath.getURI());
			
			//If there is a match.
			if(oldSet.containsKey(newPath)) {
				Resource oldNode = oldSet.get(newPath);
				
				System.out.println("");
				System.out.println("Linking Nodes: " + 
				data.get(nextNode.getPropertyResourceValue(LinuxFilesystemWrapper.hasPath).getURI())
				+ " = " +
				data.get(oldNode.getPropertyResourceValue(LinuxFilesystemWrapper.hasPath).getURI())
						);
				
				linkedMetadata.add(linkNodeResources(nextNode, oldNode));
				
//				linkedMetadata.add(nextNode, sameAs, oldNode);
				
				//Also mark other linked metadata to be the same.
			}
			
			//Also remember to add newly processed nodes to the oldSet
			//This has to occur after the isLinkedWithProperty property is added to the linkedMetadata.
			oldSet.put(newPath, nextNode);
		}

		return linkedMetadata;
	}
	
	private static Model linkNodeResources(Resource node1, Resource node2) {
		
		Model model = ModelFactory.createDefaultModel();
		
		System.out.println("Linking Nodes: " + node1.getURI() + "=" + node2.getURI());
		model.add(node1, RDFSerializerWrapper.isLinkedWith, node2);
		
		//Mark filenames as equal.
		Resource filename1 = node1.getPropertyResourceValue(LinuxFilesystemWrapper.hasFilename);
		Resource filename2 = node2.getPropertyResourceValue(LinuxFilesystemWrapper.hasFilename);
		if(filename1 != null && filename2 != null) {
			System.out.println("Linking filenames: " + filename1.getURI() + "=" + filename2.getURI());
			model.add(filename1, RDFSerializerWrapper.isLinkedWith, filename2);
		} else {
			System.out.println("NULL FOUND!");
		}
		
		//Mark content as equal.
		Resource content1 = node1.getPropertyResourceValue(LinuxFilesystemWrapper.hasContent);
		Resource content2 = node2.getPropertyResourceValue(LinuxFilesystemWrapper.hasContent);
		if(content1 != null && content2 != null) {
			System.out.println("Linking content: " + content1.getURI() + "=" + content2.getURI());
			model.add(content1, RDFSerializerWrapper.isLinkedWith, content2);
		} else {
			System.out.println("NULL FOUND!");
		}
		
		//Mark path as equal.
		Resource path1 = node1.getPropertyResourceValue(LinuxFilesystemWrapper.hasPath);
		Resource path2 = node2.getPropertyResourceValue(LinuxFilesystemWrapper.hasPath);
		if(path1 != null && path2 != null) {
			System.out.println("Linking paths: " + path1.getURI() + "=" + path2.getURI());
			model.add(path1, RDFSerializerWrapper.isLinkedWith, path2);
		} else {
			System.out.println("NULL FOUND!");
		}
		
		//Mark parent directory as equal.
		Resource parentDirectory1 = node1.getPropertyResourceValue(LinuxFilesystemWrapper.hasParentDirectory);
		Resource parentDirectory2 = node2.getPropertyResourceValue(LinuxFilesystemWrapper.hasParentDirectory);
		if(parentDirectory1 != null && parentDirectory2 != null) {
			System.out.println("Linking parent directory: " + parentDirectory1.getURI() + "=" + parentDirectory2.getURI());
			model.add(parentDirectory1, RDFSerializerWrapper.isLinkedWith, parentDirectory2);
		} else {
			System.out.println("NULL FOUND!");
		}
		
		return model;
	}
}

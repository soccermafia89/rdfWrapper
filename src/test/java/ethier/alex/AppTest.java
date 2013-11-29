package ethier.alex;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.mindswap.pellet.jena.PelletReasonerFactory;

import rdf.serializer.DataModel;
import rdf.serializer.Reducer;
import rdf.serializer.URIFactory;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.reasoner.Reasoner;
import com.hp.hpl.jena.vocabulary.RDF;

import combiners.NodeCombiner;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import linux.filesystem.LinuxFilesystemWrapper;
import linux.filesystem.NodeAppender;

/**
 * Unit test for simple App.
 */
public class AppTest
		extends TestCase
{
	private URIFactory URIgenerator = URIFactory.getInstance();

	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName)
	{
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite()
	{
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 * 
	 * @throws Exception
	 */
	public void testInferencing() throws Exception
	{
		System.out.println("");
		System.out.println("");
		System.out.println("********************************************");
		System.out.println("******      Testing Inferencing       ******");
		System.out.println("********************************************");
		System.out.println("");
		System.out.println("");

		try {

			InputStream inputStream = ClassLoader.getSystemResourceAsStream("ExampleProb.owl");

			Model model = ModelFactory.createDefaultModel();
			model.read(inputStream, null, "RDF/XML");

			Property hasColor = model.getProperty("http://www.semanticweb.org/aethier/ontologies/2013/10/untitled-ontology-21#hasColor");
			Resource anonSubject = model.createResource(URIgenerator.generateURI());
			String id = anonSubject.getURI();
			System.out.println("Created resource: " + id.toString());
			Resource anonObject = model.createResource(URIgenerator.generateURI());

			model.add(anonSubject, hasColor, anonObject);

			//			model.write(System.out);

			Reasoner reasoner = PelletReasonerFactory.theInstance().create();
			InfModel inf = ModelFactory.createInfModel(reasoner, model);

			//			inf.write(System.out);

			Resource subject = null;
			Property predicate = inf.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			Resource object = inf.getResource("http://www.semanticweb.org/aethier/ontologies/2013/10/untitled-ontology-21#GeneralObject");

			StmtIterator it = inf.listStatements(subject, predicate, object);
			while (it.hasNext()) {
				Statement statement = it.next();

				Resource subjectQuery = statement.getSubject().asResource();
				//				if (subjectQuery.isAnon()) {
				System.out.println("Query Found Resource: " + subjectQuery.getURI().toString());
				//					assertTrue(subjectQuery.getURI().equals(id));
				//				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	//	public void testSubClassInferenc() throws Exception
	//	{
	//		System.out.println("");
	//		System.out.println("");
	//		System.out.println("********************************************");
	//		System.out.println("******       Testing SubClass         ******");
	//		System.out.println("********************************************");
	//		System.out.println("");
	//		System.out.println("");
	//
	//		try {
	//			
	//			Model model = ModelFactory.createDefaultModel();
	//			Resource pathResource = LinuxFilesystemWrapper.pathResource();
	//			
	//			
	//
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			throw new Exception(e.getMessage());
	//		}
	//	}

	public void testLinuxFilesystem() throws Exception
	{
		System.out.println("");
		System.out.println("");
		System.out.println("********************************************");
		System.out.println("*****     Testing Linux Filesystem     *****");
		System.out.println("********************************************");
		System.out.println("");
		System.out.println("");

		try {

			Model metaData = ModelFactory.createDefaultModel();

			//Load LinuxFilesystem Model
			metaData.add(LinuxFilesystemWrapper.model);

			HashMap<String, Object> data = new HashMap<String, Object>();

			String pathString = "/home/alex/";

			if (pathString.endsWith("/")) {
				pathString = pathString.substring(0, pathString.length() - 1);
			}

			String[] ls = { "ls", pathString };
			Process proc = Runtime.getRuntime().exec(ls);

			InputStream in = proc.getInputStream();
			InputStreamReader streamReader = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(streamReader);

			String line = null;
			while ((line = reader.readLine()) != null) {

				//Generate a unique resource representing each file found.
				//Resource newNode = filesMetadata.createResource(nodeType);
				Resource newNode = metaData.createResource(URIgenerator.generateURI(), LinuxFilesystemWrapper.node);

				//Append data about the file's name
				Resource nodeFilename = metaData.createResource(URIgenerator.generateURI(), LinuxFilesystemWrapper.filename);
				metaData.add(newNode, LinuxFilesystemWrapper.hasFilename, nodeFilename);
				data.put(nodeFilename.getURI(), line);

				//Append data about file's path
				String nodePathString = pathString + "/" + line;
				Resource nodePath = metaData.createResource(URIgenerator.generateURI(), LinuxFilesystemWrapper.path);
				metaData.add(newNode, LinuxFilesystemWrapper.hasPath, nodePath);
				data.put(nodePath.getURI(), nodePathString);
			}

			//			metaData.write(System.out);
			//			System.out.println("");
			//			System.out.println(data.toString());	

			ArrayList<Resource> nodes = new ArrayList<Resource>();

			StmtIterator it = metaData.listStatements((Resource) null, RDF.type, LinuxFilesystemWrapper.node);
			while (it.hasNext()) {
				Statement statement = it.nextStatement();
				Resource nextNode = statement.getSubject().asResource();
				Resource nodeFilename = nextNode.getPropertyResourceValue(LinuxFilesystemWrapper.hasFilename);
				String filenameString = (String) data.get(nodeFilename.getURI());
				System.out.println(filenameString);
				nodes.add(nextNode);
			}

			System.out.println("");

			DataModel parentDirectoryDataModel = NodeAppender.appendParentDirectory(nodes, metaData, data);
			Model parentDirectoryMetaData = parentDirectoryDataModel.getMetaData();
			HashMap<String, Object> parentDirectoryData = parentDirectoryDataModel.getData();

			//			parentDirectoryMetaData.write(System.out);
			//			System.out.println("");
			//			System.out.println(parentDirectoryData.toString());

			data.putAll(parentDirectoryData);
			metaData.add(parentDirectoryMetaData);

			Model links = NodeCombiner.linkNodes(metaData, parentDirectoryMetaData, data, parentDirectoryData);
			//			links.write(System.out);
			metaData.add(links);

			DataModel dataModel = Reducer.reduce(metaData, data);
			metaData = dataModel.getMetaData();
			data = dataModel.getData();

			metaData.write(System.out);
			System.out.println("");
			System.out.println(data.toString());

			//			Reasoner reasoner = PelletReasonerFactory.theInstance().create();
			//			InfModel inf = ModelFactory.createInfModel(reasoner, metaData);
			//			it = inf.listStatements((Resource) null, RDF.type, inf.getResource("http://ethier.alex.com/RDFSerializable#MappedResource"));
			//			while(it.hasNext()) {
			//				System.out.println(it.nextStatement().asTriple().toString());
			//			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

}

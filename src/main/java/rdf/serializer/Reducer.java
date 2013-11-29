package rdf.serializer;

import java.util.HashMap;
import java.util.HashSet;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.OWL;

/**
 * 
 * Removes sameAs statements in Model while retaining integrity and consistency of data.
 * 
 * 
 * @author alex
 *
 */
public class Reducer {
	public static DataModel reduce(Model metaData, HashMap<String, Object> data) {
		Model reducedModel = ModelFactory.createUnion(metaData, ModelFactory.createDefaultModel());
		HashMap<String, Object> reducedData = new HashMap<String, Object>();
		reducedData.putAll(data);
		
		StmtIterator it = reducedModel.listStatements((Resource) null, OWL.sameAs, (Resource) null);
		HashSet<String> ids = new HashSet<String>();
		
		while(it.hasNext()) {
			Statement statement = it.nextStatement();
			
			Resource subject = statement.getSubject();
			Resource object = statement.getObject().asResource();
			
			if(!ids.contains(subject.getURI()) && !ids.contains(object.getURI())) {
				ids.add(subject.getURI());
			}
			
			if(!ids.contains(subject.getURI())) {
				//We have the object ID in our set.
				Reducer.replace(subject, object, reducedModel, reducedData);
			} else if(!ids.contains(object.getURI())) {
				//We have the subject ID in our set.
				Reducer.replace(object, subject, reducedModel, reducedData);
			}
			
			statement.remove();
			
			it = reducedModel.listStatements((Resource) null, OWL.sameAs, (Resource) null);
		}
		
		return new DataModel(reducedModel, reducedData);
	}
	
	//Replaces subject and object nodes in statements from source to target.
	private static void replace(Resource source, Resource target, Model model, HashMap<String, Object> data) {
		StmtIterator subIt = model.listStatements(source, (Property) null, (Resource) null);
		int count = 0;
		while(subIt.hasNext()) {
			subIt.nextStatement();
			count++;
		}
		
		Statement[] subArray = new Statement[count];
		subIt = model.listStatements(source, (Property) null, (Resource) null);
		for(int i=0;i < subArray.length;i++) {
			subArray[i] = subIt.nextStatement();
		}
		
		for(int i=0;i < subArray.length;i++) {		
			Statement subStatement = subArray[i];
			Resource subject = target;
			Property predicate = subStatement.getPredicate();
			Resource object = subStatement.getObject().asResource();
			
			System.out.println("Replacing: " + data.get(subject.getURI()) + " " + predicate.getLocalName() + " " + data.get(object.getURI()));
			System.out.println("With: " + data.get(target.getURI()) + " " + predicate.getLocalName() + " " + data.get(object.getURI()));

			//Replace metadata
			subStatement.remove();
			model.add(subject, predicate, object);
			//Remove duplicate data
			data.remove(subStatement.getSubject().asResource().getURI());
		}
		
		StmtIterator objIt = model.listStatements((Resource) null, (Property) null, source);
		count = 0;
		while(objIt.hasNext()) {
			objIt.nextStatement();
			count++;
		}
		
		Statement[] objArray = new Statement[count];
		objIt = model.listStatements((Resource) null, (Property) null, source);
		for(int i=0;i < objArray.length;i++) {
			objArray[i] = objIt.nextStatement();
		}
		
		for(int i=0;i < objArray.length;i++) {		
			Statement objStatement = objArray[i];
			Resource subject = objStatement.getSubject();
			Property predicate = objStatement.getPredicate();
			Resource object = target;
			
			System.out.println("Replacing: " + data.get(subject.getURI()) + " " + predicate.getLocalName() + " " + data.get(object.getURI()));
			System.out.println("With: " + data.get(subject.getURI()) + " " + predicate.getLocalName() + " " + data.get(target.getURI()));
			
			//Replace metadata
			objStatement.remove();
			model.add(subject, predicate, object);
			//Remove duplicate data
			data.remove(objStatement.getObject().asResource().getURI());
		}
	}
}

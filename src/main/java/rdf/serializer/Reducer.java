package rdf.serializer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.AnonId;
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
	public static DataModel reduce(Model metaData, HashMap<AnonId, Object> data) {
		Model reducedModel = ModelFactory.createUnion(metaData, ModelFactory.createDefaultModel());
		HashMap<AnonId, Object> reducedData = new HashMap<AnonId, Object>();
		reducedData.putAll(data);
		
		StmtIterator it = reducedModel.listStatements((Resource) null, OWL.sameAs, (Resource) null);
		HashSet<AnonId> ids = new HashSet<AnonId>();
		
		while(it.hasNext()) {
			Statement statement = it.nextStatement();
			
			Resource subject = statement.getSubject();
			Resource object = statement.getObject().asResource();
			
			if(!ids.contains(subject.getId()) && !ids.contains(object.getId())) {
				ids.add(subject.getId());
			}
			
			if(!ids.contains(subject.getId())) {
				//We have the object ID in our set.
				Reducer.replace(subject, object, reducedModel, reducedData);
			} else if(!ids.contains(object.getId())) {
				//We have the subject ID in our set.
				Reducer.replace(object, subject, reducedModel, reducedData);
			}
			
			statement.remove();
			
			it = reducedModel.listStatements((Resource) null, OWL.sameAs, (Resource) null);
		}
		
		return new DataModel(reducedModel, reducedData);
	}
	
	//Replaces subject and object nodes in statements from source to target.
	private static void replace(Resource source, Resource target, Model model, HashMap<AnonId, Object> data) {
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
			
			System.out.println("Replacing: " + data.get(subject.getId()) + " " + predicate.getLocalName() + " " + data.get(object.getId()));
			System.out.println("With: " + data.get(target.getId()) + " " + predicate.getLocalName() + " " + data.get(object.getId()));

			//Replace metadata
			subStatement.remove();
			model.add(subject, predicate, object);
			//Remove duplicate data
			data.remove(subStatement.getSubject().asResource().getId());
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
			
			System.out.println("Replacing: " + data.get(subject.getId()) + " " + predicate.getLocalName() + " " + data.get(object.getId()));
			System.out.println("With: " + data.get(subject.getId()) + " " + predicate.getLocalName() + " " + data.get(target.getId()));
			
			//Replace metadata
			objStatement.remove();
			model.add(subject, predicate, object);
			//Remove duplicate data
			data.remove(objStatement.getObject().asResource().getId());
		}
	}
}

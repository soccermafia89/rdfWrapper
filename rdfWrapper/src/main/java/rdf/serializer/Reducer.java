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
 * Removes isLinkedWith statements in the Model while retaining integrity and
 * consistency of data.
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

	StmtIterator it = reducedModel.listStatements((Resource) null, RDFSerializerWrapper.isLinkedWith, (Resource) null);

	while (it.hasNext()) {
		Statement statement = it.nextStatement();

		Resource subject = statement.getSubject();
		Resource object = statement.getObject().asResource();
		
		Reducer.replace(object, subject, reducedModel, reducedData);

		statement.remove();

		it = reducedModel.listStatements((Resource) null, RDFSerializerWrapper.isLinkedWith, (Resource) null);
	}

	return new DataModel(reducedModel, reducedData);
}

	//Replaces subject and object nodes in statements from source to target.
	private static void replace(Resource source, Resource target, Model model, HashMap<String, Object> data) {

		//As replacements occur the isLinkedWith statements will eventually be changed to replace the same nodes,
		//instead of looping through this, just remove the statement.
		if (source.getURI().equals(target.getURI())) {
			StmtIterator stmtIterator = model.listStatements(source, RDFSerializerWrapper.isLinkedWith, target);
			stmtIterator.nextStatement().remove();
		} else {
			String sourceContentArg = "" + data.get(source.getURI());
			if (sourceContentArg.equals("null")) {
				sourceContentArg = "" + source.getURI();
			} else {
				sourceContentArg = source.getURI() + ":" + sourceContentArg;
			}

			String targetContentArg = "" + data.get(target.getURI());
			if (targetContentArg.equals("null")) {
				targetContentArg = "" + target.getURI();
			} else {
				targetContentArg = target.getURI() + ":" + targetContentArg;
			}

			System.out.println("");
			System.out.println("Replacing: " + sourceContentArg + " with " + targetContentArg);

			StmtIterator subIt = model.listStatements(source, (Property) null, (Resource) null);
			int count = 0;
			while (subIt.hasNext()) {
				subIt.nextStatement();
				count++;
			}

			Statement[] subArray = new Statement[count];
			subIt = model.listStatements(source, (Property) null, (Resource) null);
			for (int i = 0; i < subArray.length; i++) {
				subArray[i] = subIt.nextStatement();
			}

			for (int i = 0; i < subArray.length; i++) {
				Statement subStatement = subArray[i];
				Resource subject = target;
				Property predicate = subStatement.getPredicate();
				Resource object = subStatement.getObject().asResource();

				String replacedSubjectContent = "" + data.get(subStatement.getSubject().getURI());
				if (replacedSubjectContent.equals("null")) {
					replacedSubjectContent = "" + subStatement.getSubject().getURI();
				} else {
					replacedSubjectContent = subStatement.getSubject().getURI() + ":" + replacedSubjectContent;
				}

				String objectContent = "" + data.get(object.getURI());
				if (objectContent.equals("null")) {
					objectContent = "" + object.getURI();
				} else {
					objectContent = object.getURI() + ":" + objectContent;
				}

				String targetContent = "" + data.get(target.getURI());
				if (targetContent.equals("null")) {
					targetContent = "" + target.getURI();
				} else {
					targetContent = target.getURI() + ":" + targetContent;
				}

				System.out.println("Replacing Subject: " + replacedSubjectContent + " " + predicate.getLocalName() + " " + objectContent);
				System.out.println("With: " + targetContent + " " + predicate.getLocalName() + " " + objectContent);

				//Replace data
				String key = subStatement.getSubject().asResource().getURI();
				if (data.containsKey(key)) {
					data.put(subject.getURI(), data.get(key));
					data.remove(key);
				}

				//Replace metadata
				subStatement.remove();
				model.add(subject, predicate, object);

			}

			StmtIterator objIt = model.listStatements((Resource) null, (Property) null, source);
			count = 0;
			while (objIt.hasNext()) {
				objIt.nextStatement();
				count++;
			}

			Statement[] objArray = new Statement[count];
			objIt = model.listStatements((Resource) null, (Property) null, source);
			for (int i = 0; i < objArray.length; i++) {
				objArray[i] = objIt.nextStatement();
			}

			for (int i = 0; i < objArray.length; i++) {
				Statement objStatement = objArray[i];
				Resource subject = objStatement.getSubject();
				Property predicate = objStatement.getPredicate();
				Resource object = target;

				String subjectContent = "" + data.get(subject.getURI());
				if (subjectContent.equals("null")) {
					subjectContent = "" + subject.getURI();
				} else {
					subjectContent = subject.getURI() + ":" + subjectContent;
				}

				String replacdObjectContent = "" + data.get(objStatement.getObject().asResource().getURI());
				if (replacdObjectContent.equals("null")) {
					replacdObjectContent = "" + objStatement.getObject().asResource().getURI();
				} else {
					replacdObjectContent = objStatement.getObject().asResource().getURI() + ":" + replacdObjectContent;
				}

				String targetContent = "" + data.get(target.getURI());
				if (targetContent.equals("null")) {
					targetContent = "" + target.getURI();
				} else {
					targetContent = target.getURI() + ":" + targetContent;
				}

				System.out.println("Replacing Object: " + subjectContent + " " + predicate.getLocalName() + " " + replacdObjectContent);
				System.out.println("With: " + subjectContent + " " + predicate.getLocalName() + " " + targetContent);

				//Replace data
				String key = objStatement.getObject().asResource().getURI();
				if (data.containsKey(key)) {
					data.put(object.getURI(), data.get(key));
					data.remove(key);
				}

				//Replace metadata
				objStatement.remove();
				model.add(subject, predicate, object);
			}
		}
	}
}

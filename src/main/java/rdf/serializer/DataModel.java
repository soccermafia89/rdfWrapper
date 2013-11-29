package rdf.serializer;

import java.util.HashMap;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class DataModel {
	private Model metaData;
	private HashMap<AnonId, Object> data;
	
	public DataModel() {
		metaData = ModelFactory.createDefaultModel();
		data = new HashMap<AnonId, Object>();
	}
	
	public DataModel(Model myMetaData) {
		metaData = myMetaData;
		data = new HashMap<AnonId, Object>();		
	}
	
	public DataModel(HashMap<AnonId, Object> myData) {
		metaData = ModelFactory.createDefaultModel();
		data = myData;
	}
	
	public DataModel(Model myMetaData, HashMap<AnonId, Object> myData) {
		metaData = myMetaData;
		data = myData;
	}

	public Model getMetaData() {
		return metaData;
	}

	public void setMetaData(Model metaData) {
		this.metaData = metaData;
	}

	public HashMap<AnonId, Object> getData() {
		return data;
	}

	public void setData(HashMap<AnonId, Object> data) {
		this.data = data;
	}
}

package rdf.serializer;

import java.util.HashMap;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class DataModel {
	private Model metaData;
	private HashMap<String, Object> data;
	
	public DataModel() {
		metaData = ModelFactory.createDefaultModel();
		data = new HashMap<String, Object>();
	}
	
	public DataModel(Model myMetaData) {
		metaData = myMetaData;
		data = new HashMap<String, Object>();		
	}
	
	public DataModel(HashMap<String, Object> myData) {
		metaData = ModelFactory.createDefaultModel();
		data = myData;
	}
	
	public DataModel(Model myMetaData, HashMap<String, Object> myData) {
		metaData = myMetaData;
		data = myData;
	}

	public Model getMetaData() {
		return metaData;
	}

	public void setMetaData(Model metaData) {
		this.metaData = metaData;
	}

	public HashMap<String, Object> getData() {
		return data;
	}

	public void setData(HashMap<String, Object> data) {
		this.data = data;
	}
}

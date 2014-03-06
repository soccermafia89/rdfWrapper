/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethier.alex.genie.jena.impl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import ethier.alex.genie.data.interfaces.*;
import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**

 @author alex
 */
public class GenieJenaModel implements GenieModel {

    private Model model;
    private int counter;
    private Map<String, EmptyJenaInstance> genieInstances;

//    private final Resource pojo;
//    private final Property hasPojo;
//    public GenieModel() {
//        model = ModelFactory.createDefaultModel();
////        pojo = model.createResource("http://ethier.alex/genie/model#pojo");
////        hasPojo = model.createProperty("http://ethier.alex/genie/model#hasPojo");
//    }
    public GenieJenaModel() {
        model = ModelFactory.createDefaultModel();
        counter = 0;
        genieInstances = new HashMap<String, EmptyJenaInstance>();
    }

    public Model getModel() {
        return model;
    }

    EmptyInstance getInstance(Resource resource) {
        return genieInstances.get(resource.getURI());
    }
    
    public EmptyInstance createEmptyInstance() {
        EmptyJenaInstance newGenieInstance = new EmptyJenaInstance(this);
        genieInstances.put(newGenieInstance.getUri(), newGenieInstance);
        return newGenieInstance;
    }
    
    public GenieProperty createProperty(String propertyUri) {
        Property property = model.createProperty(propertyUri);
        return new GenieJenaProperty(property);
    }
    
    String generateUri() {
        counter++;
        return "" + counter;
    }
    
    //Mapped methods
    public MappedInstance createMappedInstance(Serializable serializable) {
        MappedJenaInstance newMappedInstance = new MappedJenaInstance(this, serializable);
        genieInstances.put(newMappedInstance.getUri(), newMappedInstance);
        return newMappedInstance;
    }

    public UniqueGenieProperty createUniqueProperty(String propertyUri) {
        Property property = model.createProperty(propertyUri);
        return new UniqueGenieJenaProperty(property);
    }
}

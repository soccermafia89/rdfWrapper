/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethier.alex.genie.data;

import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.RDF;
import ethier.alex.genie.data.*;
import ethier.alex.genie.exception.OverwriteException;
import java.io.Serializable;
import java.util.Collection;

/**

 @author alex
 */
public class GenieModel {

    private Model model;
    private int counter;

//    private final Resource pojo;
//    private final Property hasPojo;
//    public GenieModel() {
//        model = ModelFactory.createDefaultModel();
////        pojo = model.createResource("http://ethier.alex/genie/model#pojo");
////        hasPojo = model.createProperty("http://ethier.alex/genie/model#hasPojo");
//    }
    public GenieModel() {
        model = ModelFactory.createDefaultModel();
        counter = 0;
    }

    public Model getModel() {
        return model;
    }

    BlankGenieInstance getInstance(Resource resource) {
        return new BlankGenieInstance(model.getResource(resource.getURI()), this);
    }
    
    public BlankGenieInstance createBlankInstance() {
        return new BlankGenieInstance(this); 
    }
    
    public GenieProperty createProperty(String propertyUri) {
        Property property = model.createProperty(propertyUri);
        return new GenieProperty(property);
    }
    
    String generateUri() {
        counter++;
        return "" + counter;
    }
    
    //Mapped methods
    public MappedGenieInstance createMappedInstance(Serializable serializable) {
        return new MappedGenieInstance(this, serializable);
    }
    
//    MappedGenieInstance getMappedInstance(BlankGenieInstance blankInstance) {
//        return blankInstance.
//    }
    
    
    
//    public GenieClass addClass(String classUri, Class myClass) throws OverwriteException {
//        
//        Resource newClass = ResourceFactory.createResource(classUri);
//        Resource classType = ResourceFactory.createResource(myClass.getCanonicalName());
//        
//        if(model.containsResource(newClass)) {
//            throw new OverwriteException("Class with uri: " + classUri + " already exists!");
//        } else {
////            model.add(newClass, hasPojo, pojo);
//            model.add(newClass, RDF.type, classType);
//            GenieClass genieClass = new GenieClass(newClass, myClass);
//            return genieClass;
//        }
//    }
//
//    public BlankGenieClass getBlankClass(String classUri) throws OverwriteException {
//        Resource newClass = ResourceFactory.createResource(classUri);
//        
//        if(model.containsResource(newClass)) {
//            throw new OverwriteException("Class with uri: " + classUri + " already exists!");
//        } else {
//            model.createResource(newClass);
//            BlankGenieClass blankGenieClass = new BlankGenieClass(newClass);
//            return blankGenieClass;
//        }
//    }
//
//    @Override
//    public GenieInstance createInstance(GenieClass genieClass) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public BlankGenieInstance createBlankInstance(BlankGenieClass blankGenieClass) {
//        Resource blankResource = blankGenieClass.getResource();
//        System.out.println("Investigate jena, ensure we are not creating blank nodes.");
//        Resource blankInstanceResource = model.createResource("1", blankResource);
//        BlankGenieInstance blankGenieInstance = new BlankGenieInstance(blankInstanceResource);
//        return blankGenieInstance;
//    }
//
//    @Override
//    public void addStatement(BlankGenieInstance subjectInstance, GenieProperty property, BlankGenieInstance objectInstance) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public Collection<GenieStatement> listStatements(BlankGenieInstance subjectInstance, GenieProperty property, BlankGenieInstance objectInstance) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public BlankGenieClass addBlankClass(String classUri) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethier.alex.genie.jena.impl;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import ethier.alex.genie.data.interfaces.EmptyInstance;
import ethier.alex.genie.data.interfaces.GenieProperty;
import ethier.alex.genie.data.interfaces.MappedInstance;
import ethier.alex.genie.data.interfaces.UniqueGenieProperty;
import ethier.alex.genie.exception.OverwriteException;
import java.util.ArrayList;
import java.util.Collection;

/**

 @author alex
 */
public class EmptyJenaInstance implements EmptyInstance {
    
    private Resource resource;
    private GenieJenaModel genieModel;
    
    EmptyJenaInstance(GenieJenaModel myGenieModel) {
        
        resource = myGenieModel.getModel().createResource(myGenieModel.generateUri());
        genieModel = myGenieModel;
    }
    
//    BlankGenieInstance(Resource myResource, GenieModel myGenieModel) {
//        resource = myGenieModel.getModel().getResource(myResource.getURI());
//        genieModel = myGenieModel;
//    }
    
    Resource getResource() {
        return resource;
    }
    
    public void addObject(GenieProperty property, EmptyInstance blankGenieInstance) {
        
        GenieJenaProperty genieJenaProperty = (GenieJenaProperty) property;
        
        if(genieJenaProperty instanceof UniqueGenieProperty && resource.getPropertyResourceValue(genieJenaProperty.getProperty()) != null) {
            throw new OverwriteException("Unique property already assigned resource: " 
                    + resource.getPropertyResourceValue(genieJenaProperty.getProperty()).getURI());
        } 
        
        
        EmptyJenaInstance emptyJenaInstance = (EmptyJenaInstance) blankGenieInstance;
        
        resource.addProperty(genieJenaProperty.getProperty(), emptyJenaInstance.getResource());
    }
    
    public EmptyInstance getObject(UniqueGenieProperty property) {
        UniqueGenieJenaProperty genieJenaProperty = (UniqueGenieJenaProperty) property;
        
        Resource objectResource = resource.getPropertyResourceValue(genieJenaProperty.getProperty());
        return genieModel.getInstance(objectResource);
    }
    
    public Collection<EmptyInstance> getObjects(GenieProperty property) {
        GenieJenaProperty genieJenaProperty = (GenieJenaProperty) property;
        Model model = genieModel.getModel();
        StmtIterator it = model.listStatements(resource, genieJenaProperty.getProperty(), (Resource) null);
        
        Collection<EmptyInstance> objects = new ArrayList<EmptyInstance>();
        while(it.hasNext()) {
            Statement statement = it.nextStatement();
            Resource objectResource = statement.getObject().asResource();
            EmptyJenaInstance objectInstance = (EmptyJenaInstance) genieModel.getInstance(objectResource);
            objects.add(objectInstance);
        }
        
        return objects;
    }
    
    public String getUri() {
        return resource.getURI();
    }
    
    public MappedInstance asMappedInstance() {
        return (MappedInstance) this;    
    }
}

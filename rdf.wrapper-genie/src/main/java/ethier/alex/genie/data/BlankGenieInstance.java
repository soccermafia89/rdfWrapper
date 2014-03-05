/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethier.alex.genie.data;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**

 @author alex
 */
public class BlankGenieInstance {
    
    protected Resource resource;
    private GenieModel genieModel;
    
    BlankGenieInstance(GenieModel myGenieModel) {
        
        resource = myGenieModel.getModel().createResource(myGenieModel.generateUri());
        genieModel = myGenieModel;
    }
    
    BlankGenieInstance(Resource myResource, GenieModel myGenieModel) {
        resource = myGenieModel.getModel().getResource(myResource.getURI());
        genieModel = myGenieModel;
    }
    
    Resource getResource() {
        return resource;
    }
    
    public void addObject(GenieProperty property, BlankGenieInstance blankGenieInstance) {
        resource.addProperty(property.getProperty(), blankGenieInstance.getResource());
    }
    
    public BlankGenieInstance getObject(GenieProperty property) {
        Resource objectResource = resource.getPropertyResourceValue(property.getProperty());
        return genieModel.getInstance(objectResource);
    }
    
    public String getUri() {
        return resource.getURI();
    }
    
//    public MappedGenieInstance asMappedInstance() {
////        return genieModel.g
////        return MappedGenieInstance.class.cast(this);
////        return this.getClass().cast(this);
//    }
}

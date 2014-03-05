/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethier.alex.genie.data;

import com.hp.hpl.jena.rdf.model.Resource;
import java.io.Serializable;

/**

 @author alex
 */
public class MappedGenieInstance extends BlankGenieInstance {
    
    private Serializable pojo;
    
    MappedGenieInstance(GenieModel myGenieModel, Serializable serializable) {
        super(myGenieModel);
        pojo = serializable;
    }
    
    MappedGenieInstance(Resource myResource, GenieModel myGenieModel, Serializable serializable) {
        super(myResource, myGenieModel);
        pojo = serializable;
    }
    
    public Serializable getPojo() {
        return pojo;
    }
}

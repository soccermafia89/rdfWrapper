/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethier.alex.genie.data;

import com.hp.hpl.jena.rdf.model.Property;

/**

 @author alex
 */
public class GenieProperty {
    
    private Property property;
    
    GenieProperty(Property myProperty) {
        property = myProperty;
    }
    
    Property getProperty() {
        return property;
    }
    
}

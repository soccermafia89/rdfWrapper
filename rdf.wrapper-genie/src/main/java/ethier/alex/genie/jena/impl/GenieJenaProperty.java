/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethier.alex.genie.jena.impl;

import com.hp.hpl.jena.rdf.model.Property;
import ethier.alex.genie.data.interfaces.GenieProperty;

/**

 @author alex
 */
public class GenieJenaProperty implements GenieProperty {
    
    private Property property;
    
    GenieJenaProperty(Property myProperty) {
        property = myProperty;
    }
    
    protected Property getProperty() {
        return property;
    }
    
}

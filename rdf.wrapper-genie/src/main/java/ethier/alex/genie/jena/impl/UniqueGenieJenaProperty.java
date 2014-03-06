/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethier.alex.genie.jena.impl;

import com.hp.hpl.jena.rdf.model.Property;
import ethier.alex.genie.data.interfaces.UniqueGenieProperty;

/**

 @author alex
 */
public class UniqueGenieJenaProperty extends GenieJenaProperty implements UniqueGenieProperty {
    
       UniqueGenieJenaProperty(Property myProperty) {
        super(myProperty);
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethier.alex.genie.data.interfaces;

import ethier.alex.genie.exception.OverwriteException;
import java.io.Serializable;

/**

 @author alex
 */
public interface GenieModel {
    
    public EmptyInstance createEmptyInstance();
    
    public MappedInstance createMappedInstance(Serializable serializable);
    
    public GenieProperty createProperty(String property);
    
    public UniqueGenieProperty createUniqueProperty(String property);
}

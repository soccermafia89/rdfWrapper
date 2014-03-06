/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethier.alex.genie.data.interfaces;

import java.util.Collection;

/**

 @author alex
 */
public interface EmptyInstance {
    
    public void addObject(GenieProperty property, EmptyInstance objectInstance);
    
    public Collection<EmptyInstance> getObjects(GenieProperty property);
    
    public EmptyInstance getObject(UniqueGenieProperty property);
    
    public MappedInstance asMappedInstance();
    
}

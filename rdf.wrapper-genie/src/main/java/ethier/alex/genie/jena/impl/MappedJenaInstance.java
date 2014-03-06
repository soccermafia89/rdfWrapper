/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethier.alex.genie.jena.impl;

import ethier.alex.genie.data.interfaces.MappedInstance;
import java.io.Serializable;

/**

 @author alex
 */
public class MappedJenaInstance extends EmptyJenaInstance implements MappedInstance {
    
    private Serializable pojo;
    
    MappedJenaInstance(GenieJenaModel myGenieModel, Serializable serializable) {
        super(myGenieModel);
        pojo = serializable;
    }
    
    public Serializable getPojo() {
        return pojo;
    }
}

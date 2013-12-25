/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maven;

import java.io.IOException;
import rdf.serializer.DataModel;
import rdf.serializer.URIFactory;

/**
 *
 * @author alex
 */
public class MavenDependencyTree {
    public static DataModel buildDependencyTree(URIFactory URIgenerator) throws IOException {
        //Will read a pom file of a project.
        //Will then find all referenced dependencies, and read their pomm files.
        //Will attempt to list all transitive dependencies and where they come from.
        
        return null;
    }
}

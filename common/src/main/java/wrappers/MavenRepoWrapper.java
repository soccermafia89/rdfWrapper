/*
 */

package wrappers;

import com.hp.hpl.jena.rdf.model.*;
/**

*/

public class MavenRepoWrapper{

    protected static final String uri = "http://ethier.alex.com/MavenRepo#";

    /** returns the URI for this schema
        @return the URI for this schema
    */
    public static String getURI()
        { return uri; }

    protected static final Resource resource( String local )
        { return ResourceFactory.createResource( uri + local ); }

    protected static final Property property( String local )
        { return ResourceFactory.createProperty( uri, local ); }
    
	public static final Resource groupID = resource( "groupID" );
	public static final Resource artifactID = resource( "artifactID" );
        
        public static final Resource jar = resource( "jar" );
        public static final Resource version = resource( "version" );
        
        public static final Resource className = resource( "className" );

	public static final Property hasArtifactID = property( "hasArtifactID" );
	public static final Property hasGroupID = property( "hasGroupID" );
	public static final Property containsJar = property( "containsJar" );
        public static final Property containsVersion = property( "containsVersion" );
        public static final Property containsClassName = property( "containsJar" );
	
}

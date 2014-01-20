package maven;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.Collection;
import java.util.HashMap;
import rdf.serializer.DataModel;
import rdf.serializer.RDFSerializerWrapper;
import rdf.serializer.URIFactory;
import wrappers.MavenRepoWrapper;

/**
 *
 * @author alex
 */
public class MavenDataObject {

    private URIFactory URIgenerator;
    private String groupID;
    private String artifactID;
    private String jar;
    private String version;
    private Collection<String> classNames;

    public MavenDataObject(String myGroupID, String myArtifactID, String myJar, String myVersion, Collection<String> myClassNames) {
        URIgenerator = URIFactory.getInstance();

        groupID = myGroupID;
        artifactID = myArtifactID;
        jar = myJar;
        version = myVersion;
        classNames = myClassNames;
    }

    //Converts this pojo to rdf.
    public DataModel toDataModel() {
        Model metaData = ModelFactory.createDefaultModel();
        HashMap<String, Object> data = new HashMap<String, Object>();

        Resource b_node = metaData.createResource(URIgenerator.generateURI());

        //Setup the groupID node.
        Resource groupIDResource = metaData.createResource(URIgenerator.generateURI(), MavenRepoWrapper.groupID);
        data.put(groupIDResource.getURI(), groupID);
        b_node.addProperty(MavenRepoWrapper.hasGroupID, groupIDResource);

        Resource groupIDHashResource = metaData.createResource(URIgenerator.generateURI(), RDFSerializerWrapper.hash);
        data.put(groupIDHashResource.getURI(), groupID);//If two group ids are the same then the nodes should be joined.
        groupIDResource.addProperty(RDFSerializerWrapper.hasHash, groupIDHashResource);

        //Setup the artifactID node.
        Resource artifactIDResource = metaData.createResource(URIgenerator.generateURI(), MavenRepoWrapper.artifactID);
        data.put(groupIDResource.getURI(), artifactID);
        b_node.addProperty(MavenRepoWrapper.hasArtifactID, artifactIDResource);

        Resource artifactIDHashResource = metaData.createResource(URIgenerator.generateURI(), RDFSerializerWrapper.hash);
        data.put(artifactIDHashResource.getURI(), groupID + artifactID);//Artifact nodes should be joined with they share group and artifact IDs.
        groupIDResource.addProperty(RDFSerializerWrapper.hasHash, artifactIDHashResource);

        //Setup the jar node.
        Resource jarResource = metaData.createResource(URIgenerator.generateURI(), MavenRepoWrapper.jar);
        data.put(groupIDResource.getURI(), jar);
        b_node.addProperty(MavenRepoWrapper.containsJar, jarResource);

        Resource jarHashResource = metaData.createResource(URIgenerator.generateURI(), RDFSerializerWrapper.hash);
        data.put(jarHashResource.getURI(), groupID + artifactID + jar);//Jar nodes should be joined with they share groupID, artifactID, and jar.
        groupIDResource.addProperty(RDFSerializerWrapper.hasHash, jarHashResource);

        //Setup the version node.
        Resource versionResource = metaData.createResource(URIgenerator.generateURI(), MavenRepoWrapper.version);
        data.put(groupIDResource.getURI(), version);
        b_node.addProperty(MavenRepoWrapper.containsVersion, versionResource);

        Resource versionHashResource = metaData.createResource(URIgenerator.generateURI(), RDFSerializerWrapper.hash);
        data.put(versionHashResource.getURI(), groupID + artifactID + version);//Version nodes should be joined with they share groupID, artifactID, and version.
        groupIDResource.addProperty(RDFSerializerWrapper.hasHash, versionHashResource);

        //Setup the classNames Nodes.
        for (String className : classNames) {

            Resource classNameResource = metaData.createResource(URIgenerator.generateURI(), MavenRepoWrapper.className);
            data.put(groupIDResource.getURI(), className);
            b_node.addProperty(MavenRepoWrapper.containsClassName, classNameResource);

            Resource classNameHashResource = metaData.createResource(URIgenerator.generateURI(), RDFSerializerWrapper.hash);
            //ClassName nodes should be joined with they share groupID, artifactID,  version, and className.
            data.put(classNameHashResource.getURI(), groupID + artifactID + version + className);
            groupIDResource.addProperty(RDFSerializerWrapper.hasHash, classNameHashResource);
        }

        return new DataModel(metaData, data);
    }
}

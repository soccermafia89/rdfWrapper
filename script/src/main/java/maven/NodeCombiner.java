///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package maven;
//
//import com.hp.hpl.jena.query.Query;
//import com.hp.hpl.jena.query.QueryExecution;
//import com.hp.hpl.jena.query.QueryExecutionFactory;
//import com.hp.hpl.jena.query.QueryFactory;
//import com.hp.hpl.jena.query.QuerySolution;
//import com.hp.hpl.jena.query.ResultSet;
//import com.hp.hpl.jena.rdf.model.Model;
//import com.hp.hpl.jena.rdf.model.Resource;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import org.openrdf.model.vocabulary.RDF;
//import rdf.serializer.RDFSerializerWrapper;
//import wrappers.MavenRepoWrapper;
//
///**
// *
// * @author alex
// */
//public class NodeCombiner {
//
//    public static Model linkNodes(Model metaData, HashMap<String, Object> data) {
//        
//        Map<String, Resource> uniqueResources = new HashMap<String, Resource>();
//        
//        //First link group ids.  If the group id data values are identical the resources are linked.
//        String queryString = "SELECT { ?groupID } \n"
//                + "WHERE {"
//                + "?groupID " + RDF.TYPE + " " + MavenRepoWrapper.groupID.getURI()
//                + "}";
//
//        Query query = QueryFactory.create(queryString);
//        QueryExecution queryExecution = QueryExecutionFactory.create(query, metaData);
//        ResultSet resultsSet = queryExecution.execSelect();
//        
//        while(resultsSet.hasNext()) {
//            QuerySolution querySolution = resultsSet.nextSolution();
//            Resource groupIDResource = querySolution.getResource("groupID");
//            
//            String hash = (String) data.get(groupIDResource.getURI());
//            
//            if(uniqueResources.containsKey(hash)) {
//                groupIDResource.addProperty(RDFSerializerWrapper.isLinkedWith, uniqueResources.get(hash));
//            } else {
//                uniqueResources.put(hash, groupIDResource);
//            }
//        }
//        
//        //Next link artifact ids.  If the group id and artifact id are identical then the resources are linked.
//                queryString = "SELECT { ?groupID ?artifactID } \n"
//                + "WHERE {"
//                +  "?x " + MavenRepoWrapper.hasGroupID + " ?groupID \n"      
//                +  "?x " + MavenRepoWrapper.hasArtifactID + " ?artifactID \n"    
//                + "}";
//
//        query = QueryFactory.create(queryString);
//        queryExecution = QueryExecutionFactory.create(query, metaData);
//        resultsSet = queryExecution.execSelect();
//        
//        while(resultsSet.hasNext()) {
//            QuerySolution querySolution = resultsSet.nextSolution();
//            Resource groupIDResource = querySolution.getResource("groupID");
//            Resource artifactIDResource = querySolution.getResource("artifactID");
//            
//            String hash = (String) data.get(groupIDResource.getURI());
//            
//            if(uniqueResources.containsKey(hash)) {
//                groupIDResource.addProperty(RDFSerializerWrapper.isLinkedWith, uniqueResources.get(hash));
//            } else {
//                uniqueResources.put(hash, groupIDResource);
//            }
//        }
//
//        return resultModel;
//    }
//}

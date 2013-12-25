/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stringContent;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import rdf.serializer.DataModel;
import rdf.serializer.URIFactory;
import wrappers.ListWrapper;
import wrappers.StringContentWrapper;

/**
 *
 * @author alex
 */
public class StringAppender {

    public static DataModel appendStringContent(InputStream in, URIFactory URIgenerator) throws IOException {
        Model metaData = ModelFactory.createDefaultModel();
        HashMap<String, Object> data = new HashMap<String, Object>();

        InputStreamReader streamReader = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(streamReader);

        int index = 0;
        String line = null;
        ArrayList<Resource> listNodes = new ArrayList<Resource>();
        while ((line = reader.readLine()) != null) {

            //Create new List resource
            Resource newListNode = metaData.createResource(URIgenerator.generateURI(), ListWrapper.listNode);

            //Generate a unique resource the new line
            Resource tmpIndex = metaData.createResource(URIgenerator.generateURI(), ListWrapper.index);

            //Append index data
            metaData.add(newListNode, ListWrapper.hasIndex, tmpIndex);
            data.put(tmpIndex.getURI(), index);

            //Create string line resource
            Resource stringLineContent = metaData.createResource(URIgenerator.generateURI(), StringContentWrapper.line);
            metaData.add(newListNode, StringContentWrapper.hasLine, stringLineContent);
            data.put(stringLineContent.getURI(), line);

            listNodes.add(newListNode);
            index++;
        }

        //Link list nodes together
        for (int i = 0; i < listNodes.size() - 1; i++) {
            Resource firstNode = listNodes.get(i);
            Resource secondNode = listNodes.get(i + 1);

            metaData.add(firstNode, ListWrapper.hasNext, secondNode);
            metaData.add(secondNode, ListWrapper.hasPrevious, firstNode);
        }

        return new DataModel(metaData, data);
    }
}

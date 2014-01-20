/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package browser;

import com.google.common.io.Files;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import rdf.serializer.DataModel;

/**
 *
 * @author alex
 */
public class Driver {
    
//    private Model metaData;
//    private HashMap<String, Object> data;

    public static void main(String[] args) {
        Driver driver = new Driver();
        try {
            driver.drive(args);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public Driver() {
    }

    public void drive(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
        String persistedPath = args[0];
        String queryPath = args[1];

        String queryString = this.loadQuery(queryPath);
        DataModel dataModel = this.loadDataModel(persistedPath);
        
        System.out.println("Query Found: " + queryString);
        
        //Run the query over the data.
        Model metaData = dataModel.getMetaData();
        HashMap<String, Object> data = dataModel.getData();

      Query query = QueryFactory.create(queryString);
      QueryExecution qexec = QueryExecutionFactory.create(query, metaData);

      ResultSet results = qexec.execSelect();
      while(results.hasNext()) {
          QuerySolution querySolution = results.nextSolution();
          Iterator<String> it = querySolution.varNames();
          while(it.hasNext()) {
              String varName = it.next();
              System.out.println("Var name: " + varName);
          }
      }
                                                                                                                                                                                                     
     qexec.close() ;
    }

    public String loadQuery(String queryPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(queryPath));

        String line = null;
        String content = "";

        while ((line = br.readLine()) != null) {
            content += line;
        }
        
        return content;
    }

    public DataModel loadDataModel(String persistedPath) throws FileNotFoundException, IOException, ClassNotFoundException {
        InputStream file = new FileInputStream(persistedPath);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        DataModel dataModel = (DataModel) input.readObject();
        input.close();
        return dataModel;
    }
}

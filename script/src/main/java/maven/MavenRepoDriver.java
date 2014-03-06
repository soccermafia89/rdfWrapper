/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maven;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import rdf.serializer.DataModel;
import rdf.serializer.Reducer;

/**
 *
 * @author alex
 */
public class MavenRepoDriver {

    Model metaData = ModelFactory.createDefaultModel();
    HashMap<String, Object> data = new HashMap<String, Object>();

    public MavenRepoDriver() {
    }

    public static void main(String[] args) {
        MavenRepoDriver driver = new MavenRepoDriver();
        try {
            driver.drive(args);
        } catch (IOException ex) {
            System.out.println("Failed");
            ex.printStackTrace();
        }
    }

    public void drive(String[] args) throws IOException {

        String repoPath = args[0];
        if (repoPath.charAt(repoPath.length() - 1) == '/') { //strip out the last slash if it exists.
            repoPath = repoPath.substring(0, repoPath.length() - 1);
        }

        System.out.println("Repo Path: " + repoPath);

        String persistPath = args[1];
        if (persistPath.charAt(persistPath.length() - 1) == '/') { //strip out the last slash if it exists.
            persistPath = persistPath.substring(0, persistPath.length() - 1);
        }

        System.out.println("Persist Path: " + persistPath);

        int repoPathLength = repoPath.length();

        String linuxCommand = "find " + repoPath + " -type f ";
        Process proc = Runtime.getRuntime().exec(linuxCommand);

        InputStream in = proc.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(streamReader);

        int count = 0;

        String line = null;
        while ((line = reader.readLine()) != null) {

            line = line.substring(repoPathLength);

            //Only process jar files.
            if (line.endsWith(".jar")) {
                if (count > 5) {
                    System.out.println("Breaking early: " + count);
                    break;
                }
                count++;

                MavenDataObject mavenDataObject = this.createMavenDataObject(line);
                DataModel newDataModel = mavenDataObject.toDataModel();
                metaData.add(newDataModel.getMetaData());
                data.putAll(newDataModel.getData());
            }
        }

        DataModel reducedDataModel = Reducer.reduce(metaData, data);
        metaData = reducedDataModel.getMetaData();
        data = reducedDataModel.getData();

        //Write metadata to file.
        File file = new File(persistPath + "/out.metadata");
        BufferedWriter output = new BufferedWriter(new FileWriter(file));
        metaData.write(output);
        output.close();

        //Write data to file.
        OutputStream fileOutputStream = new FileOutputStream(persistPath + "/out.data");
        OutputStream buffer = new BufferedOutputStream(fileOutputStream);
        ObjectOutput objectOutput = new ObjectOutputStream(buffer);
        objectOutput.writeObject(data);
        objectOutput.flush();
        objectOutput.close();
    }

    public MavenDataObject createMavenDataObject(String line) {
        //First extract out data from the line using string manipulation

        System.out.println("Processing Line: " + line);

        line = line.substring(1);//Remove first slash.

        String[] pathLayouts = line.split("/");
        String jarName = pathLayouts[pathLayouts.length - 1];
        String version = pathLayouts[pathLayouts.length - 2];
        String artifactID = pathLayouts[pathLayouts.length - 3];

        String groupID = "";
        for (int i = 0; i < pathLayouts.length - 3; i++) {
            groupID += pathLayouts[i] + ".";
        }

        groupID = groupID.substring(0, groupID.length() - 1);

        System.out.println("jarName: " + jarName);
        System.out.println("version: " + version);
        System.out.println("artifactID: " + artifactID);
        System.out.println("groupID: " + groupID);

        return new MavenDataObject(groupID, artifactID, jarName, version, new ArrayList());
    }
}

package ethier.alex;

import ethier.alex.genie.jena.impl.GenieJenaProperty;
import ethier.alex.genie.jena.impl.UniqueGenieJenaProperty;
import ethier.alex.genie.jena.impl.MappedJenaInstance;
import ethier.alex.genie.jena.impl.GenieJenaModel;
import ethier.alex.genie.jena.impl.EmptyJenaInstance;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import ethier.alex.genie.data.interfaces.EmptyInstance;
import ethier.alex.genie.data.interfaces.MappedInstance;
import ethier.alex.genie.exception.OverwriteException;
import java.util.Collection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
    
    public void testBasics() {
        System.out.println("Testing basics.");
        
        GenieJenaModel genieModel = new GenieJenaModel();
        
        UniqueGenieJenaProperty isFather = (UniqueGenieJenaProperty) genieModel.createUniqueProperty("isFather");
        UniqueGenieJenaProperty isSibling = (UniqueGenieJenaProperty) genieModel.createUniqueProperty("isSibling");
        UniqueGenieJenaProperty hasUncle = (UniqueGenieJenaProperty) genieModel.createUniqueProperty("hasUncle");
        EmptyInstance grandFather = genieModel.createEmptyInstance();
        EmptyInstance father = genieModel.createEmptyInstance();
        EmptyInstance uncle = genieModel.createEmptyInstance();
        EmptyInstance son = genieModel.createEmptyInstance();
        
        //        System.out.println("Uncle uri: " + uncle.getUri());
        
        grandFather.addObject(isFather, father);
        father.addObject(isFather, son);
        father.addObject(isSibling, father);
        son.addObject(hasUncle, uncle);
        
        EmptyInstance derivedUncle = grandFather.getObject(isFather).getObject(isFather).getObject(hasUncle);
//        System.out.println("Derived uncle uri: " + derivedUncle.getUri());
        
        //Test that graph traversal works.
        assertTrue(((EmptyJenaInstance)uncle).getUri().equals(((EmptyJenaInstance)derivedUncle).getUri()));
        
        EmptyInstance cousin = genieModel.createEmptyInstance();
        uncle.addObject(isFather, cousin);
        
        //Test that getting the same object twice does not create cloned copies.
        EmptyInstance derivedUncle2 = grandFather.getObject(isFather).getObject(isFather).getObject(hasUncle);
        assertTrue(((EmptyJenaInstance)derivedUncle).getUri().equals(((EmptyJenaInstance)derivedUncle2).getUri()));
        
        //Test that modification of graph retains synchronization of objects.
        EmptyInstance derivedCousin = derivedUncle.getObject(isFather);
        assertTrue(((EmptyJenaInstance)derivedCousin).getUri().equals(((EmptyJenaInstance)cousin).getUri()));
    }
    
    public void testMultipleBasic() {
        System.out.println("Testing multiple basics.");
        
        //Test having multiple object links with the same property.
        GenieJenaModel genieModel = new GenieJenaModel();
        
        GenieJenaProperty hasChild = (GenieJenaProperty) genieModel.createProperty("hasChild");
        EmptyInstance parent = genieModel.createEmptyInstance();
        
        EmptyInstance child1 = genieModel.createEmptyInstance();
        EmptyInstance child2 = genieModel.createEmptyInstance();
        EmptyInstance child3 = genieModel.createEmptyInstance();
        
        parent.addObject(hasChild, child1);
        parent.addObject(hasChild, child2);
        parent.addObject(hasChild, child3);
        
        Collection<EmptyInstance> children = parent.getObjects(hasChild);
        assertTrue(children.size() == 3);
        
        //Ensure that unique properties remain unique
        EmptyInstance guest = genieModel.createEmptyInstance();
        
        UniqueGenieJenaProperty hasFather = (UniqueGenieJenaProperty) genieModel.createUniqueProperty("hasFather");
        child1.addObject(hasFather, parent);
        child2.addObject(hasFather, parent);
        try {
            child1.addObject(hasFather, guest);
            assertTrue( false );//An exception should prevent code from reaching here.
        } catch (OverwriteException e) {
            assertTrue( true );//Exception was properly thrown.
        }
    }
    
    public void testPojo() {
        System.out.println("Testing pojo conversion.");
        
        GenieJenaModel genieModel = new GenieJenaModel();
        
        String grandfatherString = "grandfather";
        String fatherString = "father";
        String sonString = "son";
        
        MappedInstance grandfather = genieModel.createMappedInstance(grandfatherString);
        EmptyInstance father = genieModel.createEmptyInstance();
        MappedInstance son = genieModel.createMappedInstance(sonString);
        
        UniqueGenieJenaProperty isFather = (UniqueGenieJenaProperty) genieModel.createUniqueProperty("isFather");
        
        grandfather.addObject(isFather, father);
        father.addObject(isFather, son);
        
        EmptyInstance derivedSon = grandfather.getObject(isFather).getObject(isFather);
        assertTrue(((MappedJenaInstance)son).getUri().equals(((MappedJenaInstance)derivedSon).getUri()));
    }
}

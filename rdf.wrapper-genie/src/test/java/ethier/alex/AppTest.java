package ethier.alex;

import com.hp.hpl.jena.rdf.model.ResourceFactory;
import ethier.alex.genie.data.GenieModel;
import ethier.alex.genie.data.BlankGenieInstance;
import ethier.alex.genie.data.GenieProperty;
import ethier.alex.genie.data.MappedGenieInstance;
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
        
        GenieModel genieModel = new GenieModel();
        
        GenieProperty isFather = genieModel.createProperty("isFather");
        GenieProperty isSibling = genieModel.createProperty("isSibling");
        GenieProperty hasUncle = genieModel.createProperty("hasUncle");
        BlankGenieInstance grandFather = genieModel.createBlankInstance();
        BlankGenieInstance father = genieModel.createBlankInstance();
        BlankGenieInstance uncle = genieModel.createBlankInstance();
        BlankGenieInstance son = genieModel.createBlankInstance();
        
        //        System.out.println("Uncle uri: " + uncle.getUri());
        
        grandFather.addObject(isFather, father);
        father.addObject(isFather, son);
        father.addObject(isSibling, father);
        son.addObject(hasUncle, uncle);
        
        BlankGenieInstance derivedUncle = grandFather.getObject(isFather).getObject(isFather).getObject(hasUncle);
//        System.out.println("Derived uncle uri: " + derivedUncle.getUri());
        
        //Test that graph traversal works.
        assertTrue(uncle.getUri().equals(derivedUncle.getUri()));
        
        BlankGenieInstance cousin = genieModel.createBlankInstance();
        uncle.addObject(isFather, cousin);
        
        //Test that getting the same object twice does not create cloned copies.
        BlankGenieInstance derivedUncle2 = grandFather.getObject(isFather).getObject(isFather).getObject(hasUncle);
        assertTrue(derivedUncle.getUri().equals(derivedUncle2.getUri()));
        
        //Test that modification of graph retains synchronization of objects.
        BlankGenieInstance derivedCousin = derivedUncle.getObject(isFather);
        assertTrue(derivedCousin.getUri().equals(cousin.getUri()));
    }
    
    public void testPojo() {
        
        GenieModel genieModel = new GenieModel();
        
        String grandfatherString = "grandfather";
//        String fatherString = "father";
        String sonString = "son";
        
        MappedGenieInstance grandfather = genieModel.createMappedInstance(grandfatherString);
        BlankGenieInstance father = genieModel.createBlankInstance();
        MappedGenieInstance son = genieModel.createMappedInstance(sonString);
        
        GenieProperty isFather = genieModel.createProperty("isFather");
        
        grandfather.addObject(isFather, father);
        father.addObject(isFather, son);
        
        MappedGenieInstance derivedSon = (MappedGenieInstance) grandfather.getObject(isFather).getObject(isFather).asMappedInstance();
        assertTrue(derivedSon.getPojo().equals(son));
    }
}

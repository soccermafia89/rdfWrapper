package ethier.alex;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import rdf.serializer.URIFactory;

/**
 * Unit test for simple App.
 */
public class AppTest
        extends TestCase {

    private URIFactory URIgenerator = URIFactory.getInstance();

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     *
     * @throws Exception
     */
    public void testBase() throws Exception {
        assertTrue(true);
    }
}

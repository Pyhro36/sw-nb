package similarite;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;

public class URLModelTest {

	/* Model = set of triplets
	 * 	Statement = single triplet
	 * 		Resource = subject
	 * 		RDFNode = object
	 * 		Property = predicate
	 * */
	
	private static URLModel urlModel1;
	private static URLModel urlModel2;
	private static URLModel urlModel3;
	private static URLModel urlModel4;
	private static List<Resource> resources1;
	private static List<Resource> resources2;
	private static List<Resource> resources3;
	private static List<Resource> resources4;
	
	/**
	 * @brief Mis en place du banc de test avant les tests
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {	
		resources1 = new LinkedList<>();
		resources2 = new LinkedList<>();
		resources3 = new LinkedList<>();
		resources3 = new LinkedList<>();
		
		Resource alice = ResourceFactory.createResource("http://example.org/alice");    	
		Resource bob = ResourceFactory.createResource("http://example.org/bob");
		Resource jena = ResourceFactory.createResource("http://example.org/jena");
		
                resources1.add(alice);
		resources1.add(bob);
		resources1.add(jena);
		
		urlModel1 = new URLModel(resources1, "1");
		
		Resource alice1 = ResourceFactory.createResource("http://example.org/alice");    
		Resource bob1 = ResourceFactory.createResource("http://example.org/bob");
                Resource jena1 = ResourceFactory.createResource("http://example.org/jena");
		
		resources2.add(alice1);
		resources2.add(bob1);
		resources2.add(jena1);
		
		urlModel2 = new URLModel(resources2, "2");
                
		alice1.addProperty(RDF.type, FOAF.Person);
		alice1.addProperty(FOAF.name, "Alice");
		alice1.addProperty(FOAF.knows, bob);
		
		bob1.addProperty(RDF.type, FOAF.Person);
		bob1.addProperty(FOAF.name, "Bob");
		bob1.addProperty(FOAF.mbox, ResourceFactory.createResource("mailto:bob@example.org"));
                
                Resource alice2 = ResourceFactory.createResource("http://example.org/alice");    	
		Resource bob2 = ResourceFactory.createResource("http://example.org/bob");
                
		resources3.add(alice2);
		resources3.add(bob2);
                
		urlModel3 = new URLModel(resources3, "3");
                
                alice2.addProperty(RDF.type, FOAF.Person);
		alice2.addProperty(FOAF.name, "Alice");
		alice2.addProperty(FOAF.mbox, ResourceFactory.createResource("mailto:alice@example.org"));
		alice2.addProperty(FOAF.knows, bob);
		
		bob2.addProperty(RDF.type, FOAF.Person);
		bob2.addProperty(FOAF.name, "Bob");
		bob2.addProperty(FOAF.mbox, ResourceFactory.createResource("mailto:bob@example.org"));
		bob2.addProperty(FOAF.knows, alice);

                Resource alice3 = ResourceFactory.createResource("http://example.org/alice");    	
		Resource bob3 = ResourceFactory.createResource("http://example.org/bob");
                
		resources4.add(alice3);
		resources4.add(bob3);
		
		urlModel4 = new URLModel(resources4, "4");
                
                alice3.addProperty(RDF.type, FOAF.Person);
		alice3.addProperty(FOAF.name, "Alice");
		alice3.addProperty(FOAF.knows, bob);
		
		bob3.addProperty(RDF.type, FOAF.Person);
		bob3.addProperty(FOAF.name, "Bob");
		bob3.addProperty(FOAF.mbox, ResourceFactory.createResource("mailto:bob@example.org")); 
	}

	/**
	 * Test method for {@link similarite.URLModel#getResources()}.
	 */
	@Test
	public void testGetResources() {
		URLModel urlModel = new URLModel(null, "0");
		assertEquals(urlModel.getResources(), null);
		assertEquals(urlModel1.getResources(), resources1);
		assertEquals(urlModel2.getResources(), resources2);
		assertEquals(urlModel3.getResources(), resources3);
		assertEquals(urlModel4.getResources(), resources4);	
	}

	/**
	 * Test method for {@link similarite.URLModel#getPlusSimilairesURL(java.util.List, int)}.
	 */
	@Test
	public void testGetPlusSimilairesURL() {
		List<URLModel> urlModels = new LinkedList<>();
		
		urlModels.add(urlModel2);
		urlModels.add(urlModel3);
		urlModels.add(urlModel4);
		
		assertEquals(urlModel1.getPlusSimilairesURL(urlModels, 0).size(), 0);
                List<String> plusSimilairesURL = urlModel1.getPlusSimilairesURL(urlModels, 2);
                assertEquals(plusSimilairesURL.get(0), urlModel3);
                assertEquals(plusSimilairesURL.get(1), urlModel2);
                assertEquals(plusSimilairesURL.size(), 2);
	}

	/**
	 * Test method for {@link similarite.URLModel#similairiteParSujets(similarite.URLModel)}.
	 */
	@Test
	public void testSimilairiteParSujets() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link similarite.URLModel#similairiteParTriplets(similarite.URLModel)}.
	 */
	@Test
	public void testSimilairiteParTriplets() {
		fail("Not yet implemented");
	}
}